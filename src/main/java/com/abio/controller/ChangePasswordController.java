package com.abio.controller;

import com.abio.service.JavaFxHandling;
import com.abio.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordController {

    @FXML
    private Button changePasswordButton;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField oldPassword;

    @Autowired
    private JavaFxHandling javaFxHandling;

    @Autowired
    private UserService userService;

    @FXML
    void initialize(String authorizationToken) {

        this.changePasswordButton.setOnAction(event -> {
            if (newPassword.getText() == null || newPassword.getText().equals("")) {
                javaFxHandling.throwException("Введите новый пароль.");
                return;
            }
            if (oldPassword.getText() == null || oldPassword.getText().equals("")) {
                javaFxHandling.throwException("Введите старый пароль.");
                return;
            }

            try {
                userService.changePassword(oldPassword.getText(), newPassword.getText(), authorizationToken);
                javaFxHandling.showAlert("Пароль успешно изменен.");
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }

        });

    }

}
