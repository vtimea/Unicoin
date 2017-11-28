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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.adapters.RVAdapter_EXPENSES;
import vajnatimi.unicoin.adapters.RVAdapter_HOME;
import vajnatimi.unicoin.adapters.RVAdapter_INCOMES;
import vajnatimi.unicoin.fragments.AddIncomeFragment;
import vajnatimi.unicoin.model.Transaction2;

public class IncomesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String[] menuItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Spinner spYear;
    private Spinner spMonth;
    private RecyclerView recyclerView;
    private boolean firstRun= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes);
        setTitle(getString(R.string.title_incomes));

        menuItems = getResources().getStringArray(R.array.menu_items_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) this.findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, menuItems));
        drawerList.setOnItemClickListener(new IncomesActivity.DrawerItemClickListener());

        spYear = (Spinner) this.findViewById(R.id.spYear);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getSpAdapterArray_Year());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spYear.setAdapter(adapter);
        spYear.setOnItemSelectedListener(this);

        spMonth = (Spinner) this.findViewById(R.id.spMonth);
        //spMonth.setOnItemSelectedListener(this);

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
                FragmentManager fm = getSupportFragmentManager();
                AddIncomeFragment addIncomeFragment = AddIncomeFragment.newInstance();
                addIncomeFragment.show(fm, "dialog_add_income");
            }
        });

        //Recycler view inicializálása
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        RVAdapter_INCOMES rva = new RVAdapter_INCOMES();
        recyclerView.setAdapter(rva);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        boolean monthSet = false;
        Spinner spinner = (Spinner) parent;
        String[] months = getResources().getStringArray(R.array.months_array);
        switch (parent.getId()){
            case R.id.spYear:
                spMonth = (Spinner) this.findViewById(R.id.spMonth);
                ArrayList<Integer> temp = getSpAdapterArray_Month(Integer.parseInt(spYear.getSelectedItem().toString())); //hónapok: 1,2
                ArrayList<String> selectedMonths = new ArrayList<>(); //jan, febr
                for(int i = 0; i < temp.size(); ++i){
                    selectedMonths.add(months[temp.get(i)-1]);
                }
                Collections.reverse(selectedMonths);
                spMonth = (Spinner) this.findViewById(R.id.spMonth);
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, selectedMonths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMonth.setAdapter(adapter);
                spMonth.setOnItemSelectedListener(this);

                //if(!firstRun)((RVAdapter_EXPENSES) recyclerView.getAdapter()).update(Integer.parseInt(spYear.getSelectedItem().toString()));
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
                if(!firstRun)((RVAdapter_INCOMES) recyclerView.getAdapter()).update(y, m);
                break;
        }
        firstRun = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
        // Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(position, true);
        Intent intent;
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
                intent = new Intent();
                //TODO: settings
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

        setTitle(menuItems[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    private ArrayList<Integer> getSpAdapterArray_Year(){
        List<Transaction2> transactions = Transaction2.listAll(Transaction2.class);
        Set<Integer> years = new HashSet<>();

        for(int i = 0; i < transactions.size(); ++i) {
            if(transactions.get(i).getAmount() > 0){
                years.add(transactions.get(i).getYear());
            }
        }
        return new ArrayList<Integer>(years);
    }

    private ArrayList<Integer> getSpAdapterArray_Month(int YEAR){
        List<Transaction2> transactions = Transaction2.listAll(Transaction2.class);
        Set<Integer> months = new HashSet<>();

        for(int i = 0; i < transactions.size(); ++i) {
            if(transactions.get(i).getYear() == YEAR && transactions.get(i).getAmount() > 0){
                months.add(transactions.get(i).getMonth());
            }
        }
        return new ArrayList<Integer>(months);
    }

}
