package com.omegleclone.security;

import java.util.List;

public class NoSecurityPaths {
	
	public static final List<String> PATHS_TO_SKIP_LOGIN = List.of("/public/**", "test-controller/test-method",
			"/api/open/**");

}
