package com.abio.controller;

import com.abio.csv.model.*;
import com.abio.rest.CurrentStatusEnum;
import com.abio.service.AbioService;
import com.abio.service.JavaFxHandling;
import com.abio.utils.JavaBeanToCSVConverter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.abio.rest.CurrentStatusEnum.*;
import static com.abio.utils.JavaBeanToCSVConverter.convertStringToLong;

@Component
public class AdminController {

    @FXML
    private MenuItem addProductPictures;

    @FXML
    private Button addRowButton;

    @FXML
    private MenuItem changePassword;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exportCSVButton;

    @FXML
    private BorderPane globalPane;

    @FXML
    private Button importCSVButton;

    @FXML
    private MenuItem logout;

    @FXML
    private MenuItem refreshBlacklist;

    @FXML
    private MenuItem refreshGiftCards;

    @FXML
    private MenuItem refreshOrders;
    @FXML
    private MenuItem notPayedOrders;

    @FXML
    private MenuItem refreshProducts;

    @FXML
    private MenuItem filterByQuantityProducts;

    @FXML
    private MenuItem filterByQuantityServices;

    @FXML
    private MenuItem refreshPromoCodes;

    @FXML
    private MenuItem refreshRegions0;

    @FXML
    private MenuItem refreshRegions1;

    @FXML
    private MenuItem refreshServices;

    @FXML
    private MenuItem refreshVideos;

    @FXML
    private Button updateButton;

    @FXML
    private TableView<ObservableList<String>> productTableView;

    @Autowired
    private AbioService abioService;

    @Autowired
    private JavaFxHandling javaFxHandling;

    @Value("classpath:/fxml/MainControllerPage.fxml")
    private Resource mainResource;

    @Value("classpath:/fxml/ChangePasswordControllerPage.fxml")
    private Resource changePasswordControllerResource;


    private final SimpleObjectProperty<CurrentStatusEnum> currentStatus = new SimpleObjectProperty<>(NULL);

    private byte[] csv;

    @FXML
    void initialize(ApplicationContext applicationContext, String authorizationToken) {

        this.productTableView.getItems().clear();
        initializeTable();

        this.deleteButton.disableProperty().bind(productTableView.getSelectionModel().selectedItemProperty().isNull()
                .or(currentStatus.isEqualTo(ORDER)));

        this.importCSVButton.disableProperty().bind(currentStatus.isEqualTo(NULL)
                .or(currentStatus.isEqualTo(PROMOCODE))
                .or(currentStatus.isEqualTo(ORDER)));
        this.exportCSVButton.disableProperty().bind(currentStatus.isEqualTo(NULL)
                .or(currentStatus.isEqualTo(PROMOCODE))
                .or(currentStatus.isEqualTo(ORDER)));

        this.addRowButton.disableProperty().bind(currentStatus.isEqualTo(NULL)
                .or(currentStatus.isEqualTo(PRODUCT))
                .or(currentStatus.isEqualTo(ORDER)));

        this.updateButton.disableProperty().bind(productTableView.getSelectionModel().selectedItemProperty().isNull()
                .or(currentStatus.isEqualTo(NULL).or(currentStatus.isEqualTo(ORDER))));

        this.refreshProducts.setOnAction(event -> {
            try {
                currentStatus.set(PRODUCT);
                resetTableView();
                this.csv = abioService.getAllProductsCSV(authorizationToken, 0);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.filterByQuantityProducts.setOnAction(event -> {
            try {

                currentStatus.set(PRODUCT);
                resetTableView();
                Set<String> keySet = new HashSet<>();
                keySet.add("0");
                keySet.add("Greater than 0");
                ChoiceDialog<String> dialog = new ChoiceDialog<>(keySet.iterator().next(), keySet);
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(javaFxHandling.getLogoResource().getInputStream()));
                dialog.setHeaderText("Select option");
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    if (result.get().equals("0")) {
                        this.csv = abioService.getAllProductsCSVByQuantity(authorizationToken, 0, 0);
                    } else {
                        this.csv = abioService.getAllProductsCSVByQuantity(authorizationToken, 0, 100);
                    }
                }
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.filterByQuantityServices.setOnAction(event -> {
            try {

                currentStatus.set(PRODUCT);
                resetTableView();
                Set<String> keySet = new HashSet<>();
                keySet.add("0");
                keySet.add("Greater than 0");
                ChoiceDialog<String> dialog = new ChoiceDialog<>(keySet.iterator().next(), keySet);
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(javaFxHandling.getLogoResource().getInputStream()));
                dialog.setHeaderText("Select option");
                Optional<String> result = dialog.showAndWait();

                if (result.isPresent()) {
                    if (result.get().equals("0")) {
                        this.csv = abioService.getAllProductsCSVByQuantity(authorizationToken, 1, 0);
                    } else {
                        this.csv = abioService.getAllProductsCSVByQuantity(authorizationToken, 1, 100);
                    }
                }
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshServices.setOnAction(event -> {
            try {
                currentStatus.set(SERVICE);
                resetTableView();
                csv = abioService.getAllProductsCSV(authorizationToken, 1);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshGiftCards.setOnAction(event -> {
            try {
                currentStatus.set(GIFTCARD);
                resetTableView();
                csv = abioService.getAllProductsCSV(authorizationToken, 2);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshOrders.setOnAction(event -> {
            try {
                currentStatus.set(ORDER);
                resetTableView();
                csv = abioService.getOrderDetails(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.notPayedOrders.setOnAction(event -> {
            try {
                currentStatus.set(ORDER);
                resetTableView();
                csv = abioService.getNotPayedOrders(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshPromoCodes.setOnAction(event -> {
            try {
                currentStatus.set(PROMOCODE);
                resetTableView();
                csv = abioService.getPromoCodes(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshRegions0.setOnAction(event -> {
            try {
                currentStatus.set(REGION_0);
                resetTableView();
                csv = abioService.getDeliveryRegions(authorizationToken, 0);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshRegions1.setOnAction(event -> {
            try {
                currentStatus.set(REGION_1);
                resetTableView();
                csv = abioService.getDeliveryRegions(authorizationToken, 1);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshVideos.setOnAction(event -> {
            try {
                currentStatus.set(VIDEO);
                resetTableView();
                csv = abioService.getVideos(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshBlacklist.setOnAction(event -> {
            try {
                currentStatus.set(BLACKLIST);
                resetTableView();
                csv = abioService.getBlacklistedCustomers(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });


        this.deleteButton.setOnAction(event -> {

            ObservableList<String> selectedItem = productTableView.getSelectionModel().getSelectedItem();

            String id = selectedItem.get(0);

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Вы хотите удалить?");
                alert.setTitle("Удаление");
                Optional<ButtonType> choose = alert.showAndWait();

                if (choose.isPresent() && choose.get() == ButtonType.OK) {
                    switch (this.currentStatus.get()) {
                        case VIDEO -> abioService.deleteVideoById(authorizationToken, id);
                        case BLACKLIST -> abioService.deleteBlacklistCustomerById(authorizationToken, id);
                        case REGION_0, REGION_1 -> abioService.deleteRegionById(authorizationToken, id);
                        case PROMOCODE -> abioService.deletePromoCodeById(authorizationToken, id);
                        default -> throw new Exception("Невозможно удалить. Разрешено только редактирование");
                    }

                    javaFxHandling.showAlert("Удалено успешно");
                }

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.updateButton.setOnAction(actionEvent -> {

            ObservableList<String> selectedItem = productTableView.getSelectionModel().getSelectedItem();
            switch (this.currentStatus.get()) {
                case PRODUCT, SERVICE, GIFTCARD -> {
                    try {
                        ProductModelAdd product = JavaBeanToCSVConverter.convertListToProductModelAdmin(selectedItem);
                        abioService.updateProduct(product, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");

                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case VIDEO -> {
                    try {
                        Video video = JavaBeanToCSVConverter.convertListToVideo(selectedItem);
                        abioService.updateVideo(video, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case BLACKLIST -> {
                    try {
                        BlacklistedCustomer customer = JavaBeanToCSVConverter.convertListToBlacklistCustomer(selectedItem);
                        abioService.updateBlacklistedCustomer(customer, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case REGION_0, REGION_1 -> {
                    try {
                        DeliveryRegion region = JavaBeanToCSVConverter.convertListToDeliveryRegion(selectedItem);
                        abioService.updateDeliveryRegion(region, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case PROMOCODE -> {
                    try {
                        PromoCode promoCode = JavaBeanToCSVConverter.convertListToPromoCodeModel(selectedItem);
                        promoCode.setId(Long.valueOf(selectedItem.get(0)));
                        abioService.updatePromoCode(promoCode, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
            }
        });

        this.exportCSVButton.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                //Set extension filter for text files
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
                fileChooser.getExtensionFilters().add(extFilter);

                File file = fileChooser.showSaveDialog(new Stage());

                if (file != null) {
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        Utils.copy(new ByteArrayInputStream(this.csv), fos);
                        javaFxHandling.showAlert("Файл успешно сохранен.");
                    }
                }

            } catch (Exception e) {
                javaFxHandling.throwException("Не удалось сохранить файл. Попробуйте позже.");
            }
        });

        this.importCSVButton.setOnAction(event -> {
            try {
                FileChooser fil_chooser = new FileChooser();
                File file = fil_chooser.showOpenDialog(new Stage());

                if (file == null) {
                    return;
                }

                abioService.importCSV(DSSUtils.toByteArray(file), this.currentStatus.get(), authorizationToken);
                javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.addProductPictures.setOnAction(event -> {
            try {

                FileChooser fil_chooser = new FileChooser();
                List<File> files = fil_chooser.showOpenMultipleDialog(new Stage());

                if (files == null) {
                    return;
                }

                abioService.addPictures(authorizationToken, files);

                javaFxHandling.showAlert("Добавлено успешно");

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.addRowButton.setOnAction(actionEvent -> {

            BorderPane borderPane = new BorderPane();
            VBox vBox = new VBox();

            switch (this.currentStatus.get()) {
                case BLACKLIST -> {
                    TextField email = new TextField();
                    TextField phoneNumber = new TextField();
                    TextField reason = new TextField();
                    Button addButton = new Button("Добавлять");

                    addButton.setOnAction(actionEvent1 -> {
                        BlacklistedCustomer blacklistedCustomer = new BlacklistedCustomer();
                        blacklistedCustomer.setEmail(email.getText());
                        blacklistedCustomer.setPhoneNumber(phoneNumber.getText());
                        blacklistedCustomer.setReason(reason.getText());
                        try {
                            abioService.addBlacklistCustomer(blacklistedCustomer, authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });

                    vBox.getChildren().addAll(new Label("Email"),
                            email,
                            new Label("Phone number"),
                            phoneNumber,
                            new Label("Причина"),
                            reason,
                            addButton);
                    borderPane.setCenter(vBox);

                    Stage stage = new Stage();
                    Scene scene = new Scene(borderPane, 300, 170);
                    stage.setScene(scene);
                    stage.setTitle("Add black list customer");
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setResizable(false);
                    stage.showAndWait();
                }
                case VIDEO -> {

                    TextField title_en = new TextField();
                    TextField title_ru = new TextField();
                    TextField title_am = new TextField();

                    TextField description_en = new TextField();
                    TextField description_ru = new TextField();
                    TextField description_am = new TextField();
                    TextField date = new TextField();

                    TextField url = new TextField();

                    Button addButton = new Button("Добавлять");

                    addButton.setOnAction(actionEvent1 -> {
                        Video video = new Video();
                        video.setTitle_en(title_en.getText());
                        video.setTitle_ru(title_ru.getText());
                        video.setTitle_am(title_am.getText());

                        video.setDescription_en(description_en.getText());
                        video.setDescription_ru(description_ru.getText());
                        video.setDescription_am(description_am.getText());

                        video.setDate(date.getText());
                        video.setUrl(url.getText());

                        try {
                            abioService.addVideo(video, authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });

                    vBox.getChildren().addAll(
                            new Label("Title EN"),
                            title_en,
                            new Label("Title RU"),
                            title_ru,
                            new Label("Title AM"),
                            title_am,
                            new Label("Description EN"),
                            description_en,
                            new Label("Description RU"),
                            description_ru,
                            new Label("Description AM"),
                            description_am,
                            new Label("URL"),
                            date,
                            new Label("Date"),
                            url,
                            addButton);
                    borderPane.setCenter(vBox);

                    Stage stage = new Stage();
                    Scene scene = new Scene(borderPane, 300, 370);
                    stage.setScene(scene);
                    stage.setTitle("Add video url");
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setResizable(false);
                    stage.showAndWait();
                }
                case REGION_0, REGION_1 -> {
                    TextField name_en = new TextField();
                    TextField name_ru = new TextField();
                    TextField name_am = new TextField();
                    TextField price = new TextField();
                    TextField bulky = new TextField();
                    Button addButton = new Button("Добавлять");

                    addButton.setOnAction(actionEvent1 -> {
                        try {
                            abioService.addDeliveryRegion(name_en.getText(), name_ru.getText(), name_am.getText(),
                                    new BigDecimal(price.getText()), Integer.valueOf(bulky.getText()), authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });

                    vBox.getChildren().addAll(
                            new Label("English"),
                            name_en,
                            new Label("Russian"),
                            name_ru,
                            new Label("Armenian"),
                            name_am,
                            new Label("Price"),
                            price,
                            new Label("Bulky"),
                            bulky,
                            addButton);
                    borderPane.setCenter(vBox);

                    Stage stage = new Stage();
                    Scene scene = new Scene(borderPane, 300, 300);
                    stage.setScene(scene);
                    stage.setTitle("Add delivery region url");
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setResizable(false);
                    stage.showAndWait();

                }
                case PROMOCODE -> {

                    SimpleObjectProperty<PromoCodeType> property = new SimpleObjectProperty<>(null);

                    TextField codeField;
                    TextField discountField;
                    ComboBox<PromoCodeType> promoCodeTypeComboBox;
                    DatePicker validFromPicker;
                    DatePicker validUntilPicker;
                    TextField minPurchaseAmountField;
                    TextField productCodeField;
                    TextField maxApplicationsField;
                    Button addButton = new Button("Добавлять");

                    // Code field
                    Label codeLabel = new Label("Code:");
                    codeField = new TextField();

                    // Discount field
                    Label discountLabel = new Label("Discount:");
                    discountField = new TextField();

                    // Promo code type combo box
                    Label promoCodeTypeLabel = new Label("Promo Code Type:");
                    promoCodeTypeComboBox = new ComboBox<>();
                    promoCodeTypeComboBox.getItems().addAll(PromoCodeType.values());

                    promoCodeTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                            (observableValue, promoCodeType, t1) -> {
                                property.set(t1);
                            });

                    // Valid from date picker
                    Label validFromLabel = new Label("Valid From:");
                    validFromPicker = new DatePicker();
                    validFromPicker.setPromptText("Select date");

                    // Valid until date picker
                    Label validUntilLabel = new Label("Valid Until:");
                    validUntilPicker = new DatePicker();
                    validUntilPicker.setPromptText("Select date");

                    // Minimum purchase amount field
//                    Label minPurchaseAmountLabel = new Label("Minimum Purchase Amount:");
//                    minPurchaseAmountField = new TextField();

//                    minPurchaseAmountField.disableProperty().bind(property.isEqualTo(PromoCodeType.CERTAIN_PRODUCT)
//                            .or(property.isEqualTo(PromoCodeType.VALIDITY_PERIOD)));


                    // Product code field
                    Label productCodeLabel = new Label("Product Code:");
                    productCodeField = new TextField();

                    productCodeField.disableProperty().bind(property.isEqualTo(PromoCodeType.PURCHASE_AMOUNT)
                            .or(property.isEqualTo(PromoCodeType.VALIDITY_PERIOD)));

                    // Max applications field
                    Label maxApplicationsLabel = new Label("Max Applications:");
                    maxApplicationsField = new TextField();


                    addButton.setOnAction(actionEvent1 -> {
                        try {
                            PromoCode promoCode = null;
                            switch (promoCodeTypeComboBox.getSelectionModel().getSelectedItem()) {
                                case VALIDITY_PERIOD -> promoCode = new PromoCode(
                                        codeField.getText(), new BigDecimal(discountField.getText()),
                                        promoCodeTypeComboBox.getSelectionModel().getSelectedItem(),
                                        validFromPicker.getValue(), validUntilPicker.getValue(),
                                        Integer.valueOf(maxApplicationsField.getText())
                                );
                                case CERTAIN_PRODUCT -> promoCode = new PromoCode(
                                        codeField.getText(), new BigDecimal(discountField.getText()),
                                        promoCodeTypeComboBox.getSelectionModel().getSelectedItem(),
                                        convertStringToLong(productCodeField.getText()),
                                        validFromPicker.getValue(), validUntilPicker.getValue(),
                                        Integer.valueOf(maxApplicationsField.getText())
                                );
                            }

                            abioService.addPromoCode(promoCode, authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });

                    vBox.getChildren().addAll(
                            codeLabel, codeField,
                            discountLabel, discountField,
                            promoCodeTypeLabel, promoCodeTypeComboBox,
                            validFromLabel, validFromPicker,
                            validUntilLabel, validUntilPicker,
                            //minPurchaseAmountLabel, minPurchaseAmountField,
                            productCodeLabel, productCodeField,
                            maxApplicationsLabel, maxApplicationsField,
                            addButton);
                    borderPane.setCenter(vBox);

                    Stage stage = new Stage();
                    Scene scene = new Scene(borderPane, 300, 450);
                    stage.setScene(scene);
                    stage.setTitle("Add promo code");
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setResizable(false);
                    stage.showAndWait();

                }
            }
        });

        this.logout.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(mainResource.getURL());
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

                MainController mainController = fxmlLoader.getController();
                mainController.initialize(applicationContext);

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.changePassword.setOnAction(event -> {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(changePasswordControllerResource.getURL());
                fxmlLoader.setControllerFactory(applicationContext::getBean);
                Pane pane = fxmlLoader.load();

                ChangePasswordController changePasswordController = fxmlLoader.getController();
                changePasswordController.initialize(authorizationToken);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UTILITY);
                stage.setTitle("Смена пароля");
                stage.setScene(new Scene(pane));
                stage.getIcons().add(new Image(this.javaFxHandling.getLogoResource().getInputStream()));
                stage.showAndWait();

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }

        });
    }

    private void loadTable(byte[] excelBytes) throws CsvValidationException, IOException {
        InMemoryDocument inMemoryDocument = new InMemoryDocument(excelBytes);
        try (CSVReader reader = new CSVReader(new InputStreamReader(inMemoryDocument.openStream()))) {
            String[] headers = reader.readNext();
            for (String header : headers) {
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(header);
                col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()
                        .get(productTableView.getColumns().indexOf(col))));
                col.setCellFactory(TextFieldTableCell.forTableColumn());

                col.setOnEditCommit(event1 -> {
                    int rowIndex = event1.getTablePosition().getRow();
                    int columnIndex = event1.getTablePosition().getColumn();
                    String newValue = event1.getNewValue();
                    ObservableList<String> strings = productTableView.getItems().get(rowIndex);

                    strings.set(columnIndex, newValue);

                });
                productTableView.getColumns().add(col);
            }

            ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
            String[] row;
            while ((row = reader.readNext()) != null) {
                data.add(FXCollections.observableArrayList(row));
            }

            productTableView.setItems(data);

        }
    }

    private void resetTableView() {
        this.productTableView.getColumns().clear();
        this.productTableView.getItems().clear();
    }

    private void initializeTable() {
        this.productTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.productTableView.setEditable(true);
    }


}
