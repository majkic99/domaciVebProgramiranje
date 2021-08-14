package rafnews.backend.repositories.comment;

import java.util.List;

import rafnews.backend.model.Comment;

public interface ICommentRepository {

    public Comment addComment(Comment comment);
    public List<Comment> allComments(int page, int perPage);
    public Comment findComment(Integer id);
    public void deleteComment(Integer id);
	public void dislikeComment(Integer id, String id2);
	public void likeComment(Integer id, String id2);
	public Integer karma(Integer id);
	public Integer reactions(Integer id);
}
