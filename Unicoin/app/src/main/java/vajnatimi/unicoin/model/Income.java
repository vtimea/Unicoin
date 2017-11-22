package vajnatimi.unicoin.model;

import com.orm.SugarRecord;

import java.security.Timestamp;

public class Income extends SugarRecord{
    int id;
    String name;
    int amount;
    Timestamp timestamp;

    public Income(){}

}