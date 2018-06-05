package ch.awae.cloud.test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import ch.awae.cloud.security.Security;

@RestController
public class Test {

	@Secured("ROLE_USER")
	@GetMapping("/safe")
	public String test() {
		System.out.println(Security.getUser());
		return "Hello, " + Security.getUser().getUsername();
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/admin")
	public String secure() {
		System.out.println(Security.getUser());
		return "Hello, Admin " + Security.getUser().getUsername();
	}
	
	@GetMapping("/public")
	public String test2(HttpServletRequest request) {
		return request.getRemoteHost();
		//return "Hello World";
	}

}
