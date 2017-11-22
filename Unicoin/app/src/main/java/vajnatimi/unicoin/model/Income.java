package vajnatimi.unicoin.model;

import com.orm.SugarRecord;

import java.security.Timestamp;
import java.util.Date;

public class Income extends SugarRecord{
    String name;
    int amount;
    Date date;

    public Income(){

    }

    public Income(String name, int amount, Date date){
        this.name = name;
        this.amount = amount;
        this.date = date;
    }

    public String toString(){
        String s = "\nName: " + "\nAmount: " + amount + "\nDate: " + date.toString();
        return s;
    }
}