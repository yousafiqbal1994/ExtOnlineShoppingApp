package com.project.eos;

/**
 * Created by YouCaf Iqbal on 3/7/2017.
 */

public class AllItemsDetails {
    public String shopID;



    public String shopName;
    public String mobileID;
    public String link,oldPrice,newPrice,localOnline,title,imageURL,brandID;

    public AllItemsDetails(String shopID,String shopName,String mobileID,String link,String oldPrice,String newPrice,String localOnline,String title,String imageURL,String brandID) {

        this.shopID = shopID;
        this.shopName = shopName;
        this.mobileID = mobileID;
        this.link = link;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.localOnline = localOnline;
        this.title = title;
        this.imageURL = imageURL;
        this.brandID = brandID;
    }

    public String getShopName() {
        return shopName;
    }
    public String getShopID() {
        return shopID;
    }

    public String getMobileID() {
        return mobileID;
    }

    public String getLink() {
        return link;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public String getLocalOnline() {
        return localOnline;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getBrandID() {
        return brandID;
    }
}
