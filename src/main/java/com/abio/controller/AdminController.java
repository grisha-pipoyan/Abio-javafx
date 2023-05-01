package com.abio.controller;

import com.abio.dto.*;
import com.abio.service.*;
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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
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

import static com.abio.dto.CurrentStatusEnum.*;
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
    private MenuItem refreshOrders;
    @FXML
    private MenuItem notPayedOrders;

    @FXML
    private MenuItem refreshProducts;

    @FXML
    private Menu filsterMenu;
    @FXML
    private MenuItem filterByQuantityProducts;
    @FXML
    private MenuItem filterByHavingDescription;
    @FXML
    private MenuItem filterByHavingPictures;
    @FXML
    private MenuItem filterByHavingTextInName;
    @FXML
    private MenuItem filterByProductCode;

    @FXML
    private MenuItem refreshPromoCodes;

    @FXML
    private MenuItem refreshRegions0;

    @FXML
    private MenuItem refreshRegions1;

    @FXML
    private MenuItem refreshVideos;

    @FXML
    private Button updateButton;

    @FXML
    private TableView<ObservableList<String>> productTableView;

    @Autowired
    private ProductService productService;
    @Autowired
    private BlacklistCustomerService blacklistCustomerService;
    @Autowired
    private DeliverRegionService deliverRegionService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private PromoCodeService promoCodeService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private JavaFxHandling javaFxHandling;

    @Value("classpath:/fxml/MainControllerPage.fxml")
    private Resource mainResource;

    @Value("classpath:/fxml/ChangePasswordControllerPage.fxml")
    private Resource changePasswordControllerResource;

    private SimpleObjectProperty<CurrentStatusEnum> currentStatus;

    private byte[] csv;

    private String authorizationToken;

    @FXML
    void initialize(ApplicationContext applicationContext, String authorizationTokenTemp) {

        this.authorizationToken = authorizationTokenTemp;
        this.currentStatus = new SimpleObjectProperty<>(PRODUCT);

        this.productTableView.getItems().clear();
        initializeTable();

        initializeProductFilters();

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
                this.csv = productService.getAllProductsCSV(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshOrders.setOnAction(event -> {
            try {
                currentStatus.set(ORDER);
                resetTableView();
                csv = orderService.getOrderDetails(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.notPayedOrders.setOnAction(event -> {
            try {
                currentStatus.set(ORDER);
                resetTableView();
                csv = orderService.getNotPayedOrders(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshPromoCodes.setOnAction(event -> {
            try {
                currentStatus.set(PROMOCODE);
                resetTableView();
                csv = promoCodeService.getPromoCodes(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshRegions0.setOnAction(event -> {
            try {
                currentStatus.set(REGION_0);
                resetTableView();
                csv = deliverRegionService.getDeliveryRegions(authorizationToken, 0);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshRegions1.setOnAction(event -> {
            try {
                currentStatus.set(REGION_1);
                resetTableView();
                csv = deliverRegionService.getDeliveryRegions(authorizationToken, 1);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshVideos.setOnAction(event -> {
            try {
                currentStatus.set(VIDEO);
                resetTableView();
                csv = videoService.getVideos(authorizationToken);
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.refreshBlacklist.setOnAction(event -> {
            try {
                currentStatus.set(BLACKLIST);
                resetTableView();
                csv = blacklistCustomerService.getBlacklistedCustomers(authorizationToken);
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
                        case VIDEO -> videoService.deleteVideoById(authorizationToken, id);
                        case BLACKLIST -> blacklistCustomerService.deleteBlacklistCustomerById(authorizationToken, id);
                        case REGION_0, REGION_1 -> deliverRegionService.deleteRegionById(authorizationToken, id);
                        case PROMOCODE -> promoCodeService.deletePromoCodeById(authorizationToken, id);
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
                case PRODUCT -> {
                    try {
                        ProductAdminDTO product = JavaBeanToCSVConverter.convertListToProductModelAdmin(selectedItem);
                        productService.updateProduct(product, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");

                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case VIDEO -> {
                    try {
                        VideoAdminDTO videoAdminDTO = JavaBeanToCSVConverter.convertListToVideo(selectedItem);
                        videoService.updateVideo(videoAdminDTO, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case BLACKLIST -> {
                    try {
                        BlacklistedCustomer customer = JavaBeanToCSVConverter.convertListToBlacklistCustomer(selectedItem);
                        blacklistCustomerService.updateBlacklistedCustomer(customer, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case REGION_0, REGION_1 -> {
                    try {
                        DeliveryRegion region = JavaBeanToCSVConverter.convertListToDeliveryRegion(selectedItem);
                        deliverRegionService.updateDeliveryRegion(region, authorizationToken);
                        javaFxHandling.showAlert("Данные обновлены. Пожалуйста, обновите страницу, прежде чем что-то делать.");
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                }
                case PROMOCODE -> {
                    try {
                        PromoCode promoCode = JavaBeanToCSVConverter.convertListToPromoCodeModel(selectedItem);
                        promoCode.setId(Long.valueOf(selectedItem.get(0)));
                        promoCodeService.updatePromoCode(promoCode, authorizationToken);
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

                switch (this.currentStatus.get()) {
                    case PRODUCT -> productService.importCSV(DSSUtils.toByteArray(file), authorizationToken);
                    case REGION_0, REGION_1 ->
                            deliverRegionService.importCSV(DSSUtils.toByteArray(file), authorizationToken);
                    case VIDEO -> videoService.importCSV(DSSUtils.toByteArray(file), authorizationToken);
                    case BLACKLIST ->
                            blacklistCustomerService.importCSV(DSSUtils.toByteArray(file), authorizationToken);
                    default -> throw new Exception("Сначала выберите раздел.");
                }

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

                productService.addPictures(authorizationToken, files);

                javaFxHandling.showAlert("Добавлено успешно");

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.addRowButton.setOnAction(actionEvent -> {

            switch (this.currentStatus.get()) {
                case BLACKLIST -> {
                    TextField email = new TextField();
                    TextField phoneNumber = new TextField();
                    TextField reason = new TextField();
                    Button addButton = new Button("Добавлять");

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(10));

                    Label emailLabel = new Label("Email:");
                    emailLabel.setPadding(new Insets(5));
                    gridPane.add(emailLabel, 0, 0);
                    gridPane.add(email, 1, 0);

                    Label phoneNumberLabel = new Label("Номер телефона:");
                    phoneNumberLabel.setPadding(new Insets(5));
                    gridPane.add(phoneNumberLabel, 0, 1);
                    gridPane.add(phoneNumber, 1, 1);

                    Label reasonLabel = new Label("Причина:");
                    reasonLabel.setPadding(new Insets(5));
                    gridPane.add(reasonLabel, 0, 2);
                    gridPane.add(reason, 1, 2);

                    gridPane.add(addButton, 0, 3, 2, 1);

                    addButton.setOnAction(actionEvent1 -> {
                        BlacklistedCustomer blacklistedCustomer = new BlacklistedCustomer();
                        blacklistedCustomer.setEmail(email.getText());
                        blacklistedCustomer.setPhoneNumber(phoneNumber.getText());
                        blacklistedCustomer.setReason(reason.getText());
                        try {
                            blacklistCustomerService.addBlacklistCustomer(blacklistedCustomer, authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });

                    Stage stage = new Stage();
                    Scene scene = new Scene(gridPane, 300, 200);
                    stage.setScene(scene);
                    stage.setTitle("Добавить клиента в черный список");
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setResizable(false);
                    stage.showAndWait();
                }
                case VIDEO -> {

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(10, 10, 10, 10));

                    Label titleEnLabel = new Label("Title EN:");
                    TextField titleEnField = new TextField();
                    Label titleRuLabel = new Label("Title RU:");
                    TextField titleRuField = new TextField();
                    Label titleAmLabel = new Label("Title AM:");
                    TextField titleAmField = new TextField();

                    Label descriptionEnLabel = new Label("Description EN:");
                    TextField descriptionEnField = new TextField();
                    Label descriptionRuLabel = new Label("Description RU:");
                    TextField descriptionRuField = new TextField();
                    Label descriptionAmLabel = new Label("Description AM:");
                    TextField descriptionAmField = new TextField();

                    Label urlLabel = new Label("URL:");
                    TextField urlField = new TextField();

                    Label dateLabel = new Label("Date:");
                    TextField dateField = new TextField();

                    Button addButton = new Button("Добавлять");
                    addButton.setOnAction(actionEvent1 -> {
                        VideoAdminDTO videoAdminDTO = new VideoAdminDTO();
                        videoAdminDTO.setTitle_en(titleEnField.getText());
                        videoAdminDTO.setTitle_ru(titleRuField.getText());
                        videoAdminDTO.setTitle_am(titleAmField.getText());

                        videoAdminDTO.setDescription_en(descriptionEnField.getText());
                        videoAdminDTO.setDescription_ru(descriptionRuField.getText());
                        videoAdminDTO.setDescription_am(descriptionAmField.getText());

                        videoAdminDTO.setDate(dateField.getText());
                        videoAdminDTO.setUrl(urlField.getText());

                        try {
                            videoService.addVideo(videoAdminDTO, authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });

                    gridPane.add(titleEnLabel, 0, 0);
                    gridPane.add(titleEnField, 1, 0);
                    gridPane.add(titleRuLabel, 0, 1);
                    gridPane.add(titleRuField, 1, 1);
                    gridPane.add(titleAmLabel, 0, 2);
                    gridPane.add(titleAmField, 1, 2);
                    gridPane.add(descriptionEnLabel, 0, 3);
                    gridPane.add(descriptionEnField, 1, 3);
                    gridPane.add(descriptionRuLabel, 0, 4);
                    gridPane.add(descriptionRuField, 1, 4);
                    gridPane.add(descriptionAmLabel, 0, 5);
                    gridPane.add(descriptionAmField, 1, 5);
                    gridPane.add(urlLabel, 0, 6);
                    gridPane.add(urlField, 1, 6);
                    gridPane.add(dateLabel, 0, 7);
                    gridPane.add(dateField, 1, 7);
                    gridPane.add(addButton, 1, 8);

                    Stage stage = new Stage();
                    Scene scene = new Scene(gridPane, 270, 350);
                    stage.setScene(scene);
                    stage.setTitle("Добавить ссылку на видео");
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setResizable(false);
                    stage.showAndWait();

                }
                case REGION_0, REGION_1 -> {

                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(10, 10, 10, 10));

                    TextField name_en = new TextField();
                    TextField name_ru = new TextField();
                    TextField name_am = new TextField();
                    TextField price = new TextField();
                    TextField bulky = new TextField();
                    Button addButton = new Button("Добавлять");

                    addButton.setOnAction(actionEvent1 -> {
                        try {
                            deliverRegionService.addDeliveryRegion(name_en.getText(), name_ru.getText(), name_am.getText(),
                                    new BigDecimal(price.getText()), Integer.valueOf(bulky.getText()), authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });


                    gridPane.add(new Label("English"), 0, 0);
                    gridPane.add(name_en, 1, 0);
                    gridPane.add(new Label("Russian"), 0, 1);
                    gridPane.add(name_ru, 1, 1);
                    gridPane.add(new Label("Armenian"), 0, 2);
                    gridPane.add(name_am, 1, 2);
                    gridPane.add(new Label("Цена"), 0, 3);
                    gridPane.add(price, 1, 3);
                    gridPane.add(new Label("Гоборит"), 0, 4);
                    gridPane.add(bulky, 1, 4);
                    gridPane.add(addButton, 1, 5);


                    Stage stage = new Stage();
                    Scene scene = new Scene(gridPane, 250, 250);
                    stage.setScene(scene);
                    stage.setTitle("Добавить регион доставки");
                    stage.initStyle(StageStyle.UTILITY);
                    stage.setResizable(false);
                    stage.showAndWait();

                }
                case PROMOCODE -> {

                    SimpleObjectProperty<PromoCodeType> property = new SimpleObjectProperty<>(null);

                    TextField codeField = new TextField();
                    TextField discountField = new TextField();
                    ComboBox<PromoCodeType> promoCodeTypeComboBox = new ComboBox<>();
                    DatePicker validFromPicker = new DatePicker();
                    DatePicker validUntilPicker = new DatePicker();
                    TextField minPurchaseAmountField = new TextField();
                    TextField productCodeField = new TextField();
                    TextField maxApplicationsField = new TextField();

                    Label codeLabel = new Label("Код:");
                    Label discountLabel = new Label("Скидка:");
                    Label promoCodeTypeLabel = new Label("Тип промокода:");
                    Label validFromLabel = new Label("Действителен с:");
                    Label validUntilLabel = new Label("Действителен до:");
                    Label minPurchaseAmountLabel = new Label("Мин. сумма:");
                    Label productCodeLabel = new Label("Коды продуктов:");
                    Label maxApplicationsLabel = new Label("Максимальное количество применении:");
                    Button addButton = new Button("Добавлять");

                    promoCodeTypeComboBox.getItems().addAll(PromoCodeType.values());
                    promoCodeTypeComboBox.getSelectionModel().selectedItemProperty().addListener(
                            (observableValue, promoCodeType, t1) -> property.set(t1));

                    productCodeField.disableProperty().bind(property.isEqualTo(PromoCodeType.VALIDITY_PERIOD));

                    // Setting up the layout
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(10);
                    gridPane.setPadding(new Insets(10, 10, 10, 10));

                    gridPane.add(codeLabel, 0, 0);
                    gridPane.add(codeField, 1, 0);
                    gridPane.add(discountLabel, 0, 1);
                    gridPane.add(discountField, 1, 1);
                    gridPane.add(promoCodeTypeLabel, 0, 2);
                    gridPane.add(promoCodeTypeComboBox, 1, 2);
                    gridPane.add(validFromLabel, 0, 3);
                    gridPane.add(validFromPicker, 1, 3);
                    gridPane.add(validUntilLabel, 0, 4);
                    gridPane.add(validUntilPicker, 1, 4);
                    gridPane.add(minPurchaseAmountLabel, 0, 5);
                    gridPane.add(minPurchaseAmountField, 1, 5);
                    gridPane.add(productCodeLabel, 0, 6);
                    gridPane.add(productCodeField, 1, 6);
                    gridPane.add(maxApplicationsLabel, 0, 7);
                    gridPane.add(maxApplicationsField, 1, 7);
                    gridPane.add(addButton, 0, 8, 2, 1);

                    addButton.setOnAction(actionEvent1 -> {
                        try {
                            PromoCode promoCode = null;
                            switch (promoCodeTypeComboBox.getSelectionModel().getSelectedItem()) {
                                case VALIDITY_PERIOD -> promoCode = new PromoCode(
                                        codeField.getText(), new BigDecimal(discountField.getText()),
                                        new BigDecimal(minPurchaseAmountField.getText()),
                                        promoCodeTypeComboBox.getSelectionModel().getSelectedItem(),
                                        validFromPicker.getValue(), validUntilPicker.getValue(),
                                        Integer.valueOf(maxApplicationsField.getText())
                                );
                                case CERTAIN_PRODUCT -> promoCode = new PromoCode(
                                        codeField.getText(), new BigDecimal(discountField.getText()),
                                        new BigDecimal(minPurchaseAmountField.getText()),
                                        promoCodeTypeComboBox.getSelectionModel().getSelectedItem(),
                                        convertStringToLong(productCodeField.getText()),
                                        validFromPicker.getValue(), validUntilPicker.getValue(),
                                        Integer.valueOf(maxApplicationsField.getText())
                                );
                            }

                            promoCodeService.addPromoCode(promoCode, authorizationToken);
                            javaFxHandling.showAlert("Добавлено успешно");
                        } catch (Exception e) {
                            javaFxHandling.throwException(e.getMessage());
                        }
                    });


                    Stage stage = new Stage();
                    Scene scene = new Scene(gridPane, 300, 350);
                    stage.setScene(scene);
                    stage.setTitle("Добавить промокод");
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

    private void initializeProductFilters() {

        //this.filterMenu.disableProperty().bind(this.currentStatus.isNotEqualTo(PRODUCT));

        // Set the key combination
        this.filterByQuantityProducts.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        this.filterByHavingTextInName.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));

        this.filterByProductCode.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));

        this.filterByHavingDescription.setAccelerator(KeyCombination.keyCombination("Ctrl+d"));

        this.filterByHavingPictures.setAccelerator(KeyCombination.keyCombination("Ctrl+p"));

        this.filterByQuantityProducts.setOnAction(event -> {
            try {

                currentStatus.set(PRODUCT);

                // Create a grid pane for the filter field and label
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));

                // Create a label for the filter field
                Label filterLabel = new Label("Фильтровать по количеству:");

                // Create a text field for the quantity filter
                TextField filterField = new TextField();
                filterField.setPromptText(">x, <x, =x, >=x, <=x, x-y");

                // Add the filter label and field to the grid
                grid.add(filterLabel, 0, 0);
                grid.add(filterField, 1, 0);

                // Create a button to apply the filter
                Button applyButton = new Button("Применять");
                applyButton.setOnAction(actionEvent -> {
                    try {
                        this.csv = productService.getAllProductsCSVByQuantity(authorizationToken, filterField.getText());
                        resetTableView();
                        loadTable(csv);
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                });

                // Create a horizontal box for the button
                HBox buttonBox = new HBox();
                buttonBox.getChildren().add(applyButton);
                buttonBox.setPadding(new Insets(10, 10, 10, 10));
                buttonBox.setSpacing(10);

                // Create a vertical box to hold the grid and button
                VBox vbox = new VBox();
                vbox.getChildren().addAll(grid, buttonBox);

                // Set the scene
                Scene scene = new Scene(vbox, 325, 100);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image(javaFxHandling.getLogoResource().getInputStream()));

                stage.showAndWait();

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.filterByHavingTextInName.setOnAction(event -> {
            try {

                currentStatus.set(PRODUCT);

                // Create a grid pane for the filter field and label
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));

                // Create a label for the filter field
                Label filterLabel = new Label("Поиск по имени:");

                // Create a text field for the quantity filter
                TextField filterField = new TextField();

                // Add the filter label and field to the grid
                grid.add(filterLabel, 0, 0);
                grid.add(filterField, 1, 0);

                // Create a button to apply the filter
                Button applyButton = new Button("Применять");
                applyButton.setOnAction(actionEvent -> {
                    try {
                        this.csv = productService.getProductsByHavingName(authorizationToken, filterField.getText());
                        resetTableView();
                        loadTable(csv);
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                });

                // Create a horizontal box for the button
                HBox buttonBox = new HBox();
                buttonBox.getChildren().add(applyButton);
                buttonBox.setPadding(new Insets(10, 10, 10, 10));
                buttonBox.setSpacing(10);

                // Create a vertical box to hold the grid and button
                VBox vbox = new VBox();
                vbox.getChildren().addAll(grid, buttonBox);

                // Set the scene
                Scene scene = new Scene(vbox, 300, 100);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image(javaFxHandling.getLogoResource().getInputStream()));

                stage.showAndWait();

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.filterByProductCode.setOnAction(event -> {
            try {

                currentStatus.set(PRODUCT);

                // Create a grid pane for the filter field and label
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));

                // Create a label for the filter field
                Label filterLabel = new Label("Поиск по артикулу:");

                // Create a text field for the quantity filter
                TextField filterField = new TextField();

                // Add the filter label and field to the grid
                grid.add(filterLabel, 0, 0);
                grid.add(filterField, 1, 0);

                // Create a button to apply the filter
                Button applyButton = new Button("Применять");
                applyButton.setOnAction(actionEvent -> {
                    try {
                        this.csv = productService.getProductByProductCode(authorizationToken, filterField.getText());
                        resetTableView();
                        loadTable(csv);
                    } catch (Exception e) {
                        javaFxHandling.throwException(e.getMessage());
                    }
                });

                // Create a horizontal box for the button
                HBox buttonBox = new HBox();
                buttonBox.getChildren().add(applyButton);
                buttonBox.setPadding(new Insets(10, 10, 10, 10));
                buttonBox.setSpacing(10);

                // Create a vertical box to hold the grid and button
                VBox vbox = new VBox();
                vbox.getChildren().addAll(grid, buttonBox);

                // Set the scene
                Scene scene = new Scene(vbox, 310, 100);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.getIcons().add(new Image(javaFxHandling.getLogoResource().getInputStream()));

                stage.showAndWait();

            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.filterByHavingDescription.setOnAction(actionEvent -> {
            try {
                currentStatus.set(PRODUCT);
                resetTableView();
                Set<Boolean> keySet = new HashSet<>();
                keySet.add(Boolean.TRUE);
                keySet.add(Boolean.FALSE);
                ChoiceDialog<Boolean> dialog = new ChoiceDialog<>(keySet.iterator().next(), keySet);
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(javaFxHandling.getLogoResource().getInputStream()));
                dialog.setHeaderText("Выберите опцию");
                Optional<Boolean> result = dialog.showAndWait();

                if (result.isPresent()) {
                    this.csv = productService.getProductsByHavingDescription(authorizationToken, result.get());
                }
                loadTable(csv);
            } catch (Exception e) {
                javaFxHandling.throwException(e.getMessage());
            }
        });

        this.filterByHavingPictures.setOnAction(actionEvent -> {
            try {
                currentStatus.set(PRODUCT);
                resetTableView();
                Set<Boolean> keySet = new HashSet<>();
                keySet.add(Boolean.TRUE);
                keySet.add(Boolean.FALSE);
                ChoiceDialog<Boolean> dialog = new ChoiceDialog<>(keySet.iterator().next(), keySet);
                Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(javaFxHandling.getLogoResource().getInputStream()));
                dialog.setHeaderText("Выберите опцию");
                Optional<Boolean> result = dialog.showAndWait();

                if (result.isPresent()) {
                    this.csv = productService.getProductsByHavingPictures(authorizationToken, result.get());
                }

                loadTable(csv);
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
        this.productTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        this.productTableView.setEditable(true);
    }


}
