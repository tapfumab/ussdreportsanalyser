package zw.co.netone.ussdreportsanalyser.configs;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public DataSource localDataSource() {
        return DataSourceBuilder.create()
                .url(environment.getRequiredProperty("spring.datasource.url"))
                .username(environment.getRequiredProperty("spring.datasource.username"))
                .password(environment.getRequiredProperty("spring.datasource.password"))
                .driverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"))
                .build();
    }

    @Bean
    public DataSource selfCareDataSource() {
        return DataSourceBuilder.create()
                .url(environment.getRequiredProperty("spring.datasource-secondary.url"))
                .username(environment.getRequiredProperty("spring.datasource-secondary.username"))
                .password(environment.getRequiredProperty("spring.datasource-secondary.password"))
                .driverClassName(environment.getRequiredProperty("spring.datasource-secondary.driver-class-name"))
                .build();
    }

    @Bean
    public JdbcTemplate selfCareJdbcTemplate(DataSourceConfig dataSource) {
        return new JdbcTemplate(selfCareDataSource());
    }

    @Bean
    public JdbcTemplate primaryJdbcTemplate(DataSourceConfig dataSource) {
        return new JdbcTemplate(localDataSource());
    }
}
