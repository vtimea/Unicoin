package r2zzb4.unicoin.model;

import java.util.List;

public class Transactions {
    List<Expense> expenses;
    List<Income> incomes;

    public Transactions(){
        update();
    }

    public void update(){
        expenses = Expense.listAll(Expense.class);
        incomes = Income.listAll(Income.class);
    }
}
