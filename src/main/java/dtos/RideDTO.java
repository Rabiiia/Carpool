package dtos;

import entities.Ride;

import java.util.List;
import java.util.Objects;
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
        id = ride.getId();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RideDTO rideDTO = (RideDTO) o;
        return id == rideDTO.id && arrival == rideDTO.arrival && seats == rideDTO.seats && origin.equals(rideDTO.origin) && destination.equals(rideDTO.destination) && driver.equals(rideDTO.driver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, destination, arrival, seats, driver);
    }
}
