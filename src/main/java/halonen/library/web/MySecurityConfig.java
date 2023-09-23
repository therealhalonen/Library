package halonen.library.web;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import halonen.library.service.UserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class MySecurityConfig {
	@Autowired
	private UserDetailService userDetailsService;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@SuppressWarnings("removal")
	@Bean
	@Order(1)
	SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		return http.securityMatcher("/api/**").authorizeHttpRequests(auth -> {
			auth.anyRequest().hasRole("ADMIN");
		}).httpBasic().and().csrf().disable().build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(antMatcher("/css/**"), antMatcher("/login"), antMatcher("/signup"),
						antMatcher("/saveuser"))
				.permitAll()
				// Users and Admins
				.requestMatchers(antMatcher("/loan/**"), antMatcher("/likedbooks"), antMatcher("/unlike/**"),

						antMatcher("/booklist"))
				.hasAnyRole("USER", "ADMIN")
				// Admin only
				.requestMatchers(antMatcher("/js/**"), antMatcher("/add"), antMatcher("/addcategory"),
						antMatcher("/delete/**"), antMatcher("/edit/**"), antMatcher("/savecategory"),
						antMatcher("/deletecategory/**"))
				.hasRole("ADMIN")
				// Temp user
				.requestMatchers(antMatcher("/approval")).hasRole("TEMP").anyRequest().authenticated()

		).headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()) // for h2console
		).formLogin(formLogin -> formLogin.loginPage("/login").successHandler(authenticationSuccessHandler) // Use
																											// custom
																											// success
																											// handler
				.permitAll()).logout(logout -> logout.permitAll());

		return http.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Controller
	public class LogoutController {

		@GetMapping("/custom-logout")
		public String logout(HttpServletRequest request, HttpServletResponse response) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			return "redirect:/login?logout"; // Redirect to the login page after logout
		}
	}

	// Remove Role Prefix TODO, Doesnt work atm 07.09.2023
	public GrantedAuthorityDefaults grantedAuthorityDefaults() {
		return new GrantedAuthorityDefaults("");
	}
}