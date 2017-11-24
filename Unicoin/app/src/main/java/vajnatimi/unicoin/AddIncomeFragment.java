package vajnatimi.unicoin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vajnatimi.unicoin.model.Transaction2;

public class AddIncomeFragment extends DialogFragment{
    private static final String TITLE = "Add income";

    private EditText etItemName;
    private EditText etAmount;
    private CheckBox cbRecurring;
    private EditText etDate;

    public AddIncomeFragment(){}

    public static AddIncomeFragment newInstance() {
        AddIncomeFragment frag = new AddIncomeFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_income, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(TITLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_add_income, null);

        etItemName = (EditText) view.findViewById(R.id.etItemName);
        etAmount = (EditText) view.findViewById(R.id.etAmount);
        cbRecurring = (CheckBox) view.findViewById(R.id.cbRecurring);

        etDate = (EditText) view.findViewById(R.id.etDate);
        etDate.setText(currentDate());

        builder.setView(view)
                .setPositiveButton(R.string.done, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddIncomeFragment.this.getDialog().cancel();
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
                        else if(TextUtils.isEmpty(etAmount.getText().toString())){
                            etAmount.setError(getString(R.string.error_missing_price));
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
                            saveIncome();
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

    private void saveIncome(){
        String name = etItemName.getText().toString();
        int amount = Integer.parseInt(etAmount.getText().toString());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = df.parse(etDate.getText().toString());
        } catch (ParseException e) {
            //TODO
        }

        boolean recurr = cbRecurring.isChecked();

        Transaction2 transaction = new Transaction2(name, amount, date, recurr);
        transaction.save();

        RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        RVAdapter rva = (RVAdapter) rv.getAdapter();
        rva.update();
    }
}
