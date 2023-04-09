package com.abio.rest;


public class DeleteModel {
    private Long houseId;

    public DeleteModel(Long houseId) {
        this.houseId = houseId;
    }

    public DeleteModel() {
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }
}
