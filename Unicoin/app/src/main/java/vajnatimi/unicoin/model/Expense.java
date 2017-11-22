package r2zzb4.unicoin.model;

import com.orm.SugarRecord;
import java.security.Timestamp;

public class Expense extends SugarRecord {
    String name;
    int amount;
    Timestamp timestamp;    //TODO: timestamp vs date vs calendar ?
    int category;

    public Expense(String name, int amount, Timestamp timestamp, int category){
        this.name = name;
        this.amount = amount;
        this.timestamp = timestamp;
        this.category = category;
    }

}
