package r2zzb4.unicoin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.security.Timestamp;

import r2zzb4.unicoin.model.Expense;

public class AddExpenseFragment extends DialogFragment{
    private EditText etItemName;
    private EditText etPrice;
    private EditText etDate;
    private CheckBox cbRecurring;
    private Spinner spCategory;

    public AddExpenseFragment(){}

    public static AddExpenseFragment newInstance(String title) {
        AddExpenseFragment frag = new AddExpenseFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_expense, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etItemName = (EditText) view.findViewById(R.id.etItemName);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        etDate = (EditText) view.findViewById(R.id.etDate);
        cbRecurring = (CheckBox) view.findViewById(R.id.cbRecurring);
        spCategory = (Spinner) view.findViewById(R.id.spCategory);

        // Fetch arguments from bundle and set title
        //String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(R.string.add_expense);

        etItemName.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        String title = getArguments().getString("title");
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//        alertDialogBuilder.setTitle(title);
//        alertDialogBuilder.setPositiveButton(R.string.ok,  new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //// TODO: 2017. 11. 21.
//                Expense expense = new Expense(etItemName.getText(), etPrice.getText(), Timestamp,0);
//            }
//        });
//        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (dialog != null /*&& dialog.isShowing()*/) {
//                    dialog.dismiss();
//            }
//            }
//
//        });
//
//        return alertDialogBuilder.create();
//    }
}
