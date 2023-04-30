package com.abio.dto;

public class VideoAdminDTO {
    private Long Id;
    private String title_en;
    private String title_ru;
    private String title_am;

    private String description_en;
    private String description_ru;
    private String description_am;
    private String date;
    private String url;

    public VideoAdminDTO() {
    }

    public VideoAdminDTO(Long id, String title_en, String title_ru, String title_am, String description_en, String description_ru, String description_am, String date, String url) {
        Id = id;
        this.title_en = title_en;
        this.title_ru = title_ru;
        this.title_am = title_am;
        this.description_en = description_en;
        this.description_ru = description_ru;
        this.description_am = description_am;
        this.date = date;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
