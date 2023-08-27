package halonen.Bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import halonen.bookstore.domain.Book;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookstoreController {

    private List<Book> bookList = new ArrayList<>();

    @GetMapping("/index")
    public String getIndexPage(Model model) {
        model.addAttribute("newBook", new Book());
        model.addAttribute("bookList", bookList);
        return "index";
    }

    @PostMapping("/index")
    public String addBook(Book newBook) {
        bookList.add(newBook);
        return "redirect:/index";
    }
}