package rafnews.backend.model;

public class User {

    private Integer id;
    private String email;
    private String name;
    private String surname;
    private Role role;
    private Status status;
    private String password;

    public User() {
    }

	public User(Integer id, String email, String name, String surname, Role role, Status status, String password) {
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.role = role;
		this.status = status;
		this.password = password;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    
}
