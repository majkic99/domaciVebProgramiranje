package rafnews.backend.model;

import java.util.Date;
import java.util.List;

public class News {
	private Integer id;
	private String title;
	private String text;
	private Date creationTime;
	private Integer visitNumber;
	private User creator;
	private List<Comment> comments;
	private List<Keyword> keywords;
	private Category category;
	
	
	
	public News() {
		super();
	}
	public News(Integer id, String title, String text, Date creationTime, Integer visitNumber, User creator,
			List<Comment> comments, List<Keyword> keywords, Category category) {
		super();
		this.id = id;
		this.title = title;
		this.text = text;
		this.creationTime = creationTime;
		this.visitNumber = visitNumber;
		this.creator = creator;
		this.comments = comments;
		this.keywords = keywords;
		this.category = category;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Integer getVisitNumber() {
		return visitNumber;
	}
	public void setVisitNumber(Integer visitNumber) {
		this.visitNumber = visitNumber;
	}
	
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<Keyword> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	
}
