package com.tekser.web.dto;

import com.tekser.configuration.LocalDateTimeFormatter;
import com.tekser.domain.business.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ReceiptDto {

    private Long id;

    private Client client;

    private boolean underWarranty = false;
    private String password;
    private boolean hasBackup = false;
    private Delivery delivery;
    private Branche branche;
    private Product product;
    private Brand brand;
    private ProductModel productModel;
    private String problem;
    private String serialNumber;
    private String dateOfReceipt;
    private String dateOfStartToRepair;
    private String dateOfEndingRepair;
    private String dateOfDeliverToClient;

    private Set<Accessory> accessory = new HashSet<>();

    public ReceiptDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean isUnderWarranty() {
        return underWarranty;
    }

    public void setUnderWarranty(boolean underWarranty) {
        this.underWarranty = underWarranty;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasBackup() {
        return hasBackup;
    }

    public void setHasBackup(boolean hasBackup) {
        this.hasBackup = hasBackup;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Branche getBranche() {
        return branche;
    }

    public void setBranche(Branche branche) {
        this.branche = branche;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public ProductModel getProductModel() {
        return productModel;
    }

    public void setProductModel(ProductModel productModel) {
        this.productModel = productModel;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(String dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public String getDateOfStartToRepair() {
        return dateOfStartToRepair;
    }

    public void setDateOfStartToRepair(String dateOfStartToRepair) {
        this.dateOfStartToRepair = dateOfStartToRepair;
    }

    public String getDateOfEndingRepair() {
        return dateOfEndingRepair;
    }

    public void setDateOfEndingRepair(String dateOfEndingRepair) {
        this.dateOfEndingRepair = dateOfEndingRepair;
    }

    public String getDateOfDeliverToClient() {
        return dateOfDeliverToClient;
    }

    public void setDateOfDeliverToClient(String dateOfDeliverToClient) {
        this.dateOfDeliverToClient = dateOfDeliverToClient;
    }

    public Set<Accessory> getAccessory() {
        return accessory;
    }

    public void setAccessory(Set<Accessory> accessory) {
        this.accessory = accessory;
    }

}
