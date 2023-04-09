package com.abio.utils;

import com.abio.csv.model.*;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.InMemoryDocument;
import eu.europa.esig.dss.spi.DSSUtils;
import javafx.collections.ObservableList;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JavaBeanToCSVConverter {

    public static ProductModelAdd convertListToProductModelAdmin(ObservableList<String> element) {

        ProductModelAdd product = new ProductModelAdd();

        if (!Objects.equals(element.get(0), "")) {
            product.setProductCode(element.get(0));
        }

        product.setName_en(element.get(6));
        product.setName_ru(element.get(7));
        product.setName_am(element.get(8));

        product.setTitle_en(element.get(9));
        product.setTitle_ru(element.get(10));
        product.setTitle_am(element.get(11));

        product.setDescription_en(element.get(12));
        product.setDescription_ru(element.get(13));
        product.setDescription_am(element.get(14));


        if (!Objects.equals(element.get(15), "")) {
            product.setCategory1(Long.valueOf(element.get(15)));
        }
        if (!Objects.equals(element.get(16), "")) {
            product.setCategory2(Long.valueOf(element.get(16)));
        }
        if (!Objects.equals(element.get(17), "")) {
            product.setCategory3(Long.valueOf(element.get(17)));
        }

        product.setColor(element.get(18));

        product.setDimensions_en(element.get(19));
        product.setDimensions_ru(element.get(20));
        product.setDimensions_am(element.get(21));

        if (!Objects.equals(element.get(22), "")) {
            product.setBulky(Integer.valueOf(element.get(22)));
        }

        if (!Objects.equals(element.get(23), "")) {
            product.setDisplay(Boolean.valueOf(element.get(23)));
        }

        return product;

    }

    public static Video convertListToVideo(ObservableList<String> element) {
        Video video = new Video();
        video.setId(Long.valueOf(element.get(0)));

        video.setTitle_en(element.get(1));
        video.setTitle_ru(element.get(2));
        video.setTitle_am(element.get(3));

        video.setDescription_en(element.get(4));
        video.setDescription_ru(element.get(5));
        video.setDescription_am(element.get(6));
        video.setDate(element.get(7));
        video.setUrl(element.get(8));

        return video;
    }

    public static BlacklistedCustomer convertListToBlacklistCustomer(ObservableList<String> element) {
        BlacklistedCustomer blacklistedCustomer = new BlacklistedCustomer();
        blacklistedCustomer.setId(Long.valueOf(element.get(0)));
        blacklistedCustomer.setEmail(element.get(1));
        blacklistedCustomer.setPhoneNumber(element.get(2));
        blacklistedCustomer.setReason(element.get(3));
        blacklistedCustomer.setBlacklistedAt(element.get(4));
        return blacklistedCustomer;
    }

    public static DeliveryRegion convertListToDeliveryRegion(ObservableList<String> element) {
        DeliveryRegion deliveryRegion = new DeliveryRegion();
        deliveryRegion.setId(Long.valueOf(element.get(0)));
        deliveryRegion.setName_en(element.get(1));
        deliveryRegion.setName_ru(element.get(2));
        deliveryRegion.setName_am(element.get(3));
        deliveryRegion.setPrice(new BigDecimal(element.get(4)));
        deliveryRegion.setCurrencyType(element.get(5));
        deliveryRegion.setBulky(Integer.valueOf(element.get(6)));

        return deliveryRegion;
    }

    public static PromoCode convertListToPromoCodeModel(ObservableList<String> element) throws Exception {

        PromoCodeType promoCodeType = PromoCodeType.valueOf(element.get(3));
        switch (promoCodeType) {
            case CERTAIN_PRODUCT -> {
                return new PromoCode(
                        element.get(1), new BigDecimal(element.get(2)),
                        promoCodeType, convertStringToLong(element.get(7)),
                        LocalDate.parse(element.get(4)),
                        LocalDate.parse(element.get(5)),
                        Integer.valueOf(element.get(8))
                );
            }
            case VALIDITY_PERIOD -> {
                return new PromoCode(
                        element.get(1), new BigDecimal(element.get(2)),
                        promoCodeType,
                        LocalDate.parse(element.get(4)),
                        LocalDate.parse(element.get(5)),
                        Integer.valueOf(element.get(8))
                );
            }
//            case PURCHASE_AMOUNT -> {
//                return new PromoCode(
//                        element.get(1), new BigDecimal(element.get(2)),
//                        promoCodeType, new BigDecimal(element.get(6)),
//                        Integer.valueOf(element.get(8))
//                );
//            }
            default -> throw new Exception("Invalid data");
        }
    }

    public static List<Long> convertStringToLong(String input) {
        String[] parts = input.split(",");
        Long[] values = new Long[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = Long.valueOf(parts[i]);
        }

        return List.of(values);
    }

    public static <T> byte[] convertProductsToCSV(ObservableList<ObservableList<String>> beans,
                                                  String[] columns, String fileName) throws IOException {

        // Create a new CSV file
        File file = new File(fileName);
        FileWriter outPutFile = new FileWriter(file);

        CSVWriter writer = new CSVWriter(outPutFile);

        writer.writeNext(columns);

        for (ObservableList<String> row : beans) {
            writer.writeNext(row.toArray(new String[0]));
        }
        writer.close();
        outPutFile.close();

        byte[] bytes = DSSUtils.toByteArray(new FileDocument(new File(fileName)));

        if (!file.delete()) {
            String error = String.format("Can not delete file: %s", fileName);
            throw new IOException(error);
        }

        return bytes;
    }

    public static <T> byte[] convertProductToCSV(List<T> beans, String[] columns, String fileName) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            // Write the column headers to the CSV file
            writer.writeNext(columns);

            // Iterate over each bean and write its property values to the CSV file
            for (T bean : beans) {
                List<String> values = new ArrayList<>();
                for (String column : columns) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(column, bean.getClass());
                    Object value = propertyDescriptor.getReadMethod().invoke(bean);
                    values.add(value != null ? value.toString() : "");
                }
                writer.writeNext(values.toArray(new String[0]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] bytes = DSSUtils.toByteArray(new FileDocument(new File(fileName)));

        File file = new File(fileName);
        if (!file.delete()) {
            String error = String.format("Can not delete file: %s", fileName);
            throw new IOException(error);
        }

        return bytes;
    }

    public static <T> List<T> convertToJavaBean(InMemoryDocument inMemoryDocument, Class<T> beanClass) {
        return new CsvToBeanBuilder<T>(new InputStreamReader(inMemoryDocument.openStream()))
                .withType(beanClass)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .build()
                .parse();
    }


}
