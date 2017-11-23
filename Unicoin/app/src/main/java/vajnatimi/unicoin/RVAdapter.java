package vajnatimi.unicoin;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vajnatimi.unicoin.model.Transaction2;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.TransactionViewHolder>{
    List<Transaction2> transactions;

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tvTransactionName;
        TextView tvTransactionDate;
//        TextView tvTransactionAmount;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            tvTransactionName = (TextView)itemView.findViewById(R.id.tvTransactionName);
            tvTransactionDate = (TextView)itemView.findViewById(R.id.tvTransactionDate);
//            tvTransactionAmount = (TextView)itemView.findViewById(R.id.tvTransactionA);
        }
    }

    public RVAdapter(){
        this.transactions = Transaction2.listAll(Transaction2.class);
        for(int i = 0; i < transactions.size(); ++i){
            Log.d("Adap", transactions.get(i).getName());
        }
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
//        holder.tvTransactionAmount.setText(transactions.get(position).getAmount());
        holder.tvTransactionDate.setText(transactions.get(position).getDateString());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void update(){
        transactions = Transaction2.listAll(Transaction2.class);
    }
}
