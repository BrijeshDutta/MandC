package muffsandchocss.com.utility;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import muffsandchocss.com.mandc.R;

/**
 * Created by Rini Banerjee on 07-06-2017.
 */

public class DeliveryDatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String deliveryDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

        Toast.makeText(getActivity(),deliveryDate,Toast.LENGTH_SHORT).show();

        ((Button) getActivity().findViewById(R.id.btnDeliveryDate)).setText(deliveryDate);

    }
}
