package muffsandchocss.com.fragments;

/**
 * Created by Rini Banerjee on 06-06-2017.
 */

public class OrderDetails {
    String dishType;
    String dryFruits;
    int quantity;
    String deliveryDate;
    public OrderDetails() {

    }

    public OrderDetails(String dishType, String dryFruits, int quantity, String deliveryDate) {
        this.dishType = dishType;
        this.dryFruits = dryFruits;
        this.quantity = quantity;
        this.deliveryDate = deliveryDate;
    }
}
