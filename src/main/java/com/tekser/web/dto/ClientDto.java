package com.tekser.web.dto;

import com.tekser.customAnnotations.ValidEmail;
import com.tekser.domain.business.ClientDetail;
import com.tekser.domain.business.Receipt;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ClientDto {

    private Long id;

    @NotBlank (message = "İsim gerekli")
    private String name;

    @NotBlank (message = "Soyadı gerekli")
    private String surname;

    private String phone;

    private String gsm;

    @ValidEmail
    private String email;

    private String address;

    private String notes;

    private ClientDetail clientDetail;

    private List<Receipt> receiptList;

    public ClientDto() {
    }

    public ClientDto(Long id, @NotBlank(message = "İsim gerekli") String name, @NotBlank(message = "Soyadı gerekli") String surname, String phone, String gsm, String email, String address, String notes) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.gsm = gsm;
        this.email = email;
        this.address = address;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ClientDetail getClientDetail() {
        return clientDetail;
    }

    public void setClientDetail(ClientDetail clientDetail) {
        this.clientDetail = clientDetail;
    }

    public List<Receipt> getReceiptList() {
        return receiptList;
    }

    public void setReceiptList(List<Receipt> receiptList) {
        this.receiptList = receiptList;
    }
}
