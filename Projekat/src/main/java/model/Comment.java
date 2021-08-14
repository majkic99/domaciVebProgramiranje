package model;

import java.util.Date;

public class Comment {
	private Integer id;
	private String creator;
	private String text;
	private Date creationDate;
	private News news;
	
	
	
	public Comment() {
		super();
	}
	public Comment(Integer id, String creator, String text, Date creationDate, News news) {
		super();
		this.id = id;
		this.creator = creator;
		this.text = text;
		this.creationDate = creationDate;
		this.news = news;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public News getNews() {
		return news;
	}
	public void setNews(News news) {
		this.news = news;
	}
	
	
}
