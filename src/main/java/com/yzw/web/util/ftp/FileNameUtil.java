package com.yzw.web.util.ftp;

import java.util.Random;

/**
 * @author YaoZuoWei
 * @date 2020/10/12 16:35
 * @Description:生成新文件名的工具类
 */
public class FileNameUtil {

    public static String getFileName(String userId) {
        //1、获取当前时间的毫秒数
        long currentTimeMillis = System.currentTimeMillis();
        //2、创建Random对象，获取0-999之间随机数
        Random random = new Random();
        int randomNum = random.nextInt(999);
        //3、最终的文件名
        //%03d: %是占位符 3:3位数  0:不够3位数补零   d:数字
        String fileName = currentTimeMillis+ String.format("%03d", randomNum)+"_"+userId;
        //0补在后面的写法
        //        String fileName = currentTimeMillis + userId + String.format("%3d0", randomNum);
        //4、返回文件名
        return fileName;
    }

    public static void main(String[] args) {
        String fileName = getFileName("1");
        System.out.println(fileName);
    }
}
