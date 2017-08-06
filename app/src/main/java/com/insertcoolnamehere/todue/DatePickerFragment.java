package com.insertcoolnamehere.todue;

import android.app.Dialog;
import  java.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

/**
 * Creates a dialog to pick dates
 */

public class DatePickerFragment extends DialogFragment {
    private DatePickerDialog.OnDateSetListener mListener;

    public DatePickerFragment setListener(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
        return this;
    }

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // use today's date as the default
        final Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), mListener, y, m, d);
    }
}
