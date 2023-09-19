package halonen.bookstore.domain;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Loan {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id")
	    private Long loanId;

	    @ManyToOne
	    @JoinColumn(name = "userid")
	    private User user;

	    @ManyToOne
	    @JoinColumn(name = "bookid")
	    private Book book;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
