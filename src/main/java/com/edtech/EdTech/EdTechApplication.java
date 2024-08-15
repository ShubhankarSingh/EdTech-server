package com.edtech.EdTech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class EdTechApplication {
	public static void main(String[] args) {
		SpringApplication.run(EdTechApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value="Shubhankar", defaultValue = "world") String name){
		return String.format("Hello %s!", name);
	}

}
