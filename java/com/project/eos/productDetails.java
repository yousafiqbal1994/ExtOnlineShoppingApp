package com.project.eos;

/**
 * Created by YouCaf Iqbal on 2/28/2017.
 */

public class productDetails {
    public String ID;
    public String title;
    public String image;

    public String getBrandName() {
        return brandName;
    }

    public String brandName;

    public productDetails(String ID,String title,String image, String brandName) {
        this.ID = ID;
        this.title=title;
        this.image = image;
        this.brandName = brandName;
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
}
