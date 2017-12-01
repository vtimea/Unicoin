package vajnatimi.unicoin.activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.util.Calendar;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import vajnatimi.unicoin.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    //DRAWER
    private String[] menuItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    //DAILY NOTIFICATIONS
    private Switch swDailyNot;
    private EditText etDailyNotTime;

    //WEEKLY NOTIFICATIONS
    private Switch swWeeklyNot;
    private Spinner spWeeklyNotDay;
    private EditText etWeeklyNotTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle(getString(R.string.settings_title));

        /*<----------------DRAWER STUFF---------------->*/
        menuItems = getResources().getStringArray(R.array.menu_items_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) this.findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, menuItems));
        drawerList.setOnItemClickListener(new SettingsActivity.DrawerItemClickListener());

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
        /*<----------------/DRAWER STUFF---------------->*/

        /*<----------------DAILY NOTIFICATIONS---------------->*/
        etDailyNotTime = (EditText) this.findViewById(R.id.etDailyNotTime);
        etDailyNotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        if(0 < minute && minute < 10) {
                            etDailyNotTime.setText(hourOfDay + ":0" + minute);
                            editor.putString(getString(R.string.prefs_dailynottime), String.valueOf(hourOfDay) + ":0" + String.valueOf(minute));
                        }
                        else {
                            etDailyNotTime.setText(hourOfDay + ":" + minute);
                            editor.putString(getString(R.string.prefs_dailynottime), String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
                        }
                        editor.apply();
                    }
                }, hour, minute, true);
                timePicker.setTitle(getString(R.string.title_selectTime));
                timePicker.show();
            }
        });

        swDailyNot = (Switch) findViewById(R.id.swDailyNot);
        swDailyNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = swDailyNot.isChecked();

                sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(getString(R.string.prefs_dailynotenabled), String.valueOf(checked));
                editor.apply();

                etDailyNotTime.setClickable(checked);
                etDailyNotTime.setEnabled(checked);
            }
        });
        /*<----------------/DAILY NOTIFICATIONS---------------->*/

        /*<----------------WEEKLY NOTIFICATIONS---------------->*/
        spWeeklyNotDay = (Spinner) findViewById(R.id.spWeeklyNotDay);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.days_of_week_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeeklyNotDay.setEnabled(false);
        spWeeklyNotDay.setAdapter(adapter);
        spWeeklyNotDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(getString(R.string.prefs_weeklynotday), spWeeklyNotDay.getSelectedItem().toString());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etWeeklyNotTime = (EditText) findViewById(R.id.etWeeklyNotTime);
        etWeeklyNotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        if(0 < minute && minute < 10) {
                            String s = hourOfDay + ":" + "0" + minute;
                            etWeeklyNotTime.setText(s);
                            editor.putString(getString(R.string.prefs_weeklynottime), s);
                        }
                        else {
                            String s = hourOfDay + ":" + minute;
                            etWeeklyNotTime.setText(s);
                            editor.putString(getString(R.string.prefs_weeklynottime), s);
                        }
                        editor.apply();
                    }
                }, hour, minute, true);
                timePicker.setTitle(getString(R.string.title_selectTime));
                timePicker.show();
            }
        });

        swWeeklyNot = (Switch) findViewById(R.id.swWeeklyNot);
        swWeeklyNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = swWeeklyNot.isChecked();

                sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(getString(R.string.prefs_weeklyNotEnabled), Boolean.toString(checked));
                editor.apply();

                etWeeklyNotTime.setClickable(checked);
                etWeeklyNotTime.setEnabled(checked);
                spWeeklyNotDay.setClickable(checked);
                spWeeklyNotDay.setEnabled(checked);
            }
        });
        /*<----------------/WEEKLY NOTIFICATIONS---------------->*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

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
}
