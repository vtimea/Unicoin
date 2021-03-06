package vajnatimi.unicoin.fragments;

import android.app.Dialog;
import android.app.Fragment;
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
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.TransactionListener;
import vajnatimi.unicoin.adapters.RVAdapter_EXPENSES;
import vajnatimi.unicoin.adapters.RVAdapter_HOME;
import vajnatimi.unicoin.adapters.RVAdapter_INCOMES;
import vajnatimi.unicoin.model.Transaction2;

public class AddIncomeFragment extends DialogFragment{
    private static final String TITLE = "Add income";

    private EditText etItemName;
    private EditText etAmount;
    private CheckBox cbRecurring;
    private EditText etDate;
    private static ArrayList<TransactionListener> listeners = new ArrayList<>();

    public AddIncomeFragment(){}

    public static AddIncomeFragment newInstance(TransactionListener listener) {
        AddIncomeFragment frag = new AddIncomeFragment();
        frag.addListener(listener);
        return frag;
    }

    private void addListener(TransactionListener listener){
        for(int i = 0; i < listeners.size(); ++i){
            if(listeners.get(i) == listener)
                return;
        }
        listeners.add(listener);
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
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(etDate.getText().toString()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(TextUtils.isEmpty(etItemName.getText().toString())){
                            etItemName.setError(getString(R.string.error_missing_item_name));
                            return;
                        }
                        else if(TextUtils.isEmpty(etAmount.getText().toString())){
                            etAmount.setError(getString(R.string.error_missing_price));
                            return;
                        }
                        else if(Integer.parseInt(etAmount.getText().toString()) == 0){
                            etAmount.setError(getString(R.string.error_amout_cant_be_zero));
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
                        else if(c.get(Calendar.DAY_OF_MONTH) > 28 && cbRecurring.isChecked()){
                            etDate.setError(getString(R.string.error_recurring_invalid_date));
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
            e.printStackTrace();
        }

        boolean recurr = cbRecurring.isChecked();

        Transaction2 transaction = new Transaction2(name, amount, date, recurr);

        //csak akkor ment ha még nincs ilyen record
        int r = recurr ? 1 : 0;
        List<Transaction2> l = Select.from(Transaction2.class).where(Condition.prop("name").eq(name),
                Condition.prop("amount").eq(amount),
                Condition.prop("recurr").eq(r)).list();
        for(int i = 0; i < l.size(); ++i){
            if(l.get(i).getDate().compareTo(transaction.getDate()) == 0) {
                Toast.makeText(getContext(), "This transaction already exists.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        transaction.save();

        RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.recyclerView);


        for(int i = 0; i < listeners.size(); ++i){
            listeners.get(i).update();
        }
    }
}
