package com.halfkiloofcarrots.recipepuller;

import com.halfkiloofcarrots.recipepuller.dto.RecipeDataDTO;
import com.halfkiloofcarrots.recipepuller.service.AniaProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@EnableFeignClients
@SpringBootApplication
public class RecipePullerApplication {

	private final AniaProxy proxy;
	public static void main(String[] args) {
		SpringApplication.run(RecipePullerApplication.class, args);

	}
	@EventListener(ApplicationStartedEvent.class)
	public void run(){
		RecipeDataDTO obj =proxy.getRecipe("zeberka-w-kapuscie");
		System.out.println(obj);
	}


}
