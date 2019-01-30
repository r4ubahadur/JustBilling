package com.smi.justbilling.classes;

// package in.smi.ru.uttamlibrary;

import com.google.firebase.database.Exclude;

public class Uttam {
    private String mName;
    private String mImageUrl;
    private String mThumbImageUrl;
    private String mDesc;
    private String mSize;
    private String mBrand;
    private String mCount;
    private String mExpiry;
    private String mColor;
    private String mPrice;
    private String mPrice2;
    private String mPrice3;
    private String mTax;
    private String mKey;

    public Uttam(){

    }

    public Uttam(String imageUrl, String thumbImageUrl, String brand, String color,
                 String count, String desc, String expiry,
                 String name, String price, String tax , String size ){


        mBrand = brand;
        mImageUrl = imageUrl;
        mThumbImageUrl = thumbImageUrl;
        mColor = color;
        mCount = count;
        mDesc = desc;
        mExpiry = expiry;
        mName = name;
        mPrice = price;
        mTax = tax;
        mSize = size;
    }
    public String getBrand(){
        return mBrand;
    }

    public void setBrand(String brand){
        mBrand = brand;
    }

    public String getImageUrl(){
        return mImageUrl;
    }


    public void setImageUrl(String imageUrl){
        mImageUrl= imageUrl;
    }
    public String getThumbImageUrl(){
        return mThumbImageUrl;
    }
    public void setThumbImageUrl(String thumbImageUrl){
        mThumbImageUrl= thumbImageUrl;
    }
    public String getColor(){
        return mColor;
    }
    public void setColor(String color){
        mColor = color;
    }

    public String getCount(){
        return mCount;
    }
    public void setCount(String count){
        mCount = count;
    }
    public String getName(){
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
    public String getDesc(){
        return mDesc;
    }
    public void setDesc(String desc){
        mDesc = desc;
    }
    public String getSize(){

        return mSize;

    }

    public void setSize(String size){

        mSize = size;

    }

    public String getPrice(){

        return mPrice;

    }

    public void setPrice(String price){

        mPrice = price;

    }

    public String getExpiry(){
        return mExpiry;
    }

    public void setExpiry(String expiry){

        mExpiry = expiry;

    }
    public String getPrice2(){

        return mPrice2;

    }

    public void setPrice2(String price2){

        mPrice2 = price2;

    }



    public String getPrice3(){

        return mPrice3;

    }

    public void setPrice3(String price3){

        mPrice3 = price3;

    }





    public String getTax(){

        return mTax;

    }

    public void setTax(String tax){

        mTax = tax;

    }




    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }


}