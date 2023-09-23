package halonen.library.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import halonen.library.domain.User;
import halonen.library.domain.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public String adminPage(Model model) {
		// Fetch the list of users from the repository
		Iterable<User> users = userRepository.findAll();

		// Add the list of users to the model
		model.addAttribute("users", users);

		return "admin";
	}

	@PostMapping("/delete-user")
	public String deleteUser(@RequestParam("userId") Long userId) {
		// Delete the user by ID
		userRepository.deleteById(userId);

		// Redirect back to the user list
		return "redirect:/admin";
	}

	@PostMapping("/update-role")
	public String updateRole(@RequestParam("userId") Long userId, @RequestParam("newRole") String newRole) {
		// Find the user by ID
		User user = userRepository.findById(userId).orElse(null);

		if (user != null) {
			// Update the users role
			user.setRole(newRole);

			// Save the updated user
			userRepository.save(user);
		}

		// Redirect back to the user list
		return "redirect:/admin";
	}
}
