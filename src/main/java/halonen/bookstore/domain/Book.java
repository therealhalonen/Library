package halonen.bookstore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name= "title")
	private String title;
	@Column(name= "author")
	private String author;
	
	@Column(name= "publicationYear")
	@Min(value = 1000, message="min value is 1000")
	@Max(value = 2023, message="cant be in the future!")
	private int publicationYear;
	@Column(name= "isbn")
	private String isbn;
	@Column(name= "price")
	private double price;

	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryid")
    private Category category;

	public Book() {
	}

	public Book(String title, String author, int publicationYear, String isbn, double price, Category category) {
		super();
		this.title = title;
		this.author = author;
		this.publicationYear = publicationYear;
		this.isbn = isbn;
		this.price = price;
		this.category = category;
	}

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

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", author=" + author + ", publicationYear=" + publicationYear
				+ ", isbn=" + isbn + ", price=" + price + ", category=" + category + "]";
	}

}