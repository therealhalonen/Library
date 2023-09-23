package halonen.library.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import halonen.library.domain.Book;
import halonen.library.domain.BookRepository;
import halonen.library.domain.Borrow;
import halonen.library.domain.BorrowRepository;
import halonen.library.domain.Category;
import halonen.library.domain.CategoryRepository;
import halonen.library.domain.LikeRepository;
import halonen.library.domain.UserRepository;
import halonen.library.service.BookStatus;
import halonen.library.service.LikeService;
import jakarta.validation.Valid;

@Controller
public class BookController {
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

	// Login stuffz
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	// Approval page for newly registered user
	@GetMapping("/approval")
	public String approvalPage() {
		return "approval";
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

			Borrow loan = borrowRepository.findByBook(book);
			if (loan != null) {
				// If a loan exists, set the status to BORROWED
				book.setStatus(BookStatus.BORROWED);
			} else {
				// If no loan exists, set the status to AVAILABLE
				book.setStatus(BookStatus.AVAILABLE);
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
	
	// Create Book
	@GetMapping(value = "/add")
	public String addBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addbook";
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
}