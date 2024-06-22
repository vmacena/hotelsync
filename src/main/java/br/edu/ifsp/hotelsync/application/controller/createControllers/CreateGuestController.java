package br.edu.ifsp.hotelsync.application.controller.createControllers;

import br.edu.ifsp.hotelsync.application.util.ExitHandler;
import br.edu.ifsp.hotelsync.application.util.NavigationHandler;
import br.edu.ifsp.hotelsync.application.util.UIMode;
import br.edu.ifsp.hotelsync.domain.entities.guest.Guest;
import br.edu.ifsp.hotelsync.domain.entities.guest.State;
import br.edu.ifsp.hotelsync.domain.entities.guest.Phone;
import br.edu.ifsp.hotelsync.domain.entities.guest.Cpf;
import br.edu.ifsp.hotelsync.domain.entities.guest.Address;
import br.edu.ifsp.hotelsync.domain.usecases.guest.create.CreateGuestUseCase;
import br.edu.ifsp.hotelsync.domain.usecases.guest.update.UpdateGuestUseCase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;

import static br.edu.ifsp.hotelsync.application.main.Main.createGuestUseCase;
import static br.edu.ifsp.hotelsync.application.main.Main.updateGuestUseCase;

public class CreateGuestController {

    @FXML
    private DatePicker birthDateGuestPicker;

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
    private Button cancelGuestBtn;

    @FXML
    private TextField roadGuestField;

    @FXML
    private TextField cepGuestField;

    @FXML
    private TextField cityGuestField;

    @FXML
    private TextField complementField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField districtField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private Pane pnlOverview;

    @FXML
    private TextField pronounsGuestField;

    @FXML
    private Label viewTitle;

    @FXML
    private Button saveGuestBtn;

    @FXML
    private ComboBox<State> stateGuestCombo;

    private final ExitHandler exitHandler = new ExitHandler();

    private final NavigationHandler navHandler = new NavigationHandler();

    private Guest guest;

    @FXML
    public void initialize() {
        stateGuestCombo.getItems().setAll(State.values());
    }

    @FXML
    void handleExit(ActionEvent event) {
        exitHandler.handleExit(event);
    }

    @FXML
    void handleGuestPage() throws IOException {
        navHandler.navigateToGuestPage();
    }

    @FXML
    void handleProductPage(ActionEvent event) throws IOException {
        navHandler.navigateToProductPage();
    }

    @FXML
    void handleReportPage(ActionEvent event) throws IOException {
        navHandler.navigateToReportPage();
    }

    @FXML
    void handleReservationPage(ActionEvent event) throws IOException {
        navHandler.navigateToReservationPage();
    }

    @FXML
    void handleRoomPage(ActionEvent event) throws IOException {
        navHandler.navigateToRoomPage();
    }

    @FXML
    void onAddGuestClick(ActionEvent event) {
        saveOrUpdate();
    }

    @FXML
    void onCancelAddGuestClick(ActionEvent event) throws IOException {
        navHandler.navigateToGuestPage();
    }

    public void setEntity(Guest guest, UIMode mode) {
        if (guest == null)
            throw new IllegalArgumentException("Guest can not be null.");

        this.guest = guest;
        setEntityIntoView();

        if (mode == UIMode.VIEW)
            configureViewMode();
    }

    private void setEntityIntoView() {
        if (guest != null) {
            viewTitle.setText("Update Guest");
            nameField.setText(guest.getName());
            pronounsGuestField.setText(guest.getPronouns());
            birthDateGuestPicker.setValue(guest.getBirthdate());
            phoneField.setText(guest.getPhone() != null ? guest.getPhone().toString() : "");
            cpfField.setText(guest.getCpf().toString());
            if(guest.getAddress() != null) {
                roadGuestField.setText(guest.getAddress().getRoad());
                cepGuestField.setText(guest.getAddress().getCep());
                cityGuestField.setText(guest.getAddress().getCity());
                complementField.setText(guest.getAddress().getComplement());
                districtField.setText(guest.getAddress().getDistrict());
                stateGuestCombo.setValue(guest.getAddress().getState());
            }
        }
    }

    private void configureViewMode() {
        nameField.setDisable(true);
        pronounsGuestField.setDisable(true);
        birthDateGuestPicker.setDisable(true);
        phoneField.setDisable(true);
        cpfField.setDisable(true);
        cepGuestField.setDisable(true);
        cityGuestField.setDisable(true);
        complementField.setDisable(true);
        districtField.setDisable(true);
        stateGuestCombo.setDisable(true);
        saveGuestBtn.setDisable(true);
    }

    private void getEntityToView() {
        String name = nameField.getText();
        LocalDate birthdate = birthDateGuestPicker.getValue();
        Cpf cpf = new Cpf(cpfField.getText());

        if (isOwnerInputValid()) {
            String pronouns = pronounsGuestField.getText();
            Phone phone = new Phone(phoneField.getText());
            Address address = new Address(
                    roadGuestField.getText(),
                    cityGuestField.getText(),
                    stateGuestCombo.getValue(),
                    cepGuestField.getText(),
                    districtField.getText(),
                    complementField.getText()
            );

            if (guest == null) {
                guest = Guest.createOwner(name, pronouns, birthdate, phone, cpf, address);
            } else {
                guest.setName(name);
                guest.setPronouns(pronouns);
                guest.setBirthdate(birthdate);
                guest.setPhone(phone);
                guest.setAddress(address);
            }
        } else {
            if (guest == null) {
                guest = Guest.createGuest(name, birthdate, cpf);
            } else {
                guest.setName(name);
                guest.setBirthdate(birthdate);
                guest.setCpf(cpf);
            }
        }
    }

    private boolean isOwnerInputValid() {
        boolean isValid = true;

        if (pronounsGuestField.getText() == null || pronounsGuestField.getText().isEmpty()) isValid = false;
        if (phoneField.getText() == null || phoneField.getText().isEmpty()) isValid = false;
        if (cepGuestField.getText() == null || cepGuestField.getText().isEmpty()) isValid = false;
        if (cityGuestField.getText() == null || cityGuestField.getText().isEmpty()) isValid = false;
        if (districtField.getText() == null || districtField.getText().isEmpty()) isValid = false;
        if (stateGuestCombo.getValue() == null) isValid = false;

        return isValid;
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Fields");
        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void saveOrUpdate() {
        try {
            getEntityToView();
            if (guest == null) return;
            if (guest.getId() == null) {
                if (guest.getPronouns() != null && guest.getPhone() != null && guest.getAddress() != null) {
                    createGuestUseCase.createOwner(new CreateGuestUseCase.OwnerRequestModel(
                            guest.getName(),
                            guest.getPronouns(),
                            guest.getBirthdate(),
                            guest.getPhone().getValue(),
                            guest.getCpf().getValue(),
                            guest.getAddress().getRoad(),
                            guest.getAddress().getCity(),
                            guest.getAddress().getState(),
                            guest.getAddress().getCep(),
                            guest.getAddress().getDistrict(),
                            guest.getAddress().getComplement()));
                } else {
                    createGuestUseCase.createGuest(new CreateGuestUseCase.GuestRequestModel(
                            guest.getName(),
                            guest.getBirthdate(),
                            guest.getCpf().getValue()));
                }
            } else {
                if (guest.getPronouns() != null && guest.getPhone() != null && guest.getAddress() != null) {
                    updateGuestUseCase.updateOwner(new UpdateGuestUseCase.OwnerRequestModel(
                            guest.getId(),
                            guest.getName(),
                            guest.getPronouns(),
                            guest.getBirthdate(),
                            guest.getPhone().getValue(),
                            guest.getCpf().getValue(),
                            guest.getAddress().getRoad(),
                            guest.getAddress().getCity(),
                            guest.getAddress().getState().name(),
                            guest.getAddress().getCep(),
                            guest.getAddress().getDistrict(),
                            guest.getAddress().getComplement())
                    );
                } else {
                    updateGuestUseCase.updateGuest(new UpdateGuestUseCase.GuestRequestModel(
                            guest.getId(),
                            guest.getName(),
                            guest.getBirthdate(),
                            guest.getCpf().getValue()
                    ));
                }
            }

            navHandler.navigateToGuestPage();
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
        }
    }
}
