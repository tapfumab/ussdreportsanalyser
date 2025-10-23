package zw.co.netone.ussdreportsanalyser.configs;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "zw.co.netone.ussdreportsanalyser.repository", // Package for JPA Repositories
        entityManagerFactoryRef = "localEntityManagerFactory",
        transactionManagerRef = "localTransactionManager"
)
public class PrimaryDataSourceConfig {

    private final Environment environment;

    public PrimaryDataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    @Primary
    @Bean(name="localDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource localDataSource() {
        return DataSourceBuilder.create()
                .url(environment.getRequiredProperty("spring.datasource.url"))
                .username(environment.getRequiredProperty("spring.datasource.username"))
                .password(environment.getRequiredProperty("spring.datasource.password"))
                .driverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"))
                .build();
    }
    @Primary
    @Bean(name = "localEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localEntityManagerFactory(
            @Qualifier("localDataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        // Important: Set the package where your JPA entities (User, Role, Shop, etc.) are located
        em.setPackagesToScan("zw.co.netone.ussdreportsanalyser.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        // Set JPA/Hibernate properties explicitly
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", environment.getProperty("spring.jpa.hibernate.ddl-auto", "none"));
        properties.put("hibernate.show_sql", environment.getProperty("spring.jpa.show-sql", "false"));
        properties.put("hibernate.dialect", environment.getRequiredProperty("spring.jpa.properties.hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }
    @Primary
    @Bean(name = "localTransactionManager")
    public PlatformTransactionManager localTransactionManager(
            @Qualifier("localEntityManagerFactory") EntityManagerFactory localEntityManagerFactory) {

        return new JpaTransactionManager(localEntityManagerFactory);
    }

    @Bean(name = "selfCareDataSource")
    @ConfigurationProperties(prefix = "spring.datasource-secondary")
    public DataSource selfCareDataSource() {
        return DataSourceBuilder.create()
                .url(environment.getRequiredProperty("spring.datasource-secondary.url"))
                .username(environment.getRequiredProperty("spring.datasource-secondary.username"))
                .password(environment.getRequiredProperty("spring.datasource-secondary.password"))
                .driverClassName(environment.getRequiredProperty("spring.datasource-secondary.driver-class-name"))
                .build();
    }


    @Bean(name = "selfCareJdbcTemplate")
    public JdbcTemplate selfCareJdbcTemplate(@Qualifier("selfCareDataSource")  DataSource selfCareDataSource) {
        return new JdbcTemplate(selfCareDataSource);
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("localDataSource") DataSource localDataSource) {
        return new JdbcTemplate(localDataSource);
    }
}
