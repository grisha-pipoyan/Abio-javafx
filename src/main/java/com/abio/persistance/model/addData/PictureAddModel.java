package com.abio.persistance.model.addData;

public class PictureAddModel {

    private String name;
    private String base64Picture;

    public PictureAddModel(String name, String base64Picture) {
        this.name = name;
        this.base64Picture = base64Picture;
    }

    public PictureAddModel() {
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
