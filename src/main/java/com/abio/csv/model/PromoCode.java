package com.abio.csv.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PromoCode {
    private Long Id;
    private String code;
    private BigDecimal discount;
    private PromoCodeType promoCodeType;

    // 2022-03-10
    private LocalDate validFrom;
    private LocalDate validUntil;
    private List<Long> productCodes;
    private Integer maxApplications;
    private Integer currentApplications;

    // Constructor for promo code with validity period
    public PromoCode(String code, BigDecimal discount,
                     PromoCodeType promoCodeType,
                     LocalDate validFrom, LocalDate validUntil,
                     Integer maxApplications) {
        this.code = code;
        this.discount = discount;
        this.promoCodeType = promoCodeType;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.maxApplications = maxApplications;
        this.currentApplications = 0;
    }

    // Constructor for promo code with specific product
    public PromoCode(String code, BigDecimal discount,
                     PromoCodeType promoCodeType, List<Long> productCodes,
                     LocalDate validFrom, LocalDate validUntil,
                     Integer maxApplications) {
        this.code = code;
        this.discount = discount;
        this.promoCodeType = promoCodeType;
        this.productCodes = productCodes;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.maxApplications = maxApplications;
        this.currentApplications = 0;
    }

    // Method to check if promo code is valid now
    public boolean isValidNow() {
        LocalDate now = LocalDate.now();
        return now.isEqual(validFrom) || (now.isAfter(validFrom) && now.isBefore(validUntil));
    }

    // Check if a promocode is exhausted based on its current and max application
    public boolean isExhausted() {
        return currentApplications >= maxApplications;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public PromoCodeType getPromoCodeType() {
        return promoCodeType;
    }

    public void setPromoCodeType(PromoCodeType promoCodeType) {
        this.promoCodeType = promoCodeType;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public List<Long> getProductCodes() {
        return productCodes;
    }

    public void setProductCodes(List<Long> productCodes) {
        this.productCodes = productCodes;
    }

    public Integer getMaxApplications() {
        return maxApplications;
    }

    public void setMaxApplications(Integer maxApplications) {
        this.maxApplications = maxApplications;
    }

    public Integer getCurrentApplications() {
        return currentApplications;
    }

    public void setCurrentApplications(Integer currentApplications) {
        this.currentApplications = currentApplications;
    }
}


