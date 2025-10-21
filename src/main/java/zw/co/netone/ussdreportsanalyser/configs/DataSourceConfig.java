package zw.co.netone.ussdreportsanalyser.configs;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final Environment environment;

    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    public DataSource localDataSource() {
        return DataSourceBuilder.create()
                .url(environment.getRequiredProperty("local.datasource.url"))
                .username(environment.getRequiredProperty("local.datasource.username"))
                .password(environment.getRequiredProperty("local.datasource.password"))
                .driverClassName(environment.getRequiredProperty("local.datasource.driver-class-name"))
                .build();
    }

//    @Bean
//    public DataSource xyzDataSource() {
//        return DataSourceBuilder.create()
//                .url(environment.getRequiredProperty("xyz.datasource.url"))
//                .username(environment.getRequiredProperty("xyz.datasource.username"))
//                .password(environment.getRequiredProperty("xyz.datasource.password"))
//                .driverClassName(environment.getRequiredProperty("xyz.datasource.driver-class-name"))
//                .build();
//    }

//    @Bean
//    public JdbcTemplate xyzJdbcTemplate(DataSourceConfig dataSource) {
//        return new JdbcTemplate(xyzDataSource());
//    }

    @Bean
    public JdbcTemplate primaryJdbcTemplate(DataSourceConfig dataSource) {
        return new JdbcTemplate(localDataSource());
    }
}
