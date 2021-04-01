package com.ivan.tsiupa;

public class Product {

private String title;
private String price;
private String discount;
private String seller;
private String state;
private String quantitySold;
private String departure;
private String deliveryPrice;

    public Product(String title, String price, String discount, String seller, String state, String quantitySold, String departure, String deliveryPrice) {
        this.title = title;
        this.price = price;
        this.discount = discount;
        this.seller = seller;
        this.state = state;
        this.quantitySold = quantitySold;
        this.departure = departure;
        this.deliveryPrice = deliveryPrice;
    }

    @Override
    public String toString() {
        return "Title = '" + (!title.equals("") ? title : "unknow") + '\'' +
                ","  + "\n" + "price= '" + (!price.equals("") ? price : "unknow") + '\'' +
                ","  + "\n" +    "discount= '" + (!discount.equals("") ? discount : "unknow") +
                ","  + "\n" +  "discount= '" + (!discount.equals("") ? discount : "unknow") + '\'' +
                ","  + "\n" +"seller= '" + (!seller.equals("") ? seller : "unknow") + '\'' +
                ","  + "\n" + "state= '" + (!state.equals("") ? state : "unknow") + '\'' +
                ","  + "\n" + "Quantity Sold='" + (!quantitySold.equals("") ? quantitySold : "unknow") + '\'' +
                ","  + "\n" + "Departure='" + (!departure.equals("") ? departure  : "unknow" )+ '\'' +
                ","  + "\n" +"deliveryPrice= '" + (!deliveryPrice.equals("") ? deliveryPrice : "unknow") + '\'' +
                '}';
    }
}
