package vajnatimi.unicoin.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.TransactionListener;
import vajnatimi.unicoin.adapters.RVAdapter_HOME;
import vajnatimi.unicoin.fragments.AddExpenseFragment;
import vajnatimi.unicoin.fragments.AddIncomeFragment;
import vajnatimi.unicoin.fragments.TransactionTypeFragment;
import vajnatimi.unicoin.model.Transaction2;

public class HomeActivity extends AppCompatActivity implements TransactionTypeFragment.TransactionTypeListener, TransactionListener, RVAdapter_HOME.OnItemLongClickListener {
    private String[] menuItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView rv;

    private PieChart chart;

    private static boolean firstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(firstTime){
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firstTime = false;
        }
        setTheme(R.style.AppTheme);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getString(R.string.title_home));

        /*<----------------DRAWER STUFF---------------->*/
        menuItems = getResources().getStringArray(R.array.menu_items_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) this.findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, menuItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(mDrawerToggle);
        /*<----------------/DRAWER SRUFF---------------->*/

        //Floating Action gomb inicializálása
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTransactionTypeDialog();
            }
        });

        //Sugar ORM inicializálása
        SugarContext.init(this);

        //Recycler view inicializálása
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter_HOME rva = new RVAdapter_HOME(this);
        rv.setAdapter(rva);

        //Ismétlődő tranzakciók felvétele
        Calendar c = Calendar.getInstance();
        rva.addRecurringTransactions(c.getTime());

        //Kördiagram inicializálása
        chart = (PieChart) findViewById(R.id.chartHoliday);
        loadTransactions();
    }

    /*<----------------DRAWER STUFF---------------->*/
    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        drawerList.setItemChecked(position, true);
        Intent intent;
        boolean b = false;
        switch (position){
            case 0:
                intent = new Intent(this, HomeActivity.class);
                break;
            case 1:
                intent = new Intent(this, ExpensesActivity.class);
                break;
            case 2:
                intent = new Intent(this, IncomesActivity.class);
                break;
            default:
                intent = new Intent(this, SettingsActivity.class);
                break;
        }
        if(!b){
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }

        setTitle(menuItems[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        return super.onPrepareOptionsMenu(menu);
    }
    /*<----------------/DRAWER STUFF---------------->*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Összes tranzakció törlése
        if (id == R.id.action_settings) {
            Transaction2.deleteAll(Transaction2.class);
            update();
            return true;
        }

        //
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //A fragment a tranzakció típusának kiválasztásához
    private void showTransactionTypeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TransactionTypeFragment transactionTypeFragment = TransactionTypeFragment.newInstance();
        transactionTypeFragment.show(fm, "fragment_transaction_type");
    }

    //Meghívódik amikor a user kiválasztotta a tranzakció típusát
    @Override
    public void onFinishChoosing(String transactionType) {
        if(transactionType.equals(getString(R.string.expense))){
            showAddExpenseDialog();
        } else if(transactionType.equals(getString(R.string.income))){
            showAddIncomeDialog();
        }
    }

    //Kiadás hozzáadása
    private void showAddExpenseDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddExpenseFragment addExpenseFragment = AddExpenseFragment.newInstance(this);
        addExpenseFragment.show(fm, "dialog_add_expense");
    }

    //Bevétel hozzáadása
    private void showAddIncomeDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddIncomeFragment addIncomeFragment = AddIncomeFragment.newInstance(this);
        addIncomeFragment.show(fm, "dialog_add_income");
    }

    @Override
    public boolean onItemLongClicked(int position) {
        update();
        return true;
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    //tranzakciók betöltése a kördiagramhoz
    private void loadTransactions() {
        List<Transaction2> transactions = ((RVAdapter_HOME) rv.getAdapter()).getSortedTransactions();

        int i = 0;
        long incomes = 0;
        long expenses = 0;
        if(transactions.isEmpty()) return;
        while (i < transactions.size() && transactions.get(i).getYear() == Calendar.getInstance().get(Calendar.YEAR) && transactions.get(i).getMonth() == Calendar.getInstance().get(Calendar.MONTH)+1){
            if(transactions.get(i).getAmount() > 0)
                incomes += transactions.get(i).getAmount();
            else
                expenses -= transactions.get(i).getAmount();
            i++;
        }

        List<PieEntry> entries = new ArrayList<>();
        PieDataSet dataSet = new PieDataSet(entries, getString(R.string.pie_chart_label));

        if(incomes != 0){
            entries.add(new PieEntry(incomes, getString(R.string.title_incomes)));
        }
        if(expenses != 0){
            entries.add(new PieEntry(expenses, getString(R.string.title_expenses)));
        }

        dataSet.setColors(ColorTemplate.rgb(getString(R.string.colorIncome)), ColorTemplate.rgb(getString(R.string.colorExpense)));
        dataSet.setDrawValues(false);

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }

    @Override
    public void update() {
        RVAdapter_HOME adapter = (RVAdapter_HOME) rv.getAdapter();
        adapter.update();
        loadTransactions();
    }
}
