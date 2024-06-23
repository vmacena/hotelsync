package br.edu.ifsp.hotelsync.application.controller.managementControllers;

import br.edu.ifsp.hotelsync.application.controller.ProductController;
import br.edu.ifsp.hotelsync.application.controller.ReservationController;
import br.edu.ifsp.hotelsync.application.main.Main;
import br.edu.ifsp.hotelsync.application.util.*;
import br.edu.ifsp.hotelsync.application.view.Home;
import br.edu.ifsp.hotelsync.domain.entities.product.Product;
import br.edu.ifsp.hotelsync.domain.entities.reservation.Payment;
import br.edu.ifsp.hotelsync.domain.entities.reservation.Reservation;
import br.edu.ifsp.hotelsync.domain.entities.reservation.ReservationStatus;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.update.interfaces.CheckInUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.reservation.update.interfaces.CheckOutUseCase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static br.edu.ifsp.hotelsync.application.main.Main.*;

public class ReservationManagementController {
    @FXML
    private Button btnGuest;

    @FXML
    private Button btnProduct;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnReservation;

    @FXML
    private Button btnRoom;

    @FXML
    private Button btnSignout;

    @FXML
    private Button checkInBtn;

    @FXML
    private Button checkOutBtn;

    @FXML
    private Button createReservationButton;

    @FXML
    private Pane pnlOverview;

    @FXML
    private TextField searchReservation;

    @FXML
    private TableColumn<Reservation, String> startDateReservationField;

    @FXML
    private TableColumn<Reservation, String> checkInReservationField;

    @FXML
    private TableColumn<Reservation, String> endDateReservationField;

    @FXML
    private TableColumn<Reservation, String> checkOutReservationField;

    @FXML
    private TableColumn<Reservation, String> ownerReservationField;

    @FXML
    private TableColumn<Reservation, String> roomReservationField;

    @FXML
    private TableColumn<Reservation, String> statusReservationField;

    @FXML
    private TableColumn<Reservation, String> paymentMethodReservationField;

    @FXML
    private TableView<Reservation> tableReservation;

    private ObservableList<Reservation> tableData;

    @FXML
    public void initialize() {
        bindTableViewToItemsList();
        bindColumnsToValuesSources();
        populateTable();
    }

    private void bindTableViewToItemsList() {
        tableData = FXCollections.observableArrayList();
        tableReservation.setItems(tableData);
    }

    private void bindColumnsToValuesSources() {
        startDateReservationField.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        checkInReservationField.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        endDateReservationField.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        checkOutReservationField.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        ownerReservationField.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getOwner() != null ?
                                String.format("#%s - %s", cell.getValue().getOwner().getId(), cell.getValue().getOwner().getName()) : null));
        roomReservationField.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getRoom() != null ?
                                String.format("#%s - %s", cell.getValue().getRoom().getId(), cell.getValue().getRoom().getNumber()) : null));

        statusReservationField.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getReservationStatus().name()));
        paymentMethodReservationField.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getPayment() != null ?
                                cell.getValue().getPayment().toString() : null));
    }


    private void showProductInMode(UIMode mode) throws IOException {
        Reservation selectedItem = tableReservation.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            navHandler.navigateToReservationPage();
            ReservationController controller = (ReservationController) Home.getController();
            controller.setEntity(selectedItem, mode);
        }
    }

    public void populateTable() {
        Map<Long, Reservation> reservations = findAllReservationUseCase.findAll();
        tableData.clear();
        tableData.addAll(reservations.values());
    }

    private final NavigationHandler navHandler =
            new NavigationHandler();

    @FXML
    void handleExit(ActionEvent event) {
        new ExitHandler().handleExit(event);
    }

    @FXML
    void handleGuestPage(ActionEvent event) throws IOException{
        navHandler.navigateToGuestManagementPage();
    }

    @FXML
    void handleProductPage(ActionEvent actionEvent) throws IOException {
        navHandler.navigateToProductManagementPage();
    }

    @FXML
    void handleReportPage(ActionEvent event) throws IOException {
        navHandler.navigateToReportPage();
    }

    @FXML
    void handleReservationPage(ActionEvent event) throws IOException {
        navHandler.navigateToReservationManagementPage();
    }

    @FXML
    void handleRoomPage(ActionEvent event) throws IOException {
        navHandler.navigateToRoomManagementPage();
    }

    @FXML
    void handleCreateReservation(ActionEvent event) throws IOException {
        navHandler.navigateToReservationPage();
    }

    public void handleUpdateReservation(ActionEvent actionEvent) throws IOException {
        showProductInMode(UIMode.UPDATE);
    }

    @FXML
    public void handleCheckIn() {
        Reservation selectedItem = tableReservation.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                checkInUseCase.doCheckIn(new CheckInUseCase.RequestModel(selectedItem.getId()));
                populateTable();
            } catch (IllegalStateException e) {
                AlertHelper.showErrorAlert(
                        "Error Dialog",
                        "Check-in Error",
                        e.getMessage());
            }
        }
    }

    @FXML
    public void handleCheckOut() {
        Reservation selectedItem = tableReservation.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            CheckOutDialog dialog = new CheckOutDialog();
            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(paymentAndDate -> {
                try {
                    CheckOutUseCase.RequestModel requestModel =
                            new CheckOutUseCase.RequestModel(
                                    selectedItem.getId(),
                                    Payment.
                                            fromDescription(paymentAndDate.
                                                    getKey()));

                   checkOutUseCase.doCheckOut(requestModel);

                    populateTable();
                } catch (IllegalStateException e) {
                    AlertHelper.showErrorAlert("Error Dialog", "Check-out Error", e.getMessage());
                }
            });
        }
    }
}

