package com.abio.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.CsvToBeanFilter;
import eu.europa.esig.dss.model.InMemoryDocument;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils {
    public static String[] productColumns = {
            "productCode", "name", "quantity", "price", "discount", "discountPrice",
            "name_en", "name_ru", "name_am", "title_en", "title_ru", "title_am",
            "description_en", "description_ru", "description_am", "category1", "category2", "category3",
            "color", "dimensions_en", "dimensions_ru", "dimensions_am", "bulky", "enabled", "hasPictures"
    };

    public static String[] blacklistColumns = {
            "Id", "email", "phoneNumber", "reason", "blacklistedAt"
    };

    public static String[] deliveryRegionsColumns = {
            "Id", "name_en", "name_ru", "name_am", "price", "currencyType", "bulky"
    };

    public static String[] videoColumns = {
            "Id", "title_en", "title_ru", "title_am",
            "description_en", "description_ru", "description_am", "date", "url"
    };

    public static String[] orderColumns = {
            "Id", "orderDateTime", "address", "date",
            "time", "comment", "first_name", "last_name", "email",
            "mobileNo", "totalPrice", "paymentStatus", "paymentType", "isConfirmed", "transactionId"
    };

    public static String[] promoCodeColumns = {
            "Id", "code", "discount", "promoCodeType",
            "validFrom", "validUntil",
            "productCodes", "minimumPurchase", "maxApplications", "currentApplications"
    };

    public static <T> byte[] convertProductToCSV(T[] beans, String[] columns) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(byteArrayOutputStream))) {
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

        return byteArrayOutputStream.toByteArray();
//        byte[] bytes = DSSUtils.toByteArray(new FileDocument(new File(fileName)));
//
//        File file = new File(fileName);
//        if (!file.delete()) {
//            String error = String.format("Can not delete file: %s", fileName);
//            throw new IOException(error);
//        }
//
//        return bytes;
    }

    public static <T> List<T> convertToJavaBean(InMemoryDocument inMemoryDocument, Class<T> beanClass, CsvToBeanFilter filter) {
        return new CsvToBeanBuilder<T>(new InputStreamReader(inMemoryDocument.openStream()))
                .withType(beanClass)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withFilter(filter)
                .build()
                .parse();
    }
}
