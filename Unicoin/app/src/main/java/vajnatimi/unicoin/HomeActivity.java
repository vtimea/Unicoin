package vajnatimi.unicoin;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

import vajnatimi.unicoin.model.Transaction2;

public class HomeActivity extends AppCompatActivity implements TransactionTypeFragment.TransactionTypeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTransactionTypeDialog();
            }
        });

        SugarContext.init(this);

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVAdapter rva = new RVAdapter();
        rv.setAdapter(rva);
    }

    private void showTransactionTypeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TransactionTypeFragment transactionTypeFragment = TransactionTypeFragment.newInstance();
        transactionTypeFragment.show(fm, "fragment_transaction_type");
    }

    private void showAddExpenseDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddExpenseFragment addExpenseFragment = AddExpenseFragment.newInstance();
        addExpenseFragment.show(fm, "dialog_add_expense");
    }

    private void showAddIncomeDialog(){
        FragmentManager fm = getSupportFragmentManager();
        AddIncomeFragment addIncomeFragment = AddIncomeFragment.newInstance();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishChoosing(String transactionType) {
        if(transactionType.equals(getString(R.string.expense))){
            showAddExpenseDialog();
        } else if(transactionType.equals(getString(R.string.income))){
            showAddIncomeDialog();
        }
    }
}
