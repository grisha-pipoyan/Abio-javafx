package com.abio;

import com.abio.controller.MainController;
import com.abio.exception.FxmlLoadingException;
import com.abio.service.JavaFxHandling;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootApplication
public class AbioApplication extends AbstractJavaFxApplicationSupport{

    @Value("${spring.application.ui.title}")
    private String applicationTitle;
    @Value("classpath:/fxml/MainControllerPage.fxml")
    private Resource mainResource;
    @Autowired
    private JavaFxHandling javaFxHandling;

    @Value("classpath:/pictures/picture.png")
    private Resource logoResource;


//    @Value("classpath:/fxml/FileSelectingControllerPage.fxml")
//    private Resource fileSelectingControllerResource;

    @Override
    public void start(Stage stage){

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(mainResource.getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            Parent parent = fxmlLoader.load();

            MainController controller = fxmlLoader.getController();
            controller.initialize(applicationContext);

            Scene scene = new Scene(parent, 1000, 600);
            stage.setTitle(applicationTitle);
            stage.setResizable(true);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.getIcons().add(new Image(logoResource.getInputStream()));

            stage.show();

        } catch (IOException e) {
            javaFxHandling.throwException("Системная ошибка. Пожалуйста, повторите позже.");
            throw new FxmlLoadingException(e);
        }

    }

    public static void main(String[] args) {
        launchApp(args);
    }

}
