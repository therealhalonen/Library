package halonen.bookstore.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	@GetMapping(ERROR_PATH)
	public String handleError(HttpServletRequest request, Model model) {
		// Get the error status code
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());

			if (statusCode == 404) {
				// Handle 404 (Not Found) error
				model.addAttribute("errorMessage", "The requested page was not found.");
				return "error";
			} else if (statusCode == 401) {
				// Handle 401 (Unauthorized) error
				model.addAttribute("errorMessage", "You are not authorized to access this resource.");
				return "error";
			} else if (statusCode == 403) {
				// Handle 403 (Forbidden) error
				model.addAttribute("errorMessage", "Access to this resource is forbidden.");
				return "error";
			}
		}

		// Default error message for other errors
		model.addAttribute("errorMessage", "An error occurred.");
		return "error"; // Return the error template
	}
}