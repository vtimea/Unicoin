package vajnatimi.unicoin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.media.MediaCodec;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import vajnatimi.unicoin.model.Expense;

public class AddExpenseFragment extends DialogFragment{
    private static final String TITLE = "Add expense";

    private EditText etItemName;
    private EditText etPrice;
    private CheckBox cbRecurring;
    private Spinner spCategory;
    private EditText etDate;

    public AddExpenseFragment(){}

    public static AddExpenseFragment newInstance() {
        AddExpenseFragment frag = new AddExpenseFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_expense, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.add_expense);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_expense, null);

        etItemName = (EditText) view.findViewById(R.id.etItemName);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        cbRecurring = (CheckBox) view.findViewById(R.id.cbRecurring);
        spCategory = (Spinner) view.findViewById(R.id.spCategory);

        etDate = (EditText) view.findViewById(R.id.etDate);
        etDate.setText(currentDate());

        builder.setView(view)
            .setPositiveButton(R.string.done, null)
            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddExpenseFragment.this.getDialog().cancel();
                }
            });

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(TextUtils.isEmpty(etItemName.getText().toString())){
                            etItemName.setError(getString(R.string.error_missing_item_name));
                            return;
                        }
                        else if(TextUtils.isEmpty(etPrice.getText().toString())){
                            etPrice.setError(getString(R.string.error_missing_price));
                            return;
                        }
                        else if(TextUtils.isEmpty(etDate.getText().toString())){
                            etDate.setError(getString(R.string.error_missing_date));
                            return;
                        }
                        else if(!etDate.getText().toString()
                                .matches("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$")){
                            etDate.setError(getString(R.string.error_invalid_date) +
                                    "");
                            return;
                        }
                        else{
                            saveExpense();
                            dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    private String currentDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    private void saveExpense(){
        String name = etItemName.getText().toString();
        int price = Integer.parseInt(etPrice.getText().toString());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(etDate.getText().toString());
        } catch (ParseException e) {
            //TODO
        }
        Expense expense = new Expense(name, price, date, 0);
        expense.save();
    }
}
