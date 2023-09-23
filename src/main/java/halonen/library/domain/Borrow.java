package halonen.library.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Borrow {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long borrowId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "bookid")
	private Book book;
	
	// Constructors
	
	public Long getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(Long loanId) {
		this.borrowId = loanId;
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
