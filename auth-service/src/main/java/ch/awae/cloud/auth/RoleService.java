package ch.awae.cloud.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.val;

@Service
@AllArgsConstructor
public class RoleService {

	private RoleRepository roleRepo;

	public List<String> getCompletedRoleList(List<String> basic) {
		val roles = roleRepo.findAll();
		val result = new ArrayList<String>();
		if (basic.contains("ROLE_ADMIN")) {
			for (val role : roles)
				result.add(role.getRole());
		} else {
			result.addAll(basic);
		}

		if (!result.contains("ROLE_USER"))
			result.add("ROLE_USER");

		return result;
	}

}
