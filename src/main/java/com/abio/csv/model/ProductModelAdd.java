package com.abio.csv.model;

public class ProductModelAdd {
    private String productCode;

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

    private Boolean display;

    public ProductModelAdd(String productCode, String name_en, String name_ru, String name_am, String title_en, String title_ru, String title_am, String description_en, String description_ru, String description_am, Long category1, Long category2, Long category3, String color, String dimensions_en, String dimensions_ru, String dimensions_am, Integer bulky, Boolean display) {
        this.productCode = productCode;
        this.name_en = name_en;
        this.name_ru = name_ru;
        this.name_am = name_am;
        this.title_en = title_en;
        this.title_ru = title_ru;
        this.title_am = title_am;
        this.description_en = description_en;
        this.description_ru = description_ru;
        this.description_am = description_am;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.color = color;
        this.dimensions_en = dimensions_en;
        this.dimensions_ru = dimensions_ru;
        this.dimensions_am = dimensions_am;
        this.bulky = bulky;
        this.display = display;
    }

    public ProductModelAdd() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }
}
