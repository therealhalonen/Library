package halonen.library.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import halonen.library.domain.Book;
import halonen.library.domain.BookRepository;
import halonen.library.domain.Borrow;
import halonen.library.domain.BorrowRepository;
import halonen.library.domain.User;
import halonen.library.domain.UserRepository;
import halonen.library.service.BookStatus;

@Controller
public class BorrowController {

	@Autowired
	private BorrowRepository borrowRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@PostMapping("/borrow/{bookId}")
	public String borrowBook(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
		// Retrieve the book and user
		Book book = bookRepository.findById(bookId).orElse(null);
		User user = userRepository.findByUsername(userDetails.getUsername());

		if (book != null && user != null) {
			// Create a borrow record (change Borrow status also)
			Borrow borrow = new Borrow();
			borrow.setBook(book);
			borrow.setUser(user);
			book.setStatus(BookStatus.BORROWED);
			System.out.println(book.getStatus());
			// Save the borrow record
			borrowRepository.save(borrow);
		}

		return "redirect:/booklist";
	}

	@PostMapping("/return/{bookId}")
	public String returnBook(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
		// Retrieve the book and user
		Book book = bookRepository.findById(bookId).orElse(null);
		User user = userRepository.findByUsername(userDetails.getUsername());

		if (book != null && user != null) {

			Borrow borrow = book.getBorrow();

			// Update the book status to AVAILABLE, and delete the Borrow from the table
			book.setStatus(BookStatus.AVAILABLE);
			bookRepository.save(book);
			borrowRepository.delete(borrow);
			// }
		}

		return "redirect:/booklist";
	}
}
