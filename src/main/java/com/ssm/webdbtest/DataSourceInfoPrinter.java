package com.ssm.webdbtest;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct; // 如果是Jakarta EE 9及以上版本

@Component
public class DataSourceInfoPrinter {

    @Autowired
    private HikariDataSource dataSource;

    @PostConstruct
    public void printDataSourceInfo() {
        System.out.println("JDBC URL: " + dataSource.getJdbcUrl());
        System.out.println("Driver: " + dataSource.getDriverClassName());
        System.out.println("Min Pool Size: " + dataSource.getMinimumIdle());
        System.out.println("Max Pool Size: " + dataSource.getMaximumPoolSize());
        System.out.println("AutoCommit: " + dataSource.isAutoCommit());
        // 注意：HikariCP 不直接提供获取事务隔离级别的方法
        // System.out.println("Isolation Level: " + dataSource.getTransactionIsolation());
    }
}
