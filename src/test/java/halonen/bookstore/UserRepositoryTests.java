package halonen.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import halonen.bookstore.domain.User;
import halonen.bookstore.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@DataJpaTest
public class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testSaveAndFindById() {
		// Create a User entity and save it to the repository
		User user = new User();
		user.setUsername("testuser");
		user.setPasswordHash("testpasswordhash");
		user.setRole("USER");
		userRepository.save(user);

		// Retrieve the saved user by ID
		Optional<User> foundUser = userRepository.findById(user.getId());

		// Assert that the retrieved user matches the saved user
		assertThat(foundUser).isPresent(); // Check if the user is present
		assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
		assertThat(foundUser.get().getPasswordHash()).isEqualTo("testpasswordhash");
		assertThat(foundUser.get().getRole()).isEqualTo("USER");
	}

	@Test
	public void testFindByUsername() {
		// Create and save a user to the repository
		User user = new User("testuser", "testpasswordhash", "USER");
		userRepository.save(user);

		// Retrieve the user by username
		User foundUser = userRepository.findByUsername("testuser");

		// Assert that the retrieved user matches the saved user
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getUsername()).isEqualTo("testuser");
		assertThat(foundUser.getPasswordHash()).isEqualTo("testpasswordhash");
		assertThat(foundUser.getRole()).isEqualTo("USER");
	}

	@Test
	public void testDelete() {
		// Create and save a user to the repository
		User user = new User("userToDelete", "passwordHash", "USER");
		userRepository.save(user);

		// Delete the user from the repository
		userRepository.delete(user);

		// Try to retrieve the deleted user by ID
		Optional<User> deletedUser = userRepository.findById(user.getId());

		// Assert that the deleted user is not present (null)
		assertThat(deletedUser).isNotPresent();
	}

}
