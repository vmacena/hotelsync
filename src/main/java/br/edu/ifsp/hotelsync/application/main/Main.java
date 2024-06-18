package br.edu.ifsp.hotelsync.application.main;

import br.edu.ifsp.hotelsync.application.repository.inmemory.InMemoryGuestDao;
import br.edu.ifsp.hotelsync.application.repository.inmemory.InMemoryProductDao;
import br.edu.ifsp.hotelsync.application.repository.inmemory.InMemoryReservationDao;
import br.edu.ifsp.hotelsync.application.repository.inmemory.InMemoryRoomDao;
import br.edu.ifsp.hotelsync.application.repository.sqlite.DatabaseBuilder;
import br.edu.ifsp.hotelsync.application.view.HelloApplication;
import br.edu.ifsp.hotelsync.domain.persistence.dao.GuestDao;
import br.edu.ifsp.hotelsync.domain.persistence.dao.ProductDao;
import br.edu.ifsp.hotelsync.domain.persistence.dao.ReservationDao;
import br.edu.ifsp.hotelsync.domain.persistence.dao.RoomDao;
import br.edu.ifsp.hotelsync.domain.usecases.guest.create.CreateGuestUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.guest.create.CreateGuestUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.guest.find.FindAllGuestUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.guest.find.FindAllGuestUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.guest.find.FindOneGuestUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.guest.find.FindOneGuestUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.guest.update.UpdateGuestUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.guest.update.UpdateGuestUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.product.create.CreateProductUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.product.create.CreateProductUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.product.find.FindAllProductUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.product.find.FindAllProductUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.product.find.FindOneProductUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.product.find.FindOneProductUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.product.update.UpdateProductUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.product.update.UpdateProductUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.reports.create.*;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.create.CreateReservationUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.create.CreateReservationUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.find.FindAllReservationUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.find.FindAllReservationUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.find.FindOneReservationUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.find.FindOneReservationUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.update.implementation.*;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.update.interfaces.*;
import br.edu.ifsp.hotelsync.domain.usecases.room.create.CreateRoomUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.room.create.CreateRoomUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.room.find.FindAllRoomUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.room.find.FindAllRoomUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.room.find.FindOneRoomUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.room.find.FindOneRoomUseCaseImpl;
import br.edu.ifsp.hotelsync.domain.usecases.room.update.UpdateRoomUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.room.update.UpdateRoomUseCaseImpl;

public class Main {
    public static CreateGuestUseCase createGuestUseCase;
    public static FindAllGuestUseCase findAllGuestUseCase;
    public static FindOneGuestUseCase findOneGuestUseCase;
    public static UpdateGuestUseCase updateGuestUseCase;

    public static CreateRoomUseCase createRoomUseCase;
    public static FindAllRoomUseCase findAllRoomUseCase;
    public static FindOneRoomUseCase findOneRoomUseCase;
    public static UpdateRoomUseCase updateRoomUseCase;

    public static CreateProductUseCase createProductUseCase;
    public static FindAllProductUseCase findAllProductUseCase;
    public static FindOneProductUseCase findOneProductUseCase;
    public static UpdateProductUseCase updateProductUseCase;

    public static CreateReservationUseCase createReservationUseCase;
    public static FindAllReservationUseCase findAllReservationUseCase;
    public static FindOneReservationUseCase findOneReservationUseCase;
    public static AddConsumedProductUseCase addConsumedProductUseCase;
    public static AddGuestUseCase addGuestUseCase;
    public static CancelReservationUseCase cancelReservationUseCase;
    public static CheckInUseCase checkInUseCase;
    public static CheckOutUseCase checkOutUseCase;
    public static RemoveConsumedProductUseCase removeConsumedProductUseCase;

    public static CreateReportUseCase createCheckInReport;
    public static CreateReportUseCase createCheckOutReport;
    public static CreateReportUseCase createDailyOccupationReport;
    public static CreateReportUseCase createFinancialReport;

    public static void main(String[] args) {
        configureInjection();
        setupDatabase();
        HelloApplication.main(args);
    }

    private static void setupDatabase() {
        DatabaseBuilder dbBuilder = new DatabaseBuilder();
        dbBuilder.buildDatabaseIfMissing();
    }

    private static void configureInjection() {
        GuestDao guestDao = new InMemoryGuestDao();

        createGuestUseCase = new CreateGuestUseCaseImpl(guestDao);
        findAllGuestUseCase = new FindAllGuestUseCaseImpl(guestDao);
        findOneGuestUseCase = new FindOneGuestUseCaseImpl(guestDao);
        updateGuestUseCase = new UpdateGuestUseCaseImpl(guestDao);

        RoomDao roomDao = new InMemoryRoomDao();
        createRoomUseCase = new CreateRoomUseCaseImpl(roomDao);
        findAllRoomUseCase = new FindAllRoomUseCaseImpl(roomDao);
        findOneRoomUseCase = new FindOneRoomUseCaseImpl(roomDao);
        updateRoomUseCase = new UpdateRoomUseCaseImpl(roomDao);

        ProductDao productDao = new InMemoryProductDao();
        createProductUseCase = new CreateProductUseCaseImpl(productDao);
        findAllProductUseCase = new FindAllProductUseCaseImpl(productDao);
        findOneProductUseCase = new FindOneProductUseCaseImpl(productDao);
        updateProductUseCase = new UpdateProductUseCaseImpl(productDao);

        ReservationDao reservationDao = new InMemoryReservationDao();
        createReservationUseCase = new CreateReservationUseCaseImpl(reservationDao);
        findAllReservationUseCase = new FindAllReservationUseCaseImpl(reservationDao);
        findOneReservationUseCase = new FindOneReservationUseCaseImpl(reservationDao);
        addConsumedProductUseCase = new AddConsumedProductUseCaseImpl(productDao, reservationDao);
        addGuestUseCase = new AddGuestUseCaseImpl(guestDao, reservationDao);
        cancelReservationUseCase = new CancelReservationUseCaseImpl(reservationDao);
        checkInUseCase = new CheckInUseCaseImpl(reservationDao, roomDao);
        checkOutUseCase = new CheckOutUseCaseImpl(reservationDao, roomDao);
        removeConsumedProductUseCase = new RemoveConsumedProductUseCaseImpl(productDao, reservationDao);

        createCheckInReport = new CreateCheckInReportUseCase(reservationDao);
        createCheckOutReport = new CreateCheckOutReportUseCase(reservationDao);
        createDailyOccupationReport = new CreateDailyOccupationReportUseCase(roomDao, reservationDao);
        createFinancialReport = new CreateFinancialReportUseCase(reservationDao);
    }
}