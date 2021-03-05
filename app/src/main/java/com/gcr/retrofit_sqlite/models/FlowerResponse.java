package com.gcr.retrofit_sqlite.models;


import android.graphics.Bitmap;

public class FlowerResponse {

   String category;
   double price;
   String instructions;
   String photo;
   String name;
   int productId;
   Bitmap picture;

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String getInstructions() {
        return instructions;
    }


    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public int getProductId() {
        return productId;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
