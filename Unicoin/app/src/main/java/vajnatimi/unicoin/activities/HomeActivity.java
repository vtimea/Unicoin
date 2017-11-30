package vajnatimi.unicoin.activities;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
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

public class HomeActivity extends AppCompatActivity implements TransactionTypeFragment.TransactionTypeListener, TransactionListener{
    private String[] menuItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView rv;

    //private PieChart chartHoliday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle(getString(R.string.title_home));

        //chartHoliday = (PieChart) findViewById(R.id.chartIncomeExpense);

        menuItems = getResources().getStringArray(R.array.menu_items_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) this.findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, menuItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
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

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(mDrawerToggle);

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
//        Transaction2.deleteAll(Transaction2.class);

        //Recycler view inicializálása
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter_HOME rva = new RVAdapter_HOME();
        rv.setAdapter(rva);
    }

    private void showTransactionTypeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TransactionTypeFragment transactionTypeFragment = TransactionTypeFragment.newInstance();
        transactionTypeFragment.show(fm, "fragment_transaction_type");
    }

    private void showAddExpenseDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddExpenseFragment addExpenseFragment = AddExpenseFragment.newInstance(this);
        addExpenseFragment.show(fm, "dialog_add_expense");
    }

    private void showAddIncomeDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddIncomeFragment addIncomeFragment = AddIncomeFragment.newInstance(this);
        addIncomeFragment.show(fm, "dialog_add_income");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Transaction2.deleteAll(Transaction2.class);
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFinishChoosing(String transactionType) {
        if(transactionType.equals(getString(R.string.expense))){
            showAddExpenseDialog();
        } else if(transactionType.equals(getString(R.string.income))){
            showAddIncomeDialog();
        }
    }

    @Override
    public void update() {
        RVAdapter_HOME adapter = (RVAdapter_HOME) rv.getAdapter();
        adapter.update();
    }

    /*DRAWER STUFF*/
    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        drawerList.setItemChecked(position, true);
        Intent intent = new Intent();
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
                //TODO
                //intent = new Intent();
                b = true;
                Toast t = Toast.makeText(this, "Nothing to see here.", Toast.LENGTH_SHORT);
                t.show();
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

//    private void loadTransactions(){
//        List<PieEntry> entries = new ArrayList<>();
//        List<Transaction2> transactions = Transaction2.listAll(Transaction2.class);
//        int num_expenses = 0;
//        int num_incomes = 0;
//        for(int i = 0; i < transactions.size(); ++i){
//
//        }
//
//
//
//    }

}
