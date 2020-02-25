package com.tekser.domain.business;

import com.tekser.customAnnotations.ValidEmail;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "İsim alanı boş olamaz")
    private String name;

    private String surname;

    private String phone;

    private String gsm;

    @ValidEmail
    private String email;

    private String address;

    private String notes;

    @ManyToOne
    private ClientDetail clientDetail;

    @OneToMany
    @JoinColumn(name = "client_id")
    private List<Receipt> receiptList;

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

    public ClientDetail getClientDetail() {
        return clientDetail;
    }

    public void setClientDetail(ClientDetail clientDetail) {
        this.clientDetail = clientDetail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Receipt> getReceiptList() {
        return receiptList;
    }

    public void setReceiptList(List<Receipt> receiptList) {
        this.receiptList = receiptList;
    }
}
