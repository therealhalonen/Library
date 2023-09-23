package halonen.library.domain;

import java.util.HashSet;
import java.util.Set;

import halonen.library.service.BookStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "title")
	private String title;

	@Column(name = "author")
	private String author;

	@NotNull
	@Column(name = "publicationYear")
	@Min(value = 1000, message = "Ain't that a little bit old book or what?")
	@Max(value = 2023, message = "Can't be from the future!")
	private int publicationYear;

	@NotNull
	@Column(name = "isbn")
	private String isbn;

	@NotNull
	@Column(name = "price")
	private double price;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "categoryid")
	private Category category;

	@OneToOne(mappedBy = "book")
	private Borrow borrow;

	@Enumerated(EnumType.STRING)
	private BookStatus status;

	@OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
	private final Set<Like> likes = new HashSet<>();

	@Transient
	private boolean userHasLiked;

	@ManyToMany(mappedBy = "likedBooks")
	private Set<User> likedUsers = new HashSet<>();

	public Book() {
	}

	public Book(String title, String author, int publicationYear, String isbn, double price, Category category) {
		this.title = title;
		this.author = author;
		this.publicationYear = publicationYear;
		this.isbn = isbn;
		this.price = price;
		this.category = category;
	}

	// Constructors
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Borrow getBorrow() {
		return borrow;
	}

	public void setBorrow(Borrow loan) {
		this.borrow = borrow;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public boolean isUserHasLiked() {
		return userHasLiked;
	}

	public void setUserHasLiked(boolean userHasLiked) {
		this.userHasLiked = userHasLiked;
	}

	public boolean bookHasLikes() {
		return !likes.isEmpty();
	}

	public Set<Like> getLikes() {
		return likes;
	}

	public Set<User> getLikedUsers() {
		return likedUsers;
	}

	public void setLikedUsers(Set<User> likedUsers) {
		this.likedUsers = likedUsers;
	}
}
