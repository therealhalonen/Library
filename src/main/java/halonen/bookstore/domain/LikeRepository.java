package halonen.bookstore.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LikeRepository extends CrudRepository<BookLike, Long> {
    List<BookLike> findByUser_UsernameAndBook_Id(String username, Long bookId);

    void deleteByUserAndBook(User currentUser, Book book);

}