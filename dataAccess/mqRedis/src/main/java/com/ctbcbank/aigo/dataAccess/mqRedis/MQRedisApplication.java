package com.ctbcbank.aigo.dataAccess.mqRedis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MQRedisApplication {

  public static void main(String[] args) {
    SpringApplication.run(MQRedisApplication.class, args);
  }
}
