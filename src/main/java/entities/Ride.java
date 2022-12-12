package entities;

import dtos.Waypoint;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "rides")
@NamedQuery(name = "Rides.deleteAllRows", query = "DELETE FROM Ride ")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Size(max = 45)
    @NotNull
    @Column(name = "destination", nullable = false, length = 45)
    private String destination;

    @Size(max = 45)
    @NotNull
    @Column(name = "origin", nullable = false, length = 45)
    private String origin;

    @NotNull
    @Column(name = "arrival_time", nullable = false)
    private Long arrivalTime;

    @NotNull
    @Column(name = "seats", nullable = false)
    private Byte seats;

    @OneToMany(mappedBy = "ride")
    private Set<Request> requests = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "passengers",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> passengers = new LinkedHashSet<>();


    public Ride() {
    }

    public Ride(int id) {
        this.id = id;
    }

    public Ride(String origin, String destination, long arrival, byte seats, User driver) {
        this.origin = origin;
        this.destination = destination;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Byte getSeats() {
        return seats;
    }

    public void setSeats(Byte seats) {
        this.seats = seats;
    }

    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    public Set<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<User> users) {
        this.passengers = users;
    }



}