package com.abio.utils;

import com.abio.dto.*;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class JavaBeanToCSVConverter {

    public static ProductAdminDTO convertListToProductModelAdmin(ObservableList<String> element) {

        ProductAdminDTO product = new ProductAdminDTO();

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
            product.setEnabled(Boolean.valueOf(element.get(23)));
        }

        if (!Objects.equals(element.get(24), "")) {
            product.setHasPictures(Boolean.valueOf(element.get(23)));
        }

        return product;

    }

    public static VideoAdminDTO convertListToVideo(ObservableList<String> element) {
        VideoAdminDTO videoAdminDTO = new VideoAdminDTO();
        videoAdminDTO.setId(Long.valueOf(element.get(0)));

        videoAdminDTO.setTitle_en(element.get(1));
        videoAdminDTO.setTitle_ru(element.get(2));
        videoAdminDTO.setTitle_am(element.get(3));

        videoAdminDTO.setDescription_en(element.get(4));
        videoAdminDTO.setDescription_ru(element.get(5));
        videoAdminDTO.setDescription_am(element.get(6));
        videoAdminDTO.setDate(element.get(7));
        videoAdminDTO.setUrl(element.get(8));

        return videoAdminDTO;
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
                        element.get(1),
                        new BigDecimal(element.get(2)),
                        new BigDecimal(element.get(7)),
                        promoCodeType,
                        convertStringToLong(element.get(6)),
                        LocalDate.parse(element.get(4)),
                        LocalDate.parse(element.get(5)),
                        Integer.valueOf(element.get(8))
                );
            }
            case VALIDITY_PERIOD -> {
                return new PromoCode(
                        element.get(1),
                        new BigDecimal(element.get(2)),
                        new BigDecimal(element.get(7)),
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

}
