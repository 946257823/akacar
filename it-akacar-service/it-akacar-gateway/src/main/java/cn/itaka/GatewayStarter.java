package cn.itaka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//网关
@SpringBootApplication()
public class GatewayStarter {
    public static void main(String[] args) {
        SpringApplication.run(GatewayStarter.class , args);
    }
}