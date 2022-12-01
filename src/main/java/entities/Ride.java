package entities;

import dtos.Waypoint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ride")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id", nullable = false)
    private Integer id;

    /*
    @Column(name = "driver", nullable = false, length = 45)
    */
    @NotNull
    @ManyToOne
    @JoinTable(name = "users",
            joinColumns = @JoinColumn(name = "driver_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private User driver;

    @NotNull
    @Column(name = "seats", nullable = false, length = 45)
    private int seats;

    //@Size(max = 45)
    @NotNull
    @Column(name = "origin", nullable = false, length = 45)
    private String origin;

    //@Size(max = 45)
    @NotNull
    @Column(name = "destination", nullable = false, length = 45)
    private String destination;

    @Size(max = 45)
    @NotNull
    @Column(name = "arrival_time", nullable = false, length = 45)
    private long arrivalTime;

    @ManyToMany
    @JoinTable(name = "passengers",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> passengers = new LinkedHashSet<>();

    public Ride() {
    }

    public Ride(Waypoint origin, Waypoint destination, long arrival, int seats, User driver) {
        this.origin = origin.getLng()+","+origin.getLat();
        this.destination = destination.getLng()+","+destination.getLat();
        this.arrivalTime = arrival;
        this.seats = seats;
        this.driver = driver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
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

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Set<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<User> users) {
        this.passengers = users;
    }
}
