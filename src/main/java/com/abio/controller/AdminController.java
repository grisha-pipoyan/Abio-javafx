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
    private Menu filterMenu;
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
                            blacklistCustomerService.addBlacklistCustomer(blacklistedCustomer, authorizationToken);
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
                        VideoAdminDTO videoAdminDTO = new VideoAdminDTO();
                        videoAdminDTO.setTitle_en(title_en.getText());
                        videoAdminDTO.setTitle_ru(title_ru.getText());
                        videoAdminDTO.setTitle_am(title_am.getText());

                        videoAdminDTO.setDescription_en(description_en.getText());
                        videoAdminDTO.setDescription_ru(description_ru.getText());
                        videoAdminDTO.setDescription_am(description_am.getText());

                        videoAdminDTO.setDate(date.getText());
                        videoAdminDTO.setUrl(url.getText());

                        try {
                            videoService.addVideo(videoAdminDTO, authorizationToken);
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
                            deliverRegionService.addDeliveryRegion(name_en.getText(), name_ru.getText(), name_am.getText(),
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
                            (observableValue, promoCodeType, t1) -> property.set(t1));

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

                            promoCodeService.addPromoCode(promoCode, authorizationToken);
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

    private void initializeProductFilters() {

        this.filterMenu.disableProperty().bind(this.currentStatus.isNotEqualTo(PRODUCT));

        this.filterByQuantityProducts.setOnAction(event -> {
            try {

                currentStatus.set(PRODUCT);

                // Create a grid pane for the filter field and label
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));

                // Create a label for the filter field
                Label filterLabel = new Label("Filter by quantity:");

                // Create a text field for the quantity filter
                TextField filterField = new TextField();
                filterField.setPromptText(">x, <x, =x, >=x, <=x, x-y");

                // Add the filter label and field to the grid
                grid.add(filterLabel, 0, 0);
                grid.add(filterField, 1, 0);

                // Create a button to apply the filter
                Button applyButton = new Button("Apply");
                applyButton.setOnAction(actionEvent -> {
                    try {
                        this.csv = productService.getAllProductsCSVByQuantity(authorizationToken,  filterField.getText());
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

        this.filterByHavingTextInName.setOnAction(event -> {
            try {

                currentStatus.set(PRODUCT);

                // Create a grid pane for the filter field and label
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(10, 10, 10, 10));

                // Create a label for the filter field
                Label filterLabel = new Label("Search by name:");

                // Create a text field for the quantity filter
                TextField filterField = new TextField();

                // Add the filter label and field to the grid
                grid.add(filterLabel, 0, 0);
                grid.add(filterField, 1, 0);

                // Create a button to apply the filter
                Button applyButton = new Button("Apply");
                applyButton.setOnAction(actionEvent -> {
                    try {
                        this.csv = productService.getProductsByHavingName(authorizationToken,  filterField.getText());
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
                Label filterLabel = new Label("Search by product code:");

                // Create a text field for the quantity filter
                TextField filterField = new TextField();

                // Add the filter label and field to the grid
                grid.add(filterLabel, 0, 0);
                grid.add(filterField, 1, 0);

                // Create a button to apply the filter
                Button applyButton = new Button("Apply");
                applyButton.setOnAction(actionEvent -> {
                    try {
                        this.csv = productService.getProductByProductCode(authorizationToken,  filterField.getText());
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
                    this.csv = productService.getProductsByHavingDescription(authorizationToken,  result.get());
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
                    this.csv = productService.getProductsByHavingPictures(authorizationToken,  result.get());
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
