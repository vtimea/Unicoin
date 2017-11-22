package vajnatimi.unicoin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaCodec;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddExpenseFragment extends DialogFragment{
    private static final String TITLE = "Add expense";

    private EditText etItemName;
    private EditText etPrice;
    private EditText etDate;
    private CheckBox cbRecurring;
    private Spinner spCategory;

    public AddExpenseFragment(){}

    public static AddExpenseFragment newInstance() {
        AddExpenseFragment frag = new AddExpenseFragment();
        Bundle args = new Bundle();
        //args.putString("title", title);
        frag.setArguments(args);
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

        etItemName = (EditText) view.findViewById(R.id.etItemName);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        etDate = (EditText) view.findViewById(R.id.etDate);
        cbRecurring = (CheckBox) view.findViewById(R.id.cbRecurring);
        spCategory = (Spinner) view.findViewById(R.id.spCategory);

        // Fetch arguments from bundle and set title
        //String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(R.string.add_expense);
    }

    //TODO
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add_expense, null))
            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("mt", "CLICK --> Positive button");
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("mt", "CLICK --> Negative button");
                    AddExpenseFragment.this.getDialog().cancel();

                }
            });

        return builder.create();
    }
}
