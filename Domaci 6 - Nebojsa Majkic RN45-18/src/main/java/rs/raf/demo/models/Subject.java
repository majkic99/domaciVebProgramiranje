package rs.raf.demo.models;

import java.io.Serializable;

public class Subject implements Serializable {
    private String name;
    private String comment;

    public Subject() {
    }

    public Subject( String name, String comment) {
        this.name = name;
        this.comment = comment;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
