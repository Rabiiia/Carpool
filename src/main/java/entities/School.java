package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQuery(name = "School.deleteAllRows", query = "DELETE FROM School")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Size(max = 45)
    @NotNull
    @Column(name = "location", nullable = false, length = 45)
    private String location;

    @OneToMany(mappedBy = "school")
    private Set<User> users = new LinkedHashSet<>();

    public School() {
    }

    public School(String name, String location) {
        this.name = name;
        this.location = location;
    }


    // Syncronizing with user object
    public void addUser(User user) {
        if (user != null) {
            this.users.add(user);
            user.setSchool(this);
        } else {
            this.users = null;
        }
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

//    public Set<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<User> users) {
//        this.users = users;
//    }

}