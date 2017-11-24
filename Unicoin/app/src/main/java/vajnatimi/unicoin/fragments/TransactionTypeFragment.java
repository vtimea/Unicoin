package vajnatimi.unicoin.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import vajnatimi.unicoin.R;

public class TransactionTypeFragment extends DialogFragment{
    private Button btnExpense;
    private Button btnIncome;

    public interface TransactionTypeListener{
        void onFinishChoosing(String transactionType);
    }

    public TransactionTypeFragment(){}

    public static TransactionTypeFragment newInstance() {
        TransactionTypeFragment frag = new TransactionTypeFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_type, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnExpense = (Button) view.findViewById(R.id.btnExpense);
        btnIncome = (Button) view.findViewById(R.id.btnIncome);

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionTypeListener listener = (TransactionTypeListener) getActivity();
                listener.onFinishChoosing(btnExpense.getText().toString());
                dismiss();
            }
        });

        btnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionTypeListener listener = (TransactionTypeListener) getActivity();
                listener.onFinishChoosing(btnIncome.getText().toString());
                dismiss();
            }
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }
}
