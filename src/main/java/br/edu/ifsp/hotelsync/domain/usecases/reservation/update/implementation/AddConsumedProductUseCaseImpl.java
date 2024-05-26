package br.edu.ifsp.hotelsync.domain.usecases.reservation.update.implementation;

import br.edu.ifsp.hotelsync.domain.entities.product.Product;
import br.edu.ifsp.hotelsync.domain.entities.reservation.ConsumedProduct;
import br.edu.ifsp.hotelsync.domain.entities.reservation.Reservation;
import br.edu.ifsp.hotelsync.domain.persistence.dao.ProductDAO;
import br.edu.ifsp.hotelsync.domain.persistence.dao.ReservationDAO;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.update.interfaces.AddConsumedProductUseCase;

import java.util.NoSuchElementException;

public class AddConsumedProductUseCaseImpl implements AddConsumedProductUseCase {

    private final ProductDAO productRepository;
    private final ReservationDAO reservationRepository;

    public AddConsumedProductUseCaseImpl(ProductDAO productRepository, ReservationDAO reservationRepository) {
        this.productRepository = productRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void addConsumedProduct(RequestModel request) {
        Long idReservation = request.idReservation();
        Reservation reservation = reservationRepository.findOneByKey(idReservation).orElseThrow(
                () -> new NoSuchElementException("Reservation of id " + idReservation + " not found")
        );

        Long idProduct = request.idProduct();
        Product product = productRepository.findOneByKey(idProduct).orElseThrow(
                () -> new NoSuchElementException("Product of id " + idProduct + " not found")
        );

        ConsumedProduct consumedProduct = new ConsumedProduct(product, request.quantity());
        reservation.addProduct(consumedProduct);
        reservationRepository.update(reservation);
    }
}
