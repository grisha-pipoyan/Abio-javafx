//package com.abio.utils;
//
//import javafx.geometry.Insets;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//
//public class FilterWindowsUtils {
//
//
//    public void createFilterWindow(){
//        // Create a grid pane for the filter field and label
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//        grid.setPadding(new Insets(10, 10, 10, 10));
//
//        // Create a label for the filter field
//        Label filterLabel = new Label("Search by product code:");
//
//        // Create a text field for the quantity filter
//        TextField filterField = new TextField();
//
//        // Add the filter label and field to the grid
//        grid.add(filterLabel, 0, 0);
//        grid.add(filterField, 1, 0);
//
//        // Create a button to apply the filter
//        Button applyButton = new Button("Apply");
//        applyButton.setOnAction(actionEvent -> {
//            try {
//                this.csv = productService.getProductByProductCode(authorizationToken, 0, filterField.getText());
//                resetTableView();
//                loadTable(csv);
//            } catch (Exception e) {
//                javaFxHandling.throwException(e.getMessage());
//            }
//        });
//
//        // Create a horizontal box for the button
//        HBox buttonBox = new HBox();
//        buttonBox.getChildren().add(applyButton);
//        buttonBox.setPadding(new Insets(10, 10, 10, 10));
//        buttonBox.setSpacing(10);
//
//        // Create a vertical box to hold the grid and button
//        VBox vbox = new VBox();
//        vbox.getChildren().addAll(grid, buttonBox);
//
//
//    }
//
//}
