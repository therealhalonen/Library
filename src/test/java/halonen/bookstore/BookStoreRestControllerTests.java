package halonen.bookstore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class BookStoreRestControllerTests {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @BeforeEach
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testCreateCategory() throws Exception {
        // Create a new category
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/categories")
                .content("{\"name\": \"Test Category\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testGetAllBooks() throws Exception {
        // Test that fetching all books returns a successful response
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testAddAndUpdateBook() throws Exception {
        // Create a new book with a valid categoryId
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
            .content("{\"title\": \"Test Book\","
                + "\"author\": \"Test Author\","
                + "\"publicationYear\": 2022,"
                + "\"isbn\": \"1234567890123\","
                + "\"price\": 150.0,"
                + "\"category\": {\"categoryid\": 1}}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated())  // Expect a successful creation (status 201)
            .andReturn();

        // Extract the book ID from the JSON response
        String responseJson = result.getResponse().getContentAsString();
        int bookId = JsonPath.read(responseJson, "$.id");

        // Update the book using the retrieved ID
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/{id}", bookId)
            .content("{\"title\": \"Updated Book Title\","
                + "\"author\": \"Updated Author\","
                + "\"publicationYear\": 2023,"
                + "\"isbn\": \"9876543210987\","
                + "\"price\": 200.0,"
                + "\"category\": {\"categoryid\": 2}}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Book Title"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("Updated Author"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.publicationYear").value(2023))
            .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("9876543210987"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(200.0))
            .andExpect(MockMvcResultMatchers.jsonPath("$.category.categoryid").value(2));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testDeleteBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
