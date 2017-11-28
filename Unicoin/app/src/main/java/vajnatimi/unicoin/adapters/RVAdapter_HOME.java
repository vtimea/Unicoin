package vajnatimi.unicoin.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.model.Transaction2;

public class RVAdapter_HOME extends RecyclerView.Adapter<RVAdapter_HOME.TransactionViewHolder>{
    private static final int NUM_OF_ITEMS_TO_SHOW = 10;
    private static List<Transaction2> transactions;

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

    public RVAdapter_HOME(){
        update();
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
        holder.tvTransactionAmount.setText(transactions.get(position).getAmountString());
        holder.tvTransactionDate.setText(transactions.get(position).getDateString());

        if(transactions.get(position).getAmount() > 0){
            holder.tvTransactionName.setTextColor(Color.rgb(79, 150, 95));
            holder.tvTransactionAmount.setTextColor(Color.rgb(79, 150, 95));
            holder.tvTransactionDate.setTextColor(Color.rgb(79, 150, 95));
        }
        else {
            holder.tvTransactionName.setTextColor(Color.rgb(249, 79, 79));
            holder.tvTransactionAmount.setTextColor(Color.rgb(249, 79, 79));
            holder.tvTransactionDate.setTextColor(Color.rgb(249, 79, 79));
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void update(){
        transactions = Transaction2.listAll(Transaction2.class);
        Comparator<Transaction2> comparator = new Comparator<Transaction2>() {
            @Override
            public int compare(Transaction2 o1, Transaction2 o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };
        Collections.sort(transactions, comparator);
        Collections.reverse(transactions);
        List<Transaction2> temp = new ArrayList<Transaction2>();
        int items_to_show = transactions.size() > NUM_OF_ITEMS_TO_SHOW ? NUM_OF_ITEMS_TO_SHOW : transactions.size();
        for(int i = 0; i < items_to_show; ++i){
            temp.add(transactions.get(i));
        }
        transactions = temp;
        this.notifyDataSetChanged();
    }
}