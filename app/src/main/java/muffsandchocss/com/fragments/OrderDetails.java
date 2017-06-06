package muffsandchocss.com.fragments;

/**
 * Created by Rini Banerjee on 06-06-2017.
 */

public class OrderDetails {
    public String dishType;
    public String choclateType;
    public String dryFruits;
    public int quantity;
    public String deliveryDate;
    public String specialPreComments;
    public OrderDetails() {

    }

    public OrderDetails(String dishType, String choclateType, String dryFruits, int quantity, String deliveryDate,String specialPreComments) {
        this.dishType = dishType;
        this.choclateType = choclateType;
        this.dryFruits = dryFruits;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
        this.specialPreComments = specialPreComments;
    }
}
