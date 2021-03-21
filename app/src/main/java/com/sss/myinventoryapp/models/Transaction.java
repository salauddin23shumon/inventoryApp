package com.sss.myinventoryapp.models;

public class Transaction {

    private String tid;
    private String type;
    private String productName;
    private int quantity;
    private String date;
    private double rate;


    public Transaction(String tid, String type, String productName, int quantity, String date, double rate) {
        this.tid = tid;
        this.type = type;
        this.productName = productName;
        this.quantity = quantity;
        this.date = date;
        this.rate = rate;
    }

    public Transaction() {

    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
