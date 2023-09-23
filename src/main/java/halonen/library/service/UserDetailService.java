package halonen.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import halonen.library.domain.User;
import halonen.library.domain.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {
	private final UserRepository repository;

	@Autowired
	public UserDetailService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User curruser = repository.findByUsername(username);

		UserBuilder builder = null;
		if (curruser == null) {
			throw new UsernameNotFoundException("User not found.");
		} else {
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(curruser.getPasswordHash());
			builder.roles(curruser.getRole());
		}

		return builder.build();
	}

}