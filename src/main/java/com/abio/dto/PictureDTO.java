package com.abio.dto;

public class PictureDTO {

    private String name;
    private String base64Picture;

    public PictureDTO(String name, String base64Picture) {
        this.name = name;
        this.base64Picture = base64Picture;
    }

    public PictureDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64Picture() {
        return base64Picture;
    }

    public void setBase64Picture(String base64Picture) {
        this.base64Picture = base64Picture;
    }
}
