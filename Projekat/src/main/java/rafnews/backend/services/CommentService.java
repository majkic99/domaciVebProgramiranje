package rafnews.backend.services;

import java.util.List;

import javax.inject.Inject;

import rafnews.backend.model.Comment;
import rafnews.backend.repositories.comment.ICommentRepository;

public class CommentService {

	@Inject
	private ICommentRepository commentRepository;

	public Comment addComment(Comment comment) {
		return this.commentRepository.addComment(comment);
	}

	public List<Comment> allComments(int page, int perPage) {
		return this.commentRepository.allComments(page, perPage);
	}

	public Comment findComment(Integer id) {
		return this.commentRepository.findComment(id);
	}

	public void deleteComment(Integer id) {
		this.commentRepository.deleteComment(id);
	}

	public void likeComment(Integer id, String id2) {
		this.commentRepository.likeComment(id, id2);
		
	}

	public void dislikeComment(Integer id, String id2) {
		this.commentRepository.dislikeComment(id, id2);
		
	}

	public Integer karma(Integer id) {
		return this.commentRepository.karma(id);
	}

	public Integer reactions(Integer id) {
		return this.commentRepository.reactions(id);
	}
}
