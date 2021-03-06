package vajnatimi.unicoin.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import vajnatimi.unicoin.R;
import vajnatimi.unicoin.activities.HomeActivity;
import vajnatimi.unicoin.model.Transaction2;

public class RVAdapter_HOME extends RecyclerView.Adapter<RVAdapter_HOME.TransactionViewHolder>{
    private static final int NUM_OF_ITEMS_TO_SHOW = 10;
    private static List<Transaction2> transactions;
    private Context context;

    //longclick listener for the recycler view items
    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tvTransactionName;
        TextView tvTransactionDate;
        TextView tvTransactionAmount;
        View view;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            tvTransactionName = (TextView)itemView.findViewById(R.id.tvTransactionName);
            tvTransactionDate = (TextView)itemView.findViewById(R.id.tvTransactionDate);
            tvTransactionAmount = (TextView)itemView.findViewById(R.id.tvTransactionAmount);
            view = itemView;
        }
    }

    public RVAdapter_HOME(Context context){
        this.context = context;
        update();
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        TransactionViewHolder tvh = new TransactionViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(final TransactionViewHolder holder, final int position) {
        holder.tvTransactionName.setText(transactions.get(position).getName());
        holder.tvTransactionDate.setText(transactions.get(position).getDateString());

        //format the amount before setting it to the textview -> 100,000,000
        int amount = transactions.get(position).getAmount();
        String formattedAmount = String.format("%,d", amount);
        holder.tvTransactionAmount.setText(formattedAmount);

        //setting the color of the text view depending on the amount (expense or income)
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

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Action")
                        .setItems(R.array.edit_or_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    //TODO: edit the transaction
                                    Toast.makeText(context, "EDIT!", Toast.LENGTH_SHORT).show();
                                } else if(which == 1){
                                    //Delete the selected transaction
                                    Transaction2 toBeDeleted = transactions.get(position);
                                    List<Transaction2> trs = Transaction2.listAll(Transaction2.class);
                                    for(int i = 0; i < trs.size(); ++i){
                                        Transaction2 temp = trs.get(i);
                                        if(temp.getName().equals(toBeDeleted.getName()) &&
                                                temp.getAmount() == toBeDeleted.getAmount() &&
                                                temp.getCategory() == toBeDeleted.getCategory() &&
                                                temp.getRecurr() == toBeDeleted.getRecurr() &&
                                                temp.getDate().compareTo(toBeDeleted.getDate()) == 0){
                                            temp.delete();
                                            break;
                                        }
                                    }

                                    //call the activitiy's onItemLongClicked function to update the view
                                    ((OnItemLongClickListener) context).onItemLongClicked(position);
                                }
                            }
                        });
                builder.create();
                builder.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public List<Transaction2> update(){
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

        return transactions;
    }

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

    //Ismétlődő tranzakciók felvétele automatikusan
    public void addRecurringTransactions(Date currDate){
        Calendar currC = Calendar.getInstance();
        currC.setTime(currDate);
        List<Transaction2> l = Select.from(Transaction2.class).where(Condition.prop("recurr").eq(1)).list();
        for(int i = 0; i < l.size(); ++i){
            Transaction2 old = l.get(i);
            Calendar oldC = Calendar.getInstance();
            oldC.setTime(old.getDate());
            old.delete();
            Transaction2 tr = old;
            while (oldC.compareTo(currC) <= 0) {
                tr = new Transaction2(old.getName(), old.getAmount(), oldC.getTime(), false, old.getCategory());
                tr.save();
                oldC.add(Calendar.MONTH, 1);
            }
            tr.setRecurr(true);
            tr.save();
        }
    }
}