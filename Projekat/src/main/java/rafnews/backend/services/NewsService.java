package rafnews.backend.services;

import java.util.List;

import javax.inject.Inject;

import rafnews.backend.model.Comment;
import rafnews.backend.model.Keyword;
import rafnews.backend.model.News;
import rafnews.backend.repositories.news.INewsRepository;

public class NewsService {
	@Inject
	private INewsRepository newsRepository;

	public News addNews(News news) {
		return this.newsRepository.addNews(news);
	}

	public News updateNews(News news) {
		return this.newsRepository.updateNews(news);
	}

	public List<News> allNews(int page, int perPage) {
		return this.newsRepository.allNews(page, perPage);
	}

	public List<News> allNewsByVisits(int page, int perPage) {
		return this.newsRepository.allNewsByVisits(page, perPage);
	}

	public News findNews(Integer id) {
		return this.newsRepository.findNews(id);
	}

	public void deleteNews(Integer id) {
		this.newsRepository.deleteNews(id);
	}

	public List<News> allByCategory(String name, int page, int perPage) {
		return this.newsRepository.allByCategory(name, page, perPage);
	}

	public List<News> allByTag(Integer id, int page, int perPage) {
		return this.newsRepository.allByKeyword(id, page, perPage);
	}

	public List<Keyword> allTagByNews(Integer id) {
		return this.newsRepository.allKeywordByNews(id);
	}

	public List<Comment> allCommentsByNews(Integer id) {
		return this.newsRepository.allCommentsByNews(id);
	}

	public void dislikeNews(Integer id, String id2) {
		this.newsRepository.dislikeNews(id, id2);
		
	}

	public void likeNews(Integer id, String id2) {
		this.newsRepository.likeNews(id, id2);
	}

	public Integer reactions(Integer id) {
		return this.newsRepository.reactions(id);
	}

	public Integer karma(Integer id) {
		return this.newsRepository.karma(id);
	}


}
