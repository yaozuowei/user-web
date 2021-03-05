package com.yzw.web.util.ftp;

import com.yzw.web.common.util.DateUtils;
import com.yzw.web.exception.PlatException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YaoZuoWei
 * @date 2020/10/12 16:26
 * @Description: 上传的工具类
 */
@Slf4j
public class FtpUtil {


    /**
     * ftp上传
     *
     * @param ftpProperties
     * @param file
     * @param loginName
     * @return
     * @throws IOException
     * @throws PlatException
     */
    public static String uploadFile(FtpProperties ftpProperties, MultipartFile file, String loginName) throws IOException, PlatException {
        //老文件名
        String oldFileName = file.getOriginalFilename();
        //根据登录人调用工具类生成新文件名

        String newFileName = FileNameUtil.getFileName(loginName);

        //截取老文件名的后缀
        String substring = FilenameUtils.getExtension(file.getOriginalFilename());
        //将后缀放在新文件名的后面
        newFileName = newFileName +"."+ substring;
        //生成路径
        String filePath = DateUtils.datePath();

        boolean resultBoolean = upload(ftpProperties, filePath, newFileName, file.getInputStream());
        if (!resultBoolean) {
            throw new PlatException("上传失败!");
        }

        return ftpProperties.getHttpPath() + filePath + "/" + newFileName;
    }


    /**
     * 从FTP下载文件到本地
     *
     * @param fileName      FTP上的目标文件路径+文件名称
     * @param localFilePath 下载到本地的文件路径
     * @param servicePath   服务器的上面文件的上层路径
     */
    public static File downloadFile(FtpProperties ftpProperties, String servicePath, String fileName, String localFilePath) {
        String name = dowFile(ftpProperties, servicePath, fileName, localFilePath);
        if (name != null && !name.equals("")) {
            return new File(name);
        } else {
            return null;
        }
    }

    /**
     * 获取FTP某一特定目录下的所有文件名称
     *
     * @param ftpProperties ftp参数
     * @param ftpDirPath    FTP上的目标文件路径
     */
    public static List<String> getFileNameList(FtpProperties ftpProperties, String ftpDirPath) throws PlatException {
        List<String> list = new ArrayList();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient = loginFTP(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword());

            if (ftpDirPath.startsWith("/") && ftpDirPath.endsWith("/")) {
                // 通过提供的文件路径获取FTPFile对象列表
                FTPFile[] files = ftpClient.listFiles(ftpDirPath);
                // 遍历文件列表，打印出文件名称
                for (int i = 0; i < files.length; i++) {
                    FTPFile ftpFile = files[i];
                    // 此处只打印文件，未遍历子目录（如果需要遍历，加上递归逻辑即可）
                    if (ftpFile.isFile()) {
                        log.info(ftpDirPath + ftpFile.getName());
                        list.add(ftpFile.getName());
                    }
                }
                log.info("当前FTP路径可用");
            } else {
                log.info("当前FTP路径不可用");
                throw new PlatException("当前FTP路径不可用");
            }
            // .退出ftp
            ftpClient.logout();
        } catch (IOException e) {
            log.error("错误" + e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    // .断开ftp的连接
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return list;
    }


    /**
     * 登陆FTP并获取FTPClient对象
     *
     * @param host     FTP主机地址
     * @param port     FTP端口
     * @param userName 登录用户名
     * @param password 登录密码
     * @return
     */
    public static FTPClient loginFTP(String host, int port, String userName, String password) throws PlatException {
        FTPClient ftpClient = new FTPClient();
        ;
        try {

            //、定义返回的状态码
            int reply;
            //、连接ftp(当前项目所部署的服务器和ftp服务器之间可以相互通讯，表示连接成功)
            ftpClient.connect(host, port);
            //、输入账号和密码进行登录
            ftpClient.login(userName, password);
            //、接受状态码(如果成功，返回，如果失败返回)
            reply = ftpClient.getReplyCode();
            //、根据状态码检测ftp的连接，调用isPositiveCompletion(reply)-->如果连接成功返回true，否则返回false
            if (!FTPReply.isPositiveCompletion(reply)) {
                //说明连接失败，需要断开连接
                ftpClient.disconnect();
                throw new PlatException("连接FTP失败，用户名或密码错误。");
            } else {
                log.info("FTP连接成功!");
            }

        } catch (Exception e) {
            log.error("登陆FTP失败，请检查FTP相关配置信息是否正确！" + e);
            return null;
        }
        return ftpClient;
    }


    /**
     * ftp上传到服务器
     */
    public static Boolean upload(FtpProperties ftpProperties, String filePath, String fileName, InputStream inputStream) throws IOException {
        //、创建临时路径
        String tempPath = "";
        //、创建FTPClient对象（对于连接ftp服务器，以及上传和上传都必须要用到一个对象）
        FTPClient ftp = new FTPClient();
        try {

            ftp = loginFTP(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword());

            //、changWorkingDirectory(linux上的文件夹)：检测所传入的目录是否存在，如果存在返回true，否则返回false
            //basePath+filePath-->home/ftp/www///
            if (!ftp.changeWorkingDirectory(ftpProperties.getBasePath() + filePath)) {
                //、截取filePath://-->String[]:,,
                String[] dirs = filePath.split("/");
                //、把basePath(/home/ftp/www)-->tempPath
                tempPath = ftpProperties.getBasePath();
                for (String dir : dirs) {
                    //、循环数组(第一次循环-->)
                    if (null == dir || "".equals(dir)) {
                        continue;//跳出本地循环，进入下一次循环
                    }
                    //、更换临时路径：/home/ftp/www/
                    tempPath += "/" + dir;
                    //、再次检测路径是否存在(/home/ftp/www/)-->返回false，说明路径不存在
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        //、makeDirectory():创建目录  返回Boolean雷类型，成功返回true
                        if (!ftp.makeDirectory(tempPath)) {
                            log.error("创建文件夹出现异常");
                            return false;
                        } else {
                            //、严谨判断（重新检测路径是否真的存在(检测是否创建成功)）
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }

            //.把文件转换为二进制字符流的形式进行上传
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //被动模式
            ftp.enterLocalPassiveMode();
            //设置传输方式为流方式
            ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);

            ftp.setControlEncoding("UTF-8");
            // 设置缓冲区大小
            ftp.setBufferSize(3072);


            long strat=System.currentTimeMillis();
            //、这才是真正上传方法storeFile(filename,input),返回Boolean雷类型，上传成功返回true
            boolean flag=ftp.storeFile(fileName, inputStream);
            if (flag) {
                long end=System.currentTimeMillis();
                log.info(String.format("文件:%s上传成功,耗时：%d毫秒",fileName,(end-strat)));
            }else {
                log.error("上传时出现异常");
                return false;
            }
            // .关闭输入流
            inputStream.close();
            // .退出ftp
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    // .断开ftp的连接
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return true;
    }


    /**
     * 从FTP下载文件到本地
     *
     * @param fileName      FTP上的目标文件路径+文件名称
     * @param localFilePath 下载到本地的文件路径
     * @param servicePath   服务器的上面文件的上层路径
     */
    public static String dowFile(FtpProperties ftpProperties, String servicePath, String fileName, String localFilePath) {
        InputStream is = null;
        FileOutputStream fos = null;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient = loginFTP(ftpProperties.getHost(), ftpProperties.getPort(), ftpProperties.getUsername(), ftpProperties.getPassword());
            // 获取ftp上的文件
            is = ftpClient.retrieveFileStream(servicePath + fileName);

            //下载到本地文件
            File desc = new File(localFilePath + fileName);
            //是否需要新建文件夹
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            if (!desc.exists()) {
                desc.createNewFile();
            }
            fos = new FileOutputStream(desc);
            // 文件读取方式一
            int i;
            byte[] bytes = new byte[1024];
            while ((i = is.read(bytes)) != -1) {
                fos.write(bytes, 0, i);
            }
            // 文件读取方式二
            //ftpClient.retrieveFile(ftpFilePath, new FileOutputStream(new File(localFilePath)));
            ftpClient.completePendingCommand();
            // .退出ftp
            ftpClient.logout();
            log.info("FTP文件下载成功！");
        } catch (Exception e) {
            log.error("FTP文件下载失败！" + e);
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    // .断开ftp的连接
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error("下载流关闭失败" + e);
                return null;
            }
        }
        return localFilePath + fileName;
    }


    /**
     * 判断ftp服务器文件是否存在
     *
     * @param ftpClient
     * @param path
     * @return
     * @throws IOException
     */
    private static boolean existFile(FTPClient ftpClient, String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }


}
