package com.project.eos;

/**
 * Created by Infinal on 5/16/2017.
 */

public class searchedProductDetails {

    public String ID;
    public String title;


    public String getBrandName() {
        return brandName;
    }

    public String brandName;
    public String image;
    public String webLink;
    public String disount;
    public String minPrice;
    public String availability;

    public String getOnlinePrice() {
        return onlinePrice;
    }

    public String onlinePrice;

    public String getDistance() {
        return distance;
    }

    public String distance;

    public String getMainPageDistance() {
        return mainPageDistance;
    }

    public String mainPageDistance;

    public searchedProductDetails(String ID,String title,String image, String webLink, String disount, String minPrice, String availability,String brandName,String distance,String empty,String eeee) {
        this.ID = ID;
        this.title=title;
        this.image = image;
        this.brandName = brandName;
        this.webLink = webLink;
        this.disount =disount;
        this.minPrice = minPrice;
        this.availability = availability;
        this.distance = distance;
    }



    public searchedProductDetails(String ID,String title,String image, String webLink, String disount, String minPrice, String availability,String brandName,String onlinePrice, String mainPageDistance) {
        this.ID = ID;
        this.title=title;
        this.mainPageDistance = mainPageDistance;
        this.image = image;
        this.brandName = brandName;
        this.webLink = webLink;
        this.disount =disount;
        this.minPrice = minPrice;
        this.availability = availability;
        this.onlinePrice = onlinePrice;
    }

    public String getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getWebLink() {
        return webLink;
    }

    public String getDisount() {
        return disount;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getAvailability() {
        return availability;
    }
}
