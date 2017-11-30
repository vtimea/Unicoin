package vajnatimi.unicoin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.TransactionListener;
import vajnatimi.unicoin.adapters.RVAdapter_EXPENSES;
import vajnatimi.unicoin.adapters.RVAdapter_INCOMES;

public class SlidePageRecurrFragment extends Fragment{
    private View rv;
    private RecyclerView recyclerView;
    private boolean firstRun= true;
    boolean isExpense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rv = (ViewGroup) inflater.inflate(R.layout.fragment_slide_page_recurr, container, false);
        isExpense = getArguments().getBoolean("isExpense");

        recyclerView = (RecyclerView) rv.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(llm);

        if(isExpense){
            RVAdapter_EXPENSES rva = new RVAdapter_EXPENSES();
            rva.updateByRecurr();
            recyclerView.setAdapter(rva);

        } else {
            RVAdapter_INCOMES rva = new RVAdapter_INCOMES();
            rva.updateByRecurr();
            recyclerView.setAdapter(rva);
        }

        return rv;
    }

    public static SlidePageRecurrFragment newInstance(boolean isExpense) {
        SlidePageRecurrFragment f = new SlidePageRecurrFragment();
        Bundle b = new Bundle();
        b.putBoolean("isExpense", isExpense);
        f.setArguments(b);
        return f;
    }

    public void update() {
        if(isExpense){
            ((RVAdapter_EXPENSES) recyclerView.getAdapter()).updateByRecurr();
        } else {
            ((RVAdapter_INCOMES) recyclerView.getAdapter()).updateByRecurr();
        }
    }
}
