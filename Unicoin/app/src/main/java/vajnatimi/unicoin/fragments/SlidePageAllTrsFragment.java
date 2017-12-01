package vajnatimi.unicoin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.TransactionListener;
import vajnatimi.unicoin.adapters.RVAdapter_EXPENSES;
import vajnatimi.unicoin.adapters.RVAdapter_INCOMES;
import vajnatimi.unicoin.model.Transaction2;

public class SlidePageAllTrsFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    private View rv;
    private Spinner spYear;
    private Spinner spMonth;
    private RecyclerView recyclerView;
    private boolean firstRun= true;
    boolean isExpense;

    private PieChart chart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        isExpense = getArguments().getBoolean("isExpense");

        if(isExpense) {
            rv = (ViewGroup) inflater.inflate(R.layout.fragment_slide_page_all_trs_expense, container, false);
            chart = (PieChart) rv.findViewById(R.id.chartHoliday);
        }
        else
            rv = (ViewGroup) inflater.inflate(R.layout.fragment_slide_page_all_trs_income, container, false);

        spYear = (Spinner) rv.findViewById(R.id.spYear);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, getSpAdapterArray_Year());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapter);
        spYear.setOnItemSelectedListener(this);

        spMonth = (Spinner) rv.findViewById(R.id.spMonth);
        spMonth.setOnItemSelectedListener(this);

        recyclerView = (RecyclerView) rv.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(llm);
        if(isExpense){
            RVAdapter_EXPENSES rva = new RVAdapter_EXPENSES();
            recyclerView.setAdapter(rva);
        } else {
            RVAdapter_INCOMES rva = new RVAdapter_INCOMES();
            recyclerView.setAdapter(rva);
        }

        //Floating Action gomb inicializálása
        FloatingActionButton fab = (FloatingActionButton) rv.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getChildFragmentManager();
                if(isExpense){
                    AddExpenseFragment addExpenseFragment = AddExpenseFragment.newInstance((TransactionListener) getActivity());
                    addExpenseFragment.show(fm, "dialog_add_expense");
                }
                else {
                    AddIncomeFragment addIncomeFragment = AddIncomeFragment.newInstance((TransactionListener) getActivity());
                    addIncomeFragment.show(fm, "dialog_add_income");
                }
            }
        });

        return rv;
    }

    public static SlidePageAllTrsFragment newInstance(boolean isExpense) {
        SlidePageAllTrsFragment f = new SlidePageAllTrsFragment();
        Bundle b = new Bundle();
        b.putBoolean("isExpense", isExpense);
        f.setArguments(b);
        return f;
    }

    private ArrayList<Integer> getSpAdapterArray_Year(){
        List<Transaction2> transactions = Transaction2.listAll(Transaction2.class);
        Set<Integer> years = new HashSet<>();

        if(isExpense){
            for(int i = 0; i < transactions.size(); ++i) {
                if(transactions.get(i).getAmount() < 0){
                    years.add(transactions.get(i).getYear());
                }
            }
        } else {
            for(int i = 0; i < transactions.size(); ++i) {
                if(transactions.get(i).getAmount() > 0){
                    years.add(transactions.get(i).getYear());
                }
            }
        }
        ArrayList<Integer> res = new ArrayList<Integer>(years);
        Collections.sort(res);
        Collections.reverse(res);
        return res;
    }

    private ArrayList<Integer> getSpAdapterArray_Month(int YEAR){
        List<Transaction2> transactions = Transaction2.listAll(Transaction2.class);
        Set<Integer> months = new HashSet<>();

        if(isExpense){
            for(int i = 0; i < transactions.size(); ++i) {
                if(transactions.get(i).getYear() == YEAR && transactions.get(i).getAmount() < 0){
                    months.add(transactions.get(i).getMonth());
                }
            }
        } else {
            for(int i = 0; i < transactions.size(); ++i) {
                if(transactions.get(i).getYear() == YEAR && transactions.get(i).getAmount() > 0){
                    months.add(transactions.get(i).getMonth());
                }
            }
        }
        ArrayList<Integer> res = new ArrayList<Integer>(months);
        Collections.sort(res);
        Collections.reverse(res);
        return res;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] months = getResources().getStringArray(R.array.months_array);
        switch (parent.getId()){
            case R.id.spYear:
                spMonth = (Spinner) rv.findViewById(R.id.spMonth);
                ArrayList<Integer> temp = getSpAdapterArray_Month(Integer.parseInt(spYear.getSelectedItem().toString())); //hónapok: 1,2
                ArrayList<String> selectedMonths = new ArrayList<>(); //jan, febr
                for(int i = 0; i < temp.size(); ++i){
                    selectedMonths.add(months[temp.get(i)-1]);
                }
                Collections.reverse(selectedMonths);
                spMonth = (Spinner) rv.findViewById(R.id.spMonth);
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, selectedMonths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMonth.setAdapter(adapter);
                spMonth.setOnItemSelectedListener(this);
                break;

            case R.id.spMonth:
                int y = Integer.parseInt(spYear.getSelectedItem().toString());
                int m = -1;
                for(int i = 0; i < months.length; ++i){
                    if(months[i].equals(spMonth.getSelectedItem().toString())){
                        m = i+1;
                        break;
                    }
                }
                if(isExpense){
                    if(!firstRun)((RVAdapter_EXPENSES) recyclerView.getAdapter()).update(y, m);
                } else {
                    if(!firstRun)((RVAdapter_INCOMES) recyclerView.getAdapter()).update(y, m);
                }
                loadTransactions(y, m);
                break;
        }
        firstRun = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void update() {
        Spinner spMonth = (Spinner) rv.findViewById(R.id.spMonth);
        Spinner spYear = (Spinner) rv.findViewById(R.id.spYear);

        String[] months = getResources().getStringArray(R.array.months_array);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, getSpAdapterArray_Year());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapter);

        spMonth = (Spinner) rv.findViewById(R.id.spMonth);
        ArrayList<Integer> temp = getSpAdapterArray_Month(Integer.parseInt(spYear.getSelectedItem().toString())); //hónapok: 1,2
        ArrayList<String> selectedMonths = new ArrayList<>(); //jan, febr
        for (int i = 0; i < temp.size(); ++i) {
            selectedMonths.add(months[temp.get(i) - 1]);
        }
        Collections.reverse(selectedMonths);
        spMonth = (Spinner) rv.findViewById(R.id.spMonth);
        adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, selectedMonths);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMonth.setAdapter(adapter);
        spMonth.setOnItemSelectedListener(this);

        int y = Integer.parseInt(spYear.getSelectedItem().toString());
        int m = -1;
        for (int i = 0; i < months.length; ++i) {
            if (months[i].equals(spMonth.getSelectedItem().toString())) {
                m = i + 1;
                break;
            }
        }
        if (isExpense) {
            if (!firstRun) ((RVAdapter_EXPENSES) recyclerView.getAdapter()).update(y, m);
        } else {
            if (!firstRun) ((RVAdapter_INCOMES) recyclerView.getAdapter()).update(y, m);
        }

        firstRun = false;
    }

    private void loadTransactions(int y, int m) {
        List<Transaction2> trs;
        if(isExpense){
            trs = ((RVAdapter_EXPENSES) recyclerView.getAdapter()).update(y, m);
        } else {
            return;
        }

        long[] categories = new long[9];

        for(int i = 0; i < trs.size(); ++i){
            switch (trs.get(i).getCategory()){
                case UNCATEGORIZED:
                    if(isExpense){categories[0] -= trs.get(i).getAmount(); break;}
                    categories[0] += trs.get(i).getAmount();
                    break;
                case CLOTHING:
                    if(isExpense){categories[1] -= trs.get(i).getAmount(); break;}
                    categories[1] += trs.get(i).getAmount();
                    break;
                case DEBT:
                    if(isExpense){categories[2] -= trs.get(i).getAmount(); break;}
                    categories[2] += trs.get(i).getAmount();
                    break;
                case ENTERTAINMENT:
                    if(isExpense){categories[3] -= trs.get(i).getAmount(); break;}
                    categories[3] += trs.get(i).getAmount();
                    break;
                case FOOD:
                    if(isExpense){categories[4] -= trs.get(i).getAmount(); break;}
                    categories[4] += trs.get(i).getAmount();
                    break;
                case HOUSING:
                    if(isExpense){categories[5] -= trs.get(i).getAmount(); break;}
                    categories[5] += trs.get(i).getAmount();
                    break;
                case INSURANCE:
                    if(isExpense){categories[6] -= trs.get(i).getAmount(); break;}
                    categories[6] += trs.get(i).getAmount();
                    break;
                case MEDICAL:
                    if(isExpense){categories[7] -= trs.get(i).getAmount(); break;}
                    categories[7] += trs.get(i).getAmount();
                    break;
                default:
                    if(isExpense){categories[8] -= trs.get(i).getAmount(); break;}
                    categories[8] += trs.get(i).getAmount();
                    break;
            }
        }

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for(int i = 0; i < categories.length; ++i){
            if(categories[i] != 0) {
                entries.add(new PieEntry(categories[i], getResources().getStringArray(R.array.categories_array)[i]));
                colors.add(ColorTemplate.rgb(getResources().getStringArray(R.array.colors_array)[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }
}
