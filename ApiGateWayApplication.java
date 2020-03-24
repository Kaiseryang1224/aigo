package com.ctbcbank.aigo.apiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RestController
@RequestMapping("/map")
public class ApiGateWayApplication {

	
	@GetMapping(value = "/aa", params = { "msg1", "msg2", "msg3" })
	public member_ex memberTesta(@RequestParam(name = "msg1", required = false) String msg1,
			@RequestParam(name = "msg2", required = false) String msg2,
			@RequestParam(name = "msg3", required = false) String msg3, Model model) {
		member_ex memberAccount = new member_ex();
		memberAccount.setAddress(msg1);
		memberAccount.setCellphone(msg2 + "xx");
		memberAccount.setEmail(msg3 + "@gmail.com");
		memberAccount.setId(1);
		memberAccount.setPassword("123456789" + msg1 + "@" + msg2 + "%" + msg3);
		return memberAccount;

	}
	
  public static void main(String[] args) {
    SpringApplication.run(ApiGateWayApplication.class, args);
  }

}
