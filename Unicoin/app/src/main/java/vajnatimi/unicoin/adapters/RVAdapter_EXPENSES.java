package vajnatimi.unicoin.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.model.Transaction2;

public class RVAdapter_EXPENSES extends RecyclerView.Adapter<RVAdapter_EXPENSES.TransactionViewHolder>{
    private static final int NUM_OF_ITEMS_TO_SHOW = 10;
    private static List<Transaction2> transactions = new ArrayList<>();

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tvTransactionName;
        TextView tvTransactionDate;
        TextView tvTransactionAmount;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            tvTransactionName = (TextView)itemView.findViewById(R.id.tvTransactionName);
            tvTransactionDate = (TextView)itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionAmount = (TextView)itemView.findViewById(R.id.tvTransactionAmount);
        }
    }

    public RVAdapter_EXPENSES(){
        //update();
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        TransactionViewHolder tvh = new TransactionViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.tvTransactionName.setText(transactions.get(position).getName());
        holder.tvTransactionDate.setText(transactions.get(position).getDateString());

        int amount = transactions.get(position).getAmount();
        String formattedAmount = String.format("%,d", amount);
        holder.tvTransactionAmount.setText(formattedAmount);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    //Tranzakciók csökkenő időrendbe rendezése
    private void sortTransactions(){
        transactions = Transaction2.listAll(Transaction2.class);
        Comparator<Transaction2> comparator = new Comparator<Transaction2>() {
            @Override
            public int compare(Transaction2 o1, Transaction2 o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        Collections.sort(transactions, comparator);
        Collections.reverse(transactions);
    }

    public List<Transaction2> update(){
        sortTransactions();
        List<Transaction2> temp = new ArrayList<Transaction2>();
        for(int i = 0; i < transactions.size(); ++i){
            if(transactions.get(i).getAmount() < 0)
                temp.add(transactions.get(i));
        }
        transactions = temp;
        this.notifyDataSetChanged();

        return transactions;
    }

    //A megadott hónaphoz tartozó tranzakciók listázása
    public List<Transaction2> update(int year, int month){
        sortTransactions();
        List<Transaction2> temp = new ArrayList<>();
        for(int i = 0; i < transactions.size(); ++i){
            if(transactions.get(i).getYear() == year && transactions.get(i).getMonth() == month && transactions.get(i).getAmount() < 0){
                temp.add(transactions.get(i));
            } else if(transactions.get(i).getYear() < year)
                break;
        }
        transactions = temp;
        this.notifyDataSetChanged();

        return transactions;
    }

    //Ismétlődő tranzakciók listázása
    public void updateByRecurr(){
        sortTransactions();
        List<Transaction2> temp = new ArrayList<>();
        for(int i = 0; i < transactions.size(); ++i){
            if(transactions.get(i).getRecurr() && transactions.get(i).getAmount() < 0)
                temp.add(transactions.get(i));
        }
        transactions = temp;
        this.notifyDataSetChanged();
    }

    //Tranzakciók időrendbe rendezése
    public List<Transaction2> getSortedTransactions(){
        List<Transaction2> tr = Transaction2.listAll(Transaction2.class);
        Comparator<Transaction2> comparator = new Comparator<Transaction2>() {
            @Override
            public int compare(Transaction2 o1, Transaction2 o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        Collections.sort(tr, comparator);
        Collections.reverse(tr);
        return tr;
    }
}
