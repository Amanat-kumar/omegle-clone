package com.omegleclone.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginResponse {

	String userId;
	Date issueAt;
	Date expireIn;
	String token;

}
