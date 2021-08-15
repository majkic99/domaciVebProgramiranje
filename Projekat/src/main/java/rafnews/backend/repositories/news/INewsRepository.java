package rafnews.backend.repositories.news;

import java.util.List;

import rafnews.backend.model.Comment;
import rafnews.backend.model.Keyword;
import rafnews.backend.model.News;

public interface INewsRepository {
    public News addNews(News news);
    public News updateNews(News news);
    public List<News> allNews(int page, int perPage);
    public List<News> allNewsByVisits(int page, int perPage);
    public News findNews(Integer id);
    public void deleteNews(Integer id);
    public List<News> allByCategory(String name, int page, int perPage);
    public List<News> allByKeyword(Integer id, int page, int perPage);
    public List<Keyword> allKeywordByNews(Integer id);
    public List<Comment> allCommentsByNews(Integer id);
	public void dislikeNews(Integer id, String id2);
	public void likeNews(Integer id, String id2);
	public Integer reactions(Integer id);
	public Integer karma(Integer id);
	public List<News> advancedNewsSearch(String result, Integer page, Integer perPage);
	public List<News> allNewsByReactionAmount(Integer page, Integer perPage);
}
