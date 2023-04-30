package com.abio.dto;

import java.math.BigDecimal;

public class DeliveryRegion {

    private Long Id;
    private String name_en;
    private String name_ru;
    private String name_am;
    private BigDecimal price;

    private String currencyType;

    private Integer bulky;

    public DeliveryRegion(Long id, String name_en, String name_ru, String name_am, BigDecimal price, String currencyType, Integer bulky) {
        Id = id;
        this.name_en = name_en;
        this.name_ru = name_ru;
        this.name_am = name_am;
        this.price = price;
        this.currencyType = currencyType;
        this.bulky = bulky;
    }

    public DeliveryRegion() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public String getName_am() {
        return name_am;
    }

    public void setName_am(String name_am) {
        this.name_am = name_am;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Integer getBulky() {
        return bulky;
    }

    public void setBulky(Integer bulky) {
        this.bulky = bulky;
    }
}
