package ch.awae.cloud.auth;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import lombok.val;

public class RoleServiceTest {

	private Role[] roles;
	private RoleRepository repo;
	private RoleService service;

	@Before
	public void setup() {
		roles = new Role[] { //
				new Role(1l, 1, "ROLE_ADMIN", "Admin"), //
				new Role(2l, 2, "ROLE_SHORTEN", "Shorten Service"), //
				new Role(3l, 2, "ROLE_ELITE", "Elite Service"), //
		};
		repo = mock(RoleRepository.class);
		when(repo.findAll()).thenReturn(Arrays.asList(roles));
		service = new RoleService(repo);
	}

	@Test
	public void testSimpleUserIsNotTouched() {
		val res = service.getCompletedRoleList(Arrays.asList("ROLE_USER"));
		assertEquals(1, res.size());
		assertEquals("ROLE_USER", res.get(0));
	}

	@Test
	public void testEmptyListProducesUser() {
		val res = service.getCompletedRoleList(Collections.emptyList());
		assertEquals(1, res.size());
		assertEquals("ROLE_USER", res.get(0));
	}

	@Test
	public void testAdminGivesFullList() {
		val res = service.getCompletedRoleList(Arrays.asList("ROLE_ADMIN"));
		assertEquals(roles.length + 1, res.size());
		assertTrue(res.contains("ROLE_USER"));
		for (Role r : roles)
			assertTrue(res.contains(r.getRole()));
	}

	@Test
	public void testMissingUserIsAdded() {
		val res = service.getCompletedRoleList(Arrays.asList("ROLE_SHORTEN"));
		assertEquals(2, res.size());
		assertTrue(res.contains("ROLE_USER"));
		assertTrue(res.contains("ROLE_SHORTEN"));
	}

	@Test
	public void testPartialListIsStable() {
		val res = service.getCompletedRoleList(Arrays.asList("ROLE_SHORTEN", "ROLE_USER"));
		assertEquals(2, res.size());
		assertTrue(res.contains("ROLE_USER"));
		assertTrue(res.contains("ROLE_SHORTEN"));
	}

	@Test
	public void testPartialAdminListGetsCompleted() {
		val res = service.getCompletedRoleList(Arrays.asList("ROLE_ADMIN", "ROLE_SHORTEN", "ROLE_USER"));
		assertEquals(roles.length + 1, res.size());
		assertTrue(res.contains("ROLE_USER"));
		for (Role r : roles)
			assertTrue(res.contains(r.getRole()));
	}

}
