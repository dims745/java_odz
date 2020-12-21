package Models;

import java.sql.Time;
import java.util.Date;

public class Task {
    public Task(String name, String comment, Time time) {
        setName(name);
        setComment(comment);
        setTime(time);
    }

    public Task(int id, String name, String comment, Time time) {
        setId(id);
        setName(name);
        setComment(comment);
        setTime(time);
    }

    protected int id;

    protected String name;

    protected String comment;

    protected Time time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
