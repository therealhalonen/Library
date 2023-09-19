package halonen.bookstore.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import halonen.bookstore.domain.Book;
import halonen.bookstore.domain.BookRepository;
import halonen.bookstore.domain.Loan;
import halonen.bookstore.domain.LoanRepository;
import halonen.bookstore.domain.User;
import halonen.bookstore.domain.UserRepository;
import halonen.bookstore.service.LoanStatus;

import java.util.Date;

@Controller
public class LoanController {
	
	
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;
    
    @PostMapping("/loan/{bookId}")
    public String loanBook(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the book and user
        Book book = bookRepository.findById(bookId).orElse(null);
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (book != null && user != null) {
            // Create a loan record
            Loan loan = new Loan();
            loan.setBook(book);
            loan.setUser(user);
            book.setStatus(LoanStatus.LOANED);
            System.out.println(book.getStatus());
            // Save the loan record
            loanRepository.save(loan);
        }

        return "redirect:/booklist";
    }
    
    @PostMapping("/return/{bookId}")
    public String returnBook(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        // Retrieve the book and user
        Book book = bookRepository.findById(bookId).orElse(null);
        User user = userRepository.findByUsername(userDetails.getUsername());

        if (book != null && user != null) {
            // Check if the book is loaned by the current user
            if (book.getStatus() == LoanStatus.LOANED && book.getLoan().getUser().getUsername().equals(user.getUsername())) {

                Loan loan = book.getLoan();
                
                // Update the book status to AVAILABLE, and delete the Loan from the table
                book.setStatus(LoanStatus.AVAILABLE);
                bookRepository.save(book);
                loanRepository.delete(loan);
            }
        }

        return "redirect:/booklist";
    }
}
