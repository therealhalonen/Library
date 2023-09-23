package halonen.bookstore.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import halonen.bookstore.domain.BookRepository;
import halonen.bookstore.domain.Category;
import halonen.bookstore.domain.CategoryRepository;
import halonen.bookstore.domain.LikeRepository;
import halonen.bookstore.domain.LoanRepository;
import halonen.bookstore.domain.UserRepository;
import halonen.bookstore.service.LikeService;

@Controller
public class CategoryController {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private LikeRepository likeRepository;
	@Autowired
	private LikeService likeService;

	// Create Category
	@GetMapping(value = "/addcategory")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		model.addAttribute("categories", categoryRepository.findAll());
		return "addcategory";
	}

	// Delete Category
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "/deletecategory/{id}")
	public String deleteCategory(@PathVariable("id") Long categoryId) {
		categoryRepository.deleteById(categoryId);
		return "redirect:/addcategory";
	}

	// Save Category Simple
	@PostMapping(value = "/savecategorysimple")
	public String saveCategorySimple(Category category) {
		categoryRepository.save(category);
		return "redirect:addcategory";
	}

	// Save Category using AJAX
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
