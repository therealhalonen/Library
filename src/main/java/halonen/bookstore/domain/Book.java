package halonen.bookstore.domain;

import halonen.bookstore.service.LoanStatus;
import jakarta.persistence.*;
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
    private Loan loan;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

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

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author + ", publicationYear=" + publicationYear
                + ", isbn=" + isbn + ", price=" + price + ", category=" + category + "]";
    }
}
