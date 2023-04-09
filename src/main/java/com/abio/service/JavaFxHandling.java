package com.abio.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JavaFxHandling {
    @Value("classpath:/pictures/picture.png")
    private Resource logoResource;

    public Resource getLogoResource() {
        return logoResource;
    }


    public void throwException(String error) {

        Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.CLOSE);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        alert.setHeaderText("Проблема");
        alert.setTitle("");
        try {
            stage.getIcons().add(new Image(logoResource.getInputStream()));
        } catch (IOException ignored) {

        }
        alert.showAndWait();
    }

    public void showAlert(String alertMessage) {

        Alert alert = new Alert(Alert.AlertType.NONE, alertMessage, ButtonType.OK);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        try {
            stage.getIcons().add(new Image(logoResource.getInputStream()));
        } catch (IOException ignored) {

        }

        alert.showAndWait();
    }

}
