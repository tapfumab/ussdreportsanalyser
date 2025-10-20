package zw.co.netone.ussdreportsanalyser.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "active.directory")
@Getter @Setter
public class ActiveDirectoryProperties {
    private String domain;
    private String url;
    private String rootDn;
}
