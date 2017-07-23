package com.project.eos;

/**
 * Created by YouCaf Iqbal on 3/7/2017.
 */

public class ShopDetail {
    public String getId() {
        return id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getLocation() {
        return location;
    }

    public String getLink() {
        return link;
    }


    public String getAvailablility() {
        return availablility;
    }

    public String id;
    public String shop_name;
    public String location;
    public String link;

    public String getPhone() {
        return phone;
    }

    public String getMarket_plaza() {
        return market_plaza;
    }

    public String getCity() {
        return city;
    }

    public String getLat() {
        return lat;
    }

    public String getLongi() {
        return longi;
    }

    public String phone;
    public String market_plaza;
    public String city;
    public String lat;
    public String longi;
    public String availablility;


    public String getPrice() {
        return price;
    }

    public String price;

    public String getOldprice() {
        return oldprice;
    }

    public String oldprice;


    public ShopDetail(String id,String shop_name,String location,String link,String phone,String availablility,String oldprice,String price,String market_plaza, String city, String lat, String longi) {

        this.id = id;
        this.link = link;
        this.availablility = availablility;
        this.shop_name =  shop_name;
        this.phone = phone;
        this.location = location;

        this.oldprice = oldprice;
        this.price  = price;
        this.market_plaza = market_plaza;
        this.city = city;
        this.lat = lat;
        this.longi = longi;
    }

}
