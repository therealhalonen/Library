package halonen.bookstore.web;

import halonen.bookstore.domain.*;
import halonen.bookstore.service.LoanStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Login stuffz
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/approval")
    public String approvalPage() {
        return "approval"; // Return the Thymeleaf template or view name for the approval page
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

    @GetMapping(value = {"/", "/booklist"})
    public String bookList(Model model, @RequestParam(value = "sortField", defaultValue = "title") String sortField,
                           @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection) {
        // Get books based on sorting criteria and direction
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        List<Book> books = bookRepository.findAll(sort);

        for (Book book : books) {
            Loan loan = loanRepository.findByBook(book);
            if (loan != null) {
                // If a loan exists, set the status to LOANED
                book.setStatus(LoanStatus.LOANED);
            } else {
                // If no loan exists, set the status to AVAILABLE
                book.setStatus(LoanStatus.AVAILABLE);
            }
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

}