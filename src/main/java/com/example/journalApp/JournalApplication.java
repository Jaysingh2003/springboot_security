package com.example.journalApp;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class JournalApplication {


@GetMapping("/")
public String greet(HttpServletRequest request){
	return "welcome to project :-"+request.getSession().getId();
}

//	@GetMapping("/token")
//	public CsrfToken getCsrfToken(HttpServletRequest request) {
//		return (CsrfToken) request.getAttribute("_csrf");
//	}


	public static void main(String[] args) {
		SpringApplication.run(JournalApplication.class, args);
	}

}
