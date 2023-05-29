package com.rodrigo.helpdesk.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	@Value("${jwt.expiration}")
	private Long expiration; //recebe o valor declarado em appliction.properties
	
	@Value("${jwt.secret}")
	private String secret; //recebe o valor declarado em appliction.properties
	
	public String generateToken(String email) {		
		return Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(System.currentTimeMillis() + expiration)) //momento atual + 3 minutos
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact(); //compacta o corpo do JWT deixando nossa api mais performática
	}
}
