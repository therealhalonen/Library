package halonen.bookstore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import halonen.bookstore.web.BookstoreController;

@SpringBootTest
public class BookstoreApplicationTests {

    @Autowired
    private BookstoreController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }	
}
