package halonen.bookstore.web;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error(Model model) {
        // Add custom error messages or data to the model
        model.addAttribute("errorMessage", "An error occurred.");
        
        return "error";
    }

    public String getErrorPath() {
        return PATH;
    }
}