package vajnatimi.unicoin.model;

import android.icu.util.Calendar;
import android.util.Log;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

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

    public Date getDate(){
        return date;
    }

    public String getDateString(){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String s = String.valueOf(c.get(Calendar.YEAR)) + "." + String.valueOf(c.get(Calendar.MONTH)) + "." + String.valueOf(c.get(Calendar.DAY_OF_MONTH)) + ".";
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

    public static List<Transaction2> sortByDate(List<Transaction2> list){
        int n = list.size();
        for(int i = 0; i < n-1; ++i){
            for(int j = i; j < n-1; ++j){
                if(list.get(j).date.compareTo(list.get(j+1).date) < 0){
                    Transaction2 temp = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, temp);

                }
            }
        }
        for(int i = 0; i < n; ++i)
            Log.i("mt", "List == > " + list.get(i).date);
        return list;
    }

}
