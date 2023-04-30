package com.abio.dto;


import java.math.BigDecimal;

public class ProductCsvDTO {

    // հծ
    private String productCode;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal discountPrice;

    private String name_en;
    private String name_ru;
    private String name_am;

    private String title_en;
    private String title_ru;
    private String title_am;

    private String description_en;
    private String description_ru;
    private String description_am;

    private Long category1;
    private Long category2;
    private Long category3;

    private String color;

    private String dimensions_en;
    private String dimensions_ru;
    private String dimensions_am;

    private Integer bulky;

    private Boolean enabled;
    private Boolean hasPictures;


    public ProductCsvDTO() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
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

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_ru() {
        return title_ru;
    }

    public void setTitle_ru(String title_ru) {
        this.title_ru = title_ru;
    }

    public String getTitle_am() {
        return title_am;
    }

    public void setTitle_am(String title_am) {
        this.title_am = title_am;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_ru() {
        return description_ru;
    }

    public void setDescription_ru(String description_ru) {
        this.description_ru = description_ru;
    }

    public String getDescription_am() {
        return description_am;
    }

    public void setDescription_am(String description_am) {
        this.description_am = description_am;
    }

    public Long getCategory1() {
        return category1;
    }

    public void setCategory1(Long category1) {
        this.category1 = category1;
    }

    public Long getCategory2() {
        return category2;
    }

    public void setCategory2(Long category2) {
        this.category2 = category2;
    }

    public Long getCategory3() {
        return category3;
    }

    public void setCategory3(Long category3) {
        this.category3 = category3;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDimensions_en() {
        return dimensions_en;
    }

    public void setDimensions_en(String dimensions_en) {
        this.dimensions_en = dimensions_en;
    }

    public String getDimensions_ru() {
        return dimensions_ru;
    }

    public void setDimensions_ru(String dimensions_ru) {
        this.dimensions_ru = dimensions_ru;
    }

    public String getDimensions_am() {
        return dimensions_am;
    }

    public void setDimensions_am(String dimensions_am) {
        this.dimensions_am = dimensions_am;
    }

    public Integer getBulky() {
        return bulky;
    }

    public void setBulky(Integer bulky) {
        this.bulky = bulky;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getHasPictures() {
        return hasPictures;
    }

    public void setHasPictures(Boolean hasPictures) {
        this.hasPictures = hasPictures;
    }
}
