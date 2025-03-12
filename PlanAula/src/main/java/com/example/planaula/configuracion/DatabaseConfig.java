package com.example.planaula.configuracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class DatabaseConfig {

    @Value("classpath:database/PNS_Fesd2425.accdb")
    private Resource databaseFile;

    @Bean
    public DataSource dataSource() throws IOException {
        String absolutePath = databaseFile.getFile().getAbsolutePath();
        return DataSourceBuilder.create()
                .driverClassName("net.ucanaccess.jdbc.UcanaccessDriver")
                .url("jdbc:ucanaccess://" + absolutePath)
                .build();
    }
}
