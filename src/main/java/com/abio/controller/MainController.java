package com.abio.controller;

import com.abio.service.JavaFxHandling;
import com.abio.service.AbioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    @FXML
    private BorderPane globalPane;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginText;
    @FXML
    private PasswordField passwordText;

    @Autowired
    private JavaFxHandling javaFxHandling;

    @Autowired
    private AbioService abioService;

    private String authorizationToken;

    @Value("classpath:/fxml/AdminControllerPage.fxml")
    private Resource adminControllerResource;

    @FXML
    public void initialize(ApplicationContext applicationContext) {

        this.loginButton.setOnAction(actionEvent -> {

            String login = loginText.getText();
            String password = passwordText.getText();

            if (login == null || login.equals("")) {
                javaFxHandling.throwException("Имя для входа пусто.");
                return;
            }
            if (password == null || password.equals("")) {
                javaFxHandling.throwException("Пароль пуст.");
                return;
            }

            try {
                //authorizationToken = abioService.initiate(login, password);
                authorizationToken = abioService.initiate("abio@gmail.com", "password");
                if (authorizationToken == null || authorizationToken.equals("")) {
                    throw new Exception("Системная ошибка. Пожалуйста, повторите попытку позже.");
                }

                FXMLLoader fxmlLoader = new FXMLLoader(adminControllerResource.getURL());
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                BorderPane borderPane = fxmlLoader.load();

                Stage stage = (Stage) globalPane.getScene().getWindow();
                Scene scene = stage.getScene();
                if (scene == null) {
                    scene = new Scene(borderPane, 1000, 600);
                    stage.setScene(scene);
                } else {
                    stage.getScene().setRoot(borderPane);
                }

                AdminController adminController = fxmlLoader.getController();
                adminController.initialize(applicationContext, authorizationToken);

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }

        });

    }

}
