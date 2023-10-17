package com.example.telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TelegramBotApplication extends SpringBootServletInitializer  {

	private static ApplicationContext applicationContext;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TelegramBotApplication.class);
	}

	public static void main(String[] args) {

		applicationContext = SpringApplication.run(TelegramBotApplication.class, args);

	}

	private static void checkBeansPresence(String... beans) {
		for (String beanName : beans) {
			System.out.println("Is " + beanName + " in ApplicationContext: " +
					applicationContext.containsBean(beanName));
		}
	}

}
