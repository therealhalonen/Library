package halonen.library.web;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import halonen.library.domain.Book;
import halonen.library.domain.BookRepository;
import halonen.library.domain.BorrowRepository;
import halonen.library.domain.CategoryRepository;
import halonen.library.domain.Like;
import halonen.library.domain.LikeRepository;
import halonen.library.domain.User;
import halonen.library.domain.UserRepository;
import halonen.library.service.LikeService;

@Controller
public class LikeController {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BorrowRepository borrowRepository;
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private LikeService likeService;

	// Book Liking stuff
	@PostMapping("/like/{bookId}")
	public String likeBook(@PathVariable("bookId") Long bookId, Principal principal) {
		// Retrieve the book by its ID from the repository
		Optional<Book> optionalBook = bookRepository.findById(bookId);

		// Check if the book exists
		if (optionalBook.isEmpty()) {

			return "redirect:/booklist";
		}

		Book book = optionalBook.get();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

		// Get username
		String username = authenticatedUser.getUsername();

		// Check if the user has already liked the book
		boolean userHasLiked = likeService.userHasLikedBook(book, username);

		if (!userHasLiked) {
			// Create a new like
			User currentUser = userRepository.findByUsername(username);
			if (currentUser != null) {
				Like like = new Like();
				like.setBook(book);
				like.setUser(currentUser);
				likeRepository.save(like);
			}
		}

		return "redirect:/booklist";
	}

	// Book Unliking stuff
	@PostMapping("/unlike/{bookId}")
	public String unlikeBook(@PathVariable("bookId") Long bookId, Principal principal) {
		// Retrieve the book by its ID from the repository
		Optional<Book> optionalBook = bookRepository.findById(bookId);

		// Check if the book exists
		if (optionalBook.isEmpty()) {

			return "redirect:/booklist";
		}

		Book book = optionalBook.get();

		// Get username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

		// Then you can use authenticatedUser.getUsername() to get the username.
		String username = authenticatedUser.getUsername();

		// Retrieve the user from the repository based on the username
		User currentUser = userRepository.findByUsername(username);

		// Call the unlikeBook method from LikeService
		likeService.unlikeBook(currentUser, book);

		// Redirect back to the book details page or wherever you want
		return "redirect:/booklist"; // Redirect to the book list page
	}

	// Show liked books by user
	@GetMapping("/likedbooks")
	public String booksYouLiked(Model model) {
		// Get the currently logged-in user's username
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		// Fetch the user by username
		User user = userRepository.findByUsername(username);

		// Fetch the liked books for the user
		Set<Book> likedBooks = user.getLikedBooks();

		// Add the user and liked books to the model
		model.addAttribute("user", user);
		model.addAttribute("likedBooks", likedBooks);

		return "likedbooks";
	}

}
