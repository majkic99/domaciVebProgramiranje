package model;

import java.util.UUID;

public class Player {

    private UUID id;

    public Player(UUID id) {
        this.id = id;
    }

	public UUID getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + "]";
	}

   
}
