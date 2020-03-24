package com.ctbcbank.aigo.configMgr.configCli.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
  @Value("${aigo.hello}")
  private String hello;

  @RequestMapping("/hello")
  public String hello() {
    return hello;
  }
}
