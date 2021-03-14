package model;

import java.util.UUID;

public class Request {

    private UUID id;
    private Action action;
    private int chosen;

    public Request() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

	public int getChosen() {
		return chosen;
	}

	public void setChosen(int chosen) {
		this.chosen = chosen;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", action=" + action + ", chosen=" + chosen + "]";
	}
    
    
}
