package halonen.bookstore.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, Long> {
	List<Like> findByUser_UsernameAndBook_Id(String username, Long bookId);

	void deleteByUserAndBook(User currentUser, Book book);

}