package halonen.bookstore.web;

import halonen.bookstore.domain.*;
import halonen.bookstore.service.LikeService;
import halonen.bookstore.service.LoanStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
public class BookstoreController {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private LikeService likeService;

	// Login stuffz
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/approval")
	public String approvalPage() {
		return "approval";
	}

	// Create Book
	@GetMapping(value = "/add")
	public String addBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addbook";
	}

	// Create Category
	@GetMapping(value = "/addcategory")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addcategory";
	}

	// Basic booklist with sorting and stuffs
	@GetMapping(value = { "/", "/booklist" })
	public String bookList(Model model, @RequestParam(value = "sortField", defaultValue = "title") String sortField,
			@RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection, Principal principal) {
		List<Book> books = (List<Book>) bookRepository.findAll();
		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
		Sort sort = Sort.by(direction, sortField);

		for (Book book : books) {
			// Check if the book has any likes
			boolean bookHasLikes = !book.getLikes().isEmpty();
			book.setUserHasLiked(bookHasLikes);

			Loan loan = loanRepository.findByBook(book);
			if (loan != null) {
				// If a loan exists, set the status to LOANED
				book.setStatus(LoanStatus.LOANED);
			} else {
				// If no loan exists, set the status to AVAILABLE
				book.setStatus(LoanStatus.AVAILABLE);
			}

			// Check if the current user has liked the book
			boolean userHasLiked = likeService.userHasLikedBook(book, principal.getName());
			book.setUserHasLiked(userHasLiked);
		}

		model.addAttribute("books", books);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("user", userRepository.findAll());

		return "booklist";
	}

	@GetMapping("/booklist/sort")
	public String sortBooks(@RequestParam("field") String field, @RequestParam("direction") String direction) {
		// Redirect to the /booklist page with sorting parameters
		return "redirect:/booklist?sortField=" + field + "&sortDirection=" + direction;
	}

	// Update Book
	@GetMapping(value = "/edit/{id}")
	public String editBook(@PathVariable("id") Long bookId, Model model) {
		model.addAttribute("book", bookRepository.findById(bookId));
		model.addAttribute("categories", categoryRepository.findAll());
		return "editbook";
	}

	// Delete Book
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/delete/{id}")
	public String deleteBook(@PathVariable("id") Long bookId, Model model) {
		bookRepository.deleteById(bookId);
		return "redirect:../booklist";
	}

	// Delete Category
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/deletecategory/{id}")
	public String deleteCategory(@PathVariable("id") Long categoryId) {
		categoryRepository.deleteById(categoryId);
		return "redirect:/addcategory";
	}

	// Save Book
	@PostMapping(value = "/save")
	public String saveBook(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("category", new Category());
			model.addAttribute("book", book);
			model.addAttribute("categories", categoryRepository.findAll());
			return "addbook";
		} else {
			bookRepository.save(book);
			return "redirect:/booklist";
		}
	}

	// Save Category Simple
	@PostMapping(value = "/savecategorysimple")
	public String saveCategorySimple(Category category) {
		categoryRepository.save(category);
		return "redirect:addcategory";
	}

	// Save Category - Complicated Finally working!!!! 3.9.2023
	@PostMapping(value = "/savecategory", produces = "application/json")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> saveCategory(@RequestBody Category category) {
		try {
			// Save the category to the database
			categoryRepository.save(category);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("categoryid", category.getCategoryid());
			response.put("name", category.getName());

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Handles any exceptions that occur during the save operation
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("success", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

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
				BookLike like = new BookLike();
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
		// DEBUG
		System.out.println("Username from authentication: " + username);
		System.out.println("Number of liked books: " + likedBooks.size());
		return "likedbooks"; // Use the appropriate template name
	}
}