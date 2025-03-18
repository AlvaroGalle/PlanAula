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
            String dbPath = databaseFile.getFile().getAbsolutePath();
            //String dbPath = "C:/Users/51178456/Desktop/workspace/PlanAula/Access/PNS_Fesd2425.accdb";
            return DataSourceBuilder.create()
                    .driverClassName("net.ucanaccess.jdbc.UcanaccessDriver")
                    .url("jdbc:ucanaccess://" + dbPath +
                            ";openExclusive=false" +
                            ";ignoreCase=true" +
                            ";memory=false" +
                            ";singleConnection=true")
                    .build();
        }
}
