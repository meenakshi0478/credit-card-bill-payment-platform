package com.credpay.platform;

import com.credpay.platform.security.AppProperties;
import com.credpay.platform.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.List;

@SpringBootApplication
@RestController
public class CreditCardBillPaymentPlatformApplication {

	@Autowired
	private EmailSenderService senderService;

	public static void main(String[] args) {
		SpringApplication.run(CreditCardBillPaymentPlatformApplication.class, args);
	}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}

}
