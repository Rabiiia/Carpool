package dtos;

import entities.Ride;

import java.util.List;
import java.util.stream.Collectors;

public class RideDTO {
    private int id;
    private String origin;
    private String destination;
    private long arrival;
    private int seats;
    private UserDTO driver;
    private List<UserDTO> passengers;

    public RideDTO(Ride ride) {
        if (ride.getId() != null) id = ride.getId();
        origin = ride.getOrigin();
        destination = ride.getDestination();
        arrival = ride.getArrivalTime();
        seats = ride.getSeats();
        driver = new UserDTO(ride.getDriver());
        passengers = ride.getPassengers().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    public RideDTO(String origin, String destination, long arrival, int seats, UserDTO driver) {
        this.origin = origin;
        this.destination = destination;
        this.arrival = arrival;
        this.seats = seats;
        this.driver = driver;
    }

    public int getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public long getArrival() {
        return arrival;
    }

    public int getSeats() {
        return seats;
    }

    public UserDTO getDriver() {
        return driver;
    }

    public List<UserDTO> getPassengers() {
        return passengers;
    }
}
