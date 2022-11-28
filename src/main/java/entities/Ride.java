package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ride")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id", nullable = false)
    private Integer id;

    @Size(max = 45)
    @NotNull
    @Column(name = "driver", nullable = false, length = 45)
    private String driver;

    @Size(max = 45)
    @NotNull
    @Column(name = "seats", nullable = false, length = 45)
    private String seats;

    @Size(max = 45)
    @NotNull
    @Column(name = "origin", nullable = false, length = 45)
    private String origin;

    @Size(max = 45)
    @NotNull
    @Column(name = "destination", nullable = false, length = 45)
    private String destination;

    @Size(max = 45)
    @NotNull
    @Column(name = "arrival_time", nullable = false, length = 45)
    private String arrivalTime;

    @ManyToMany
    @JoinTable(name = "passengers",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new LinkedHashSet<>();

    public Ride() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}