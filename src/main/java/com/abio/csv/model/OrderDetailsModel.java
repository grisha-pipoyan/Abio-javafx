package com.abio.csv.model;

import java.math.BigDecimal;


public class OrderDetailsModel {
    private String Id;
    private String orderDateTime;
    private String address;
    private String date;
    private String time;
    private String comment;
    private String first_name;
    private String last_name;
    private String email;
    private String mobileNo;
    private BigDecimal totalPrice;
    private String paymentStatus;
    private String paymentType;
    private String transactionId;

    public OrderDetailsModel(String id, String orderDateTime, String address, String date, String time, String comment, String first_name, String last_name, String email, String mobileNo, BigDecimal totalPrice, String paymentStatus, String paymentType, String transactionId) {
        Id = id;
        this.orderDateTime = orderDateTime;
        this.address = address;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mobileNo = mobileNo;
        this.totalPrice = totalPrice;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.transactionId = transactionId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
