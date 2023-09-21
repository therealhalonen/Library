package halonen.bookstore;

import halonen.bookstore.domain.Category;
import halonen.bookstore.domain.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testSaveAndFindById() {
        // Create a Category entity and save it to the repository
        Category category = new Category("Test Category");
        categoryRepository.save(category);

        // Retrieve the saved category by ID
        Category foundCategory = categoryRepository.findById(category.getCategoryid()).orElse(null);

        // Assert that the retrieved category matches the saved category
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("Test Category");
    }

    @Test
    public void testFindAll() {
        // Save a few categories to the repository
        Category category1 = new Category("Category 1");
        Category category2 = new Category("Category 2");
        categoryRepository.save(category1);
        categoryRepository.save(category2);

        // Retrieve all categories from the repository
        Iterable<Category> allCategories = categoryRepository.findAll();

        // Assert that the list of categories is not empty and contains the saved categories
        assertThat(allCategories).isNotEmpty();
        assertThat(allCategories).contains(category1, category2);
    }

    @Test
    public void testUpdate() {
        // Create and save a category to the repository
        Category category = new Category("Category to Update");
        categoryRepository.save(category);

        // Modify the name of the category
        category.setName("Updated Category Name");
        categoryRepository.save(category);

        // Retrieve the updated category from the repository
        Category updatedCategory = categoryRepository.findById(category.getCategoryid()).orElse(null);

        // Assert that the retrieved category's name is updated
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo("Updated Category Name");
    }

    @Test
    public void testDelete() {
        // Create and save a category to the repository
        Category category = new Category("Category to Delete");
        categoryRepository.save(category);

        // Delete the category from the repository
        categoryRepository.delete(category);

        // Try to retrieve the deleted category by ID
        Category deletedCategory = categoryRepository.findById(category.getCategoryid()).orElse(null);

        // Assert that the deleted category is null (not found)
        assertThat(deletedCategory).isNull();
    }
}
