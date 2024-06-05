package br.edu.ifsp.hotelsync.domain.entities.reservation;

import br.edu.ifsp.hotelsync.domain.entities.guest.Guest;
import br.edu.ifsp.hotelsync.domain.entities.room.Room;
import br.edu.ifsp.hotelsync.domain.usecases.utils.Notification;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Reservation {
    private Long id;
    private final LocalDate startDate;
    private LocalDate checkInDate;
    private final LocalDate endDate;
    private LocalDate checkOutDate;
    private final Guest owner;
    private Room room;
    private ReservationStatus reservationStatus = ReservationStatus.RESERVED;
    private List<Guest> guests = new ArrayList<>();
    private List<ConsumedProduct> consumedProducts = new ArrayList<>();
    private Payment payment;

    public static Reservation createCompleteReservation(Long id, LocalDate startDate, LocalDate checkInDate, LocalDate endDate,
                                                 LocalDate checkOutDate, Guest owner,
                                                 Room room, ReservationStatus reservationStatus,
                                                 List<Guest> guests, List<ConsumedProduct> consumedProducts,
                                                 Payment payment){
        return new Reservation(id, startDate, checkInDate, endDate, checkOutDate, owner, room,
                reservationStatus, guests, consumedProducts, payment);
    }

    public static Reservation createReservation(LocalDate startDate, LocalDate endDate,
                                         Guest owner, Room room, Payment payment){
        return new Reservation(startDate, endDate, owner, room, payment);
    }

    private Reservation(Long id,
                       LocalDate startDate,
                       LocalDate checkInDate,
                       LocalDate endDate,
                       LocalDate checkOutDate,
                       Guest owner,
                       Room room,
                       ReservationStatus reservationStatus,
                       List<Guest> guests,
                       List<ConsumedProduct> consumedProducts,
                       Payment payment) {
        this.id = id;
        this.startDate = startDate;
        this.checkInDate = checkInDate;
        this.endDate = endDate;
        this.checkOutDate = checkOutDate;
        this.owner = owner;
        this.room = room;
        this.reservationStatus = reservationStatus;
        this.guests = guests;
        this.consumedProducts = consumedProducts;
        this.payment = payment;
        validate();
    }

    private Reservation(LocalDate startDate,
                       LocalDate endDate,
                       Guest owner,
                       Room room,
                       Payment payment) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.owner = owner;
        this.room = room;
        this.payment = payment;
        validate();
    }

    public void checkIn(){
        checkInDate = LocalDate.now();
        room.turnOccupied();
        reservationStatus = ReservationStatus.ACTIVE;
    }

    public void checkOut(String paymentMethod){
        checkOutDate = LocalDate.now();
        room.turnAvailable();
        payment = new Payment(calculateTotalToPay(), checkOutDate, paymentMethod);
        reservationStatus = ReservationStatus.FINISHED;
    }

    public void cancelReservation(){
        room.turnAvailable();
        reservationStatus = ReservationStatus.CANCELLED;
    }

    private void validate() {
        ReservationInputRequestValidator validator = new ReservationInputRequestValidator();
        Notification notification = validator.validate(this);

        if (notification.hasErrors())
            throw new IllegalArgumentException(notification.getErrorMessage());
    }

    private double calculateTotalToPay() {
        double productTotalCost = consumedProducts
                .stream()
                .mapToDouble(consumedProduct ->
                        consumedProduct.getProduct().getPrice() * consumedProduct.getQuantity())
                .sum();
        final double roomValue = room.getRoomCategory().getBasePrice();
        final int totalDays = (int) ChronoUnit.DAYS.between(this.startDate, this.endDate);
        return (roomValue * totalDays) + productTotalCost;
    }

    public void addGuest(Guest guest){
        if(!guests.contains(guest))
            guests.add(guest);
    }

    public void removeGuest(Guest guest){
        guests.remove(guest);
    }

    public void addProduct(ConsumedProduct product){
        consumedProducts.add(product);
    }

    public void removeProduct(ConsumedProduct product){
        if(consumedProducts.contains(product))
            consumedProducts.remove(product);
        else throw new NoSuchElementException(
                "Product of id " + product.getProduct().getId() + " not found in reservation"
        );
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Guest getOwner() {
        return owner;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public List<Guest> getGuests() {
        return new ArrayList<>(guests);
    }

    public List<ConsumedProduct> getConsumedProducts() {
        return new ArrayList<>(consumedProducts);
    }
}
