package model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Reference;
import org.bson.types.ObjectId;

import java.util.List;

@Entity(value = "Clans")
public class Clan {
    @Id
    private ObjectId id;

    @Indexed
    private String name;

    @Indexed
    private String description;

    @Indexed
    private String tag;

    @Reference
    private List<User> members;

    public Clan() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMember(User u) {
        if(u == null) {
            return;
        }

        this.members.add(u);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
