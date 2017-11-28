package vajnatimi.unicoin.model;

import android.icu.util.Calendar;
import android.util.Log;

import com.orm.SugarRecord;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.util.Collections.sort;

public class Transaction2 extends SugarRecord{
    boolean recurr;
    String name;
    int amount;
    Date date;
    Category category;

    public enum Category {
        UNCATEGORIZED, CLOTHING, DEBT, ENTERTAINMENT,
        FOOD, HOUSING, INSURANCE, MEDICAL, MISCELLANEOUS
    }

    public Transaction2(){}

    public Transaction2(String name, int amount, Date date, boolean recurr){
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.recurr = recurr;
        this.category = null;
    }

    public Transaction2(String name, int amount, Date date, boolean recurr, Category category){
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.recurr = recurr;
        this.category = category;
    }

    public boolean getRecurr(){
        return recurr;
    }

    public String getName(){
        return name;
    }

    public int getAmount(){
        return amount;
    }

    public String getAmountString(){
        String s = String.valueOf(amount);
        return s;
    }

    public Date getDate(){
        return date;
    }

    public int getYear(){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public int getMonth(){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH)+1;
    }

    public String getDateString(){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String s = String.valueOf(c.get(Calendar.YEAR)) + "." + String.valueOf(c.get(Calendar.MONTH)+1) + "." + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ".";
        return s;
    }

    public Category getCategory(){
        return category;
    }

    public String toString(){
        String s = "TYPE: " + ((amount > 0) ? "income" : "expense") + "\nNAME: " + name
                    + "\nAMOUNT: " + amount + "\nDATE: " + date.toString() + "\nCATEGORY: " + category
                    + "\n RECURR: " + recurr;
        return s;
    }
}
