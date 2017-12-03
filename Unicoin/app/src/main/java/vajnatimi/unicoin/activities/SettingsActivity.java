package vajnatimi.unicoin.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.icu.util.Calendar;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import vajnatimi.unicoin.NotificationService;
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

    private int DAILY_REQUEST_CODE = 1;
    private int WEEKLY_REQUEST_CODE = 2;

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

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        /*<----------------DAILY NOTIFICATIONS---------------->*/
        etDailyNotTime = (EditText) this.findViewById(R.id.etDailyNotTime);
        etDailyNotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Beállítások kimentése
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        if(0 <= minute && minute < 10) {
                            etDailyNotTime.setText(hourOfDay + ":0" + minute);
                            editor.putString(getString(R.string.prefs_dailynottime), String.valueOf(hourOfDay) + ":0" + String.valueOf(minute));
                        }
                        else {
                            etDailyNotTime.setText(hourOfDay + ":" + minute);
                            editor.putString(getString(R.string.prefs_dailynottime), String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
                        }
                        editor.apply();

                        //Értesítés beállítása
                        String time;
                        String[] hourandmin;
                        Calendar cal;

                        time = etDailyNotTime.getText().toString();
                        hourandmin = time.split(":");
                        cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hourandmin[0]));
                        cal.set(Calendar.MINUTE, Integer.valueOf(hourandmin[1]));
                        Log.i("notif", cal.getTime().toString());
                        addAlarm(true, "daily", DAILY_REQUEST_CODE, cal);
                    }
                }, hour, minute, true);
                timePicker.setTitle(getString(R.string.title_selectTime));
                timePicker.show();
            }
        });
        //get values
        String dnt = sharedpreferences.getString(getString(R.string.prefs_dailynottime), getString(R.string.empty));
        if(!dnt.equals(getString(R.string.empty))){
            etDailyNotTime.setText(dnt);
        }


        swDailyNot = (Switch) findViewById(R.id.swDailyNot);
        swDailyNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Beállítások kimentése
                boolean checked = swDailyNot.isChecked();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(getString(R.string.prefs_dailynotenabled), String.valueOf(checked));
                editor.putString(getString(R.string.prefs_dailynottime), etDailyNotTime.getText().toString());
                editor.apply();

                //Többi elem aktíválása/inaktiválása
                etDailyNotTime.setClickable(checked);
                etDailyNotTime.setEnabled(checked);

                //Értesítés beállítása
                String time;
                String[] hourandmin;
                Calendar cal = Calendar.getInstance();
                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(checked){
                    time = etDailyNotTime.getText().toString();
                    hourandmin = time.split(":");
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourandmin[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(hourandmin[1]));
                    Log.i("notif", cal.getTime().toString());
                    addAlarm(checked, "daily", DAILY_REQUEST_CODE, cal);
                } else {
                    cancelAlarm("daily", DAILY_REQUEST_CODE);
                }
            }
        });
        //get values
        String dne = sharedpreferences.getString(getString(R.string.prefs_dailynotenabled), getString(R.string.empty));
        if(!dne.equals(getString(R.string.empty))){
            swDailyNot.setChecked(Boolean.valueOf(dne));
            etDailyNotTime.setClickable(Boolean.valueOf(dne));
            etDailyNotTime.setEnabled(Boolean.valueOf(dne));
        }
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
                //Beállítások kimentése
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(getString(R.string.prefs_weeklynotday), spWeeklyNotDay.getSelectedItem().toString());
                int tempDay = spWeeklyNotDay.getSelectedItemPosition() + 2;
                editor.putString(getString(R.string.prefs_weeklyNotDayInt), String.valueOf(tempDay));
                editor.apply();

                //Értesítés beállítása
                if(swWeeklyNot.isChecked()) {
                    String time;
                    String[] hourandmin;
                    Calendar cal = Calendar.getInstance();
                    SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String day = sharedpreferences.getString(getString(R.string.prefs_weeklyNotDayInt), getString(R.string.empty));
                    int d = 1;
                    if (!day.equals("")) d = Integer.parseInt(day);
                    time = etWeeklyNotTime.getText().toString();
                    hourandmin = time.split(":");
                    cal.set(Calendar.DAY_OF_WEEK, d);
                    Log.i("notif", "" + cal.get(Calendar.DAY_OF_WEEK));
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourandmin[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(hourandmin[1]));
                    Log.i("notif", cal.getTime().toString());
                    addAlarm(true, "weekly", WEEKLY_REQUEST_CODE, cal);
                    Log.i("notif", cal.getTime().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //get values
        String wnd = sharedpreferences.getString(getString(R.string.prefs_weeklynotday), getString(R.string.empty));
        if(!wnd.equals(getString(R.string.empty))){
            for(int i = 0; i < getResources().getStringArray(R.array.days_of_week_array).length; ++i){
                if(wnd.equals(getResources().getStringArray(R.array.days_of_week_array)[i])) {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(getString(R.string.prefs_weeklyNotDayInt), Integer.toString(i+2));
                    editor.apply();
                    spWeeklyNotDay.setSelection(i);
                    break;
                }
            }
        }


        etWeeklyNotTime = (EditText) findViewById(R.id.etWeeklyNotTime);
        etWeeklyNotTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Beállítások kimentése
                Calendar currentTime = Calendar.getInstance();
                final int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
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

                        //Értesítés beállítása
                        String time;
                        String[] hourandmin;
                        Calendar cal = Calendar.getInstance();
                        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String day = sharedpreferences.getString(getString(R.string.prefs_weeklyNotDayInt), getString(R.string.empty));
                        int d = 1;
                        if (!day.equals("")) d = Integer.parseInt(day);
                        time = etWeeklyNotTime.getText().toString();
                        hourandmin = time.split(":");
                        cal.set(Calendar.DAY_OF_WEEK, d);
                        Log.i("notif", "" + cal.get(Calendar.DAY_OF_WEEK));
                        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourandmin[0]));
                        cal.set(Calendar.MINUTE, Integer.parseInt(hourandmin[1]));
                        Log.i("notif", cal.getTime().toString());
                        addAlarm(true, "weekly", WEEKLY_REQUEST_CODE, cal);
                        Log.i("notif", cal.getTime().toString());
                    }
                }, hour, minute, true);
                timePicker.setTitle(getString(R.string.title_selectTime));
                timePicker.show();
            }
        });
        //get values
        String wnt = sharedpreferences.getString(getString(R.string.prefs_weeklynottime), getString(R.string.empty));
        if(!wnt.equals(getString(R.string.empty))){
            etWeeklyNotTime.setText(wnt);
        }


        swWeeklyNot = (Switch) findViewById(R.id.swWeeklyNot);
        swWeeklyNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Beállítások kimentése
                boolean checked = swWeeklyNot.isChecked();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(getString(R.string.prefs_weeklyNotEnabled), Boolean.toString(checked));
                editor.apply();

                //Többi elem aktíválása/inaktiválása
                etWeeklyNotTime.setClickable(checked);
                etWeeklyNotTime.setEnabled(checked);
                spWeeklyNotDay.setClickable(checked);
                spWeeklyNotDay.setEnabled(checked);

                //Értesítés beállítása
                String time;
                String[] hourandmin;
                Calendar cal = Calendar.getInstance();
                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(checked){
                    String day = sharedpreferences.getString(getString(R.string.prefs_weeklyNotDayInt), getString(R.string.empty));
                    int d = 1;
                    if (!day.equals("")) d = Integer.parseInt(day);
                    time = etWeeklyNotTime.getText().toString();
                    hourandmin = time.split(":");
                    cal.set(Calendar.DAY_OF_WEEK, d);
                    Log.i("notif", "" + cal.get(Calendar.DAY_OF_WEEK));
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourandmin[0]));
                    cal.set(Calendar.MINUTE, Integer.parseInt(hourandmin[1]));
                    Log.i("notif", cal.getTime().toString());
                    addAlarm(checked, "weekly", WEEKLY_REQUEST_CODE, cal);
                } else {
                    cancelAlarm("weekly", WEEKLY_REQUEST_CODE);
                    Log.i("notif", "Cancel WEEKLY alarm.");
                }
            }
        });
        //get values
        String wne = sharedpreferences.getString(getString(R.string.prefs_weeklyNotEnabled), getString(R.string.empty));
        if(!wne.equals(getString(R.string.empty))){
            spWeeklyNotDay.setClickable(Boolean.valueOf(wne));
            spWeeklyNotDay.setEnabled(Boolean.valueOf(wne));
            etWeeklyNotTime.setClickable(Boolean.valueOf(wne));
            etWeeklyNotTime.setEnabled(Boolean.valueOf(wne));
            swWeeklyNot.setChecked(Boolean.valueOf(wne));
        }
        /*<----------------/WEEKLY NOTIFICATIONS---------------->*/
    }

    private void addAlarm(Boolean enabled, String freq, int REQUEST_CODE, Calendar date){
        if(enabled){
            Intent intent = new Intent(getApplicationContext(), NotificationService.class);
            intent.putExtra("type", freq);
            PendingIntent notification = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);

            int alarmType = AlarmManager.RTC_WAKEUP;

            if(freq.equals("daily")){
                Calendar c = Calendar.getInstance();
                if(date.before(c))
                    c.add(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
                c.set(Calendar.SECOND, 0);
                Log.i("notif", c.getTime().toString());
                Log.i("notif", "SETTINGS ===> add: " + " " + "Hour: " + c.get(Calendar.HOUR_OF_DAY) + "Minute: " + c.get(Calendar.MINUTE));
                alarmManager.setRepeating(alarmType, c.getTimeInMillis(), 0, notification);
            } else {
                long interval = 1000 * 60 * 60 * 24 * 7;
                Calendar c = Calendar.getInstance();
                if(date.before(c))
                    c.add(Calendar.DAY_OF_MONTH, 7);
                c.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
                c.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
                c.set(Calendar.DAY_OF_WEEK, date.get(Calendar.DAY_OF_WEEK));
                c.set(Calendar.SECOND, 0);
                Log.i("notif", "SETTINGS ===> add: " + freq + " " + c.getTime().toString());
                alarmManager.setRepeating(alarmType, c.getTimeInMillis(), interval, notification);
            }
        }
    }

    private void cancelAlarm(String freq, int REQUEST_CODE){
        Log.i("notif", "SETTINGS ===> cancel " + freq);
        Intent intent = new Intent(getApplicationContext(), NotificationService.class);
        intent.putExtra("type", freq);
        PendingIntent notification = PendingIntent.getService(getApplicationContext(), REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        alarmManager.cancel(notification);
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

    @Override
    public void onBackPressed(){
        drawerList.setItemChecked(0, true);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        setTitle(menuItems[0]);
        startActivity(intent);
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
