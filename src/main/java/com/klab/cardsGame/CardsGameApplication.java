package com.klab.cardsGame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootApplication
public class CardsGameApplication {

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {		
		
		SpringApplication.run(CardsGameApplication.class, args);
        
	}

}
