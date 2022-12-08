package entities;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQuery(name = "Users.deleteAllRows", query = "DELETE FROM User")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "address", nullable = false, length = 45)
    private String address;

    @Size(max = 45)
    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "phone", nullable = false)
    private Integer phone;

    @NotNull
    @Lob
    @Column(name = "role", nullable = false)
    private String role;

    @Size(max = 45)
    @NotNull
    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @NotNull
    @Column(name = "zipcode", nullable = false)
    private Integer zipcode;

    @OneToMany(mappedBy = "user")
    private Set<Request> requests = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "passengers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "ride_id"))
    private Set<Ride> ridesJoined = new LinkedHashSet<>();

    @OneToMany(mappedBy = "driver")
    private Set<Ride> ridesCreated = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;



    public User() {
    }

    public User(int id) {
        this.id = id;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = BCrypt.hashpw(password,BCrypt.gensalt());
    }

    public User(String username, String password, String name, Integer phone, String address, Integer zipcode) {
        this.username = username;
        this.password = BCrypt.hashpw(password,BCrypt.gensalt());
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.zipcode = zipcode;
        role = "user";
    }

    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, password);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<Ride> getRidesJoined() {
        return ridesJoined;
    }

    public void setRidesJoined(Set<Ride> ridesJoined) {
        this.ridesJoined = ridesJoined;
    }

    public Set<Ride> getRidesCreated() {
        return ridesCreated;
    }

    public void setRidesCreated(Set<Ride> ridesCreated) {
        this.ridesCreated = ridesCreated;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}