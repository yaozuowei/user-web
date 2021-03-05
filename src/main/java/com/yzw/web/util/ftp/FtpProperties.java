package com.yzw.web.util.ftp;

/**
 * @author YaoZuoWei
 * @date 2020/10/12 16:26
 * @Description:
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "file.ftp")
public class FtpProperties {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String basePath;
    private String httpPath;
}
