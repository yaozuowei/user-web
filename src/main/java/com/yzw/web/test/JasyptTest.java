package com.yzw.web.test;


import com.yzw.web.common.util.SecurityEncodeUtil;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author YaoZuoWei
 * @date 2021/1/7 14:48
 * @Description:
 */
@SpringBootTest
public class JasyptTest {


    @Test
    public void testEncrypt() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        // 加密的算法，这个算法是默认的
        config.setAlgorithm("PBEWithMD5AndDES");
        // 加密的密钥，随便自己填写，很重要千万不要告诉别人
        config.setPassword("jasypt-hnxh-harvey");
        standardPBEStringEncryptor.setConfig(config);
        //自己的密码
        String plainText = "Bee2022xmf22";
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }

    @Test
    public void testDe() throws Exception {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("yaozuowei");
        standardPBEStringEncryptor.setConfig(config);
        //加密后的密码
        String encryptedText = "/Qwe1EBzhlI31tW81PN6uEGzD+xRC/SxQnVdOIKIcZBd+On9dLjucQ==";
        String plainText = standardPBEStringEncryptor.decrypt(encryptedText);
        System.out.println(plainText);
    }

    @Test
    public void password(){
        String nePassword= SecurityEncodeUtil.encode("123456");
        System.out.println(nePassword);
    }
}
