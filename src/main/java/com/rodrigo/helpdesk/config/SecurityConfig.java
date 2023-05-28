package com.rodrigo.helpdesk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

	@Bean
	UserDetailsService users() {
		UserDetails user = User.builder()
				.username("user")
				.password("{bcrypt}$2a$10$xYPdGnu5EyJ8eI16CEHDreuxHPkQSCh0pZQDDsWPVlfBBOTnXu3Z2") //senha admin
				.roles("USER")
				.build();
		return new InMemoryUserDetailsManager(user);
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();	//permite requisições POST, PUT, DELETE e acesso à console do H2 
		http.headers().frameOptions().sameOrigin();	//permite a visualização dos paineis internos da console do H2
		http.authorizeHttpRequests().antMatchers("/h2-console/**").permitAll(); //evita que o http.httpBasic() peça usuário e 
																//senhas cadastrados em memória para acessar a console do H2
				
		http.httpBasic(); //sem httpBasic todas as requisições terminam em 403 Forbiden
		http.authorizeHttpRequests().anyRequest().authenticated();	//requisições não autenticadas são bloqueadas
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //servidor deixa de enviar cookies JSESSIONID
					
		return http.build();
	}
	
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }	
	
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//    	CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
//    	configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
//    	
//    	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    	source.registerCorsConfiguration("/**", configuration);
//    	
//    	return source;
//    }	
}



















