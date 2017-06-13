package muffsandchocss.com.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        final View listViewItem = layoutInflater.inflate(R.layout.list_oder_summary,null,true);
        final TextView textViewHeader = (TextView) listViewItem.findViewById(R.id.textViewHeader);
        final TextView textViewOrderDetails = (TextView) listViewItem.findViewById(R.id.textViewOrderDetails);


        final OrderDetails orderDetails = orderDetailsList.get(position);
        textViewHeader.setText("Order Id");
        textViewOrderDetails.setText(orderDetails.getOrderId());
        textViewOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Order cliecked"+ orderDetails.getOrderId(),Toast.LENGTH_LONG).show();
            }
        });


        return listViewItem;
    }
}
