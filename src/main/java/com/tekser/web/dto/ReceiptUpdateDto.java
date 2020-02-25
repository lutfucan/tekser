package com.tekser.web.dto;

import com.tekser.customAnnotations.PasswordMatches;
import com.tekser.customAnnotations.ValidEmail;
import com.tekser.domain.business.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@PasswordMatches
public class ReceiptUpdateDto {
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
    private LocalDateTime dateOfReceipt;
    private LocalDateTime dateOfStartToRepair;
    private LocalDateTime dateOfEndingRepair;
    private LocalDateTime dateOfDeliverToClient;

    private Set<Accessory> accessory;

    public ReceiptUpdateDto() {
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

    public LocalDateTime getDateOfReceipt() {
        return dateOfReceipt;
    }

    public void setDateOfReceipt(LocalDateTime dateOfReceipt) {
        this.dateOfReceipt = dateOfReceipt;
    }

    public LocalDateTime getDateOfStartToRepair() {
        return dateOfStartToRepair;
    }

    public void setDateOfStartToRepair(LocalDateTime dateOfStartToRepair) {
        this.dateOfStartToRepair = dateOfStartToRepair;
    }

    public LocalDateTime getDateOfEndingRepair() {
        return dateOfEndingRepair;
    }

    public void setDateOfEndingRepair(LocalDateTime dateOfEndingRepair) {
        this.dateOfEndingRepair = dateOfEndingRepair;
    }

    public LocalDateTime getDateOfDeliverToClient() {
        return dateOfDeliverToClient;
    }

    public void setDateOfDeliverToClient(LocalDateTime dateOfDeliverToClient) {
        this.dateOfDeliverToClient = dateOfDeliverToClient;
    }

    public Set<Accessory> getAccessory() {
        return accessory;
    }

    public void setAccessory(Set<Accessory> accessory) {
        this.accessory = accessory;
    }
}
