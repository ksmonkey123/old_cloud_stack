package ch.awae.cloud.auth;

import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequest {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
