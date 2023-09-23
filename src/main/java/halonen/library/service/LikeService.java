package halonen.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import halonen.library.domain.Book;
import halonen.library.domain.Like;
import halonen.library.domain.LikeRepository;
import halonen.library.domain.User;
import jakarta.transaction.Transactional;

@Service
public class LikeService {
	private final LikeRepository likeRepository;

	@Autowired
	public LikeService(LikeRepository likeRepository) {
		this.likeRepository = likeRepository;
	}

	@Transactional
	public boolean userHasLikedBook(Book book, String username) {
		List<Like> likes = likeRepository.findByUser_UsernameAndBook_Id(username, book.getId());
		return !likes.isEmpty();
	}

	@Transactional
	public void unlikeBook(User currentUser, Book book) {
		likeRepository.deleteByUserAndBook(currentUser, book);
	}
}
