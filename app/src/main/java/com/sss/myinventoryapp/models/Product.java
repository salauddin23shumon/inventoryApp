package com.sss.myinventoryapp.models;

public class Product {
    private String pid;
    private String productName;
    private double rate;
    private int quantity;
    private String unit;
    private double price;
    private int stock;
    private String entryDate;

    public Product(String pid, String productName, double rate, int quantity, String unit, double price, String entryDate) {
        this.pid=pid;
        this.productName = productName;
        this.rate = rate;
        this.quantity = quantity;
        this.stock=quantity;
        this.unit = unit;
        this.price = price;
        this.entryDate = entryDate;
    }

    public Product() {
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }
}
