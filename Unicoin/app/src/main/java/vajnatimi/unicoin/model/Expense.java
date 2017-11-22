package vajnatimi.unicoin.model;

import com.orm.SugarRecord;
import java.security.Timestamp;
import java.util.Date;

public class Expense extends SugarRecord {
    String name;
    int amount;
    Date date;
    int category;

    public Expense(){

    }

    public Expense(String name, int amount, Date date, int category){
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public String toString(){
        String s = "Name: " + name + " Amount: " + amount + " Date: " + date.toString() + " Category: " + category;
        return s;
    }

}
