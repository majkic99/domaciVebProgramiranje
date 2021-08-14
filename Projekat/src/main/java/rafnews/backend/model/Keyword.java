package rafnews.backend.model;

public class Keyword {
	private Integer id;
	private String name;
	public Keyword(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public Keyword() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
