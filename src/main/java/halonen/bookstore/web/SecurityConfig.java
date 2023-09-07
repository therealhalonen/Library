package halonen.bookstore.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import halonen.bookstore.service.UserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@ComponentScan("halonen.bookstore")
public class SecurityConfig {
	@Autowired
	private UserDetailService userDetailsService;
	
    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
		return http
				.authorizeHttpRequests(
						authorizeHttpRequests -> authorizeHttpRequests
								.requestMatchers(
										mvcMatcherBuilder.pattern("/css/**"),
										mvcMatcherBuilder.pattern("/signup"), 
										mvcMatcherBuilder.pattern("/saveuser"))
								.permitAll()
								.requestMatchers(
										mvcMatcherBuilder.pattern("/booklist"))
								.hasAnyRole("USER", "ADMIN")
								.requestMatchers(
										mvcMatcherBuilder.pattern("/js/*"),
										mvcMatcherBuilder.pattern("/add"),
										mvcMatcherBuilder.pattern("/addcategory"),
										mvcMatcherBuilder.pattern("/delete"),
										mvcMatcherBuilder.pattern("/editbook"),
										mvcMatcherBuilder.pattern("/savecategory"))
								
								.hasRole("ADMIN")
								
								.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/booklist", true).permitAll())
				.logout(logout -> logout.permitAll()).build();
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
