package muffsandchocss.com.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import muffsandchocss.com.mandc.R;

/**
 * Created by Rini Banerjee on 08-06-2017.
 */

public class OrderDetailsList extends ArrayAdapter<OrderDetails> {

    private Activity context;
    private List<OrderDetails> orderDetailsList;

    public OrderDetailsList(Activity context, List<OrderDetails> orderDetailsList) {
        super(context, R.layout.list_oder_summary,orderDetailsList);
        this.context = context;
        this.orderDetailsList = orderDetailsList;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listViewItem = layoutInflater.inflate(R.layout.list_oder_summary,null,true);
        TextView textViewOrderDetails = (TextView) listViewItem.findViewById(R.id.textViewOrderDetails);


        OrderDetails orderDetails = orderDetailsList.get(position);
        textViewOrderDetails.setText("Order Summary : "+(orderDetails.dishType)+"|" + (orderDetails.choclateType) + "|" + (orderDetails.quantity)+"| Price");


        return listViewItem;
    }
}
