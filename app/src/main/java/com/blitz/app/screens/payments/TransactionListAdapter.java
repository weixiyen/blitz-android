package com.blitz.app.screens.payments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.blitz.app.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List adapter for Transactions.
 *
 * Created by Nate on 11/11/14.
 */
public class TransactionListAdapter extends ArrayAdapter<Integer> {

    private final List<Integer> mTransactionAmounts;
    private final List<String> mTransactionDescriptions;

    public TransactionListAdapter(Context context, List<Integer> transactionAmounts,
                                  List<String> transactionDescriptions) {
        super(context, R.layout.transaction_list_view_screen, transactionAmounts);

        mTransactionAmounts = transactionAmounts;
        mTransactionDescriptions = transactionDescriptions;
    }


    /**
     * Fetch and populate a leaderboard list item
     * at a specified position.
     *
     * @param position Position.
     * @param convertView Re-usable view.
     * @param parent Parent view.
     *
     * @return Inflated and populated view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.transaction_list_item, parent, false);

            // Create and set associated view holder.
            convertView.setTag(new TransactionListItemViewHolder(convertView));
        }

        TransactionListItemViewHolder viewHolder = (TransactionListItemViewHolder) convertView.getTag();

        viewHolder.mTransactionAmount.setText(String.format("$%.02f", mTransactionAmounts.get(position) / 100.0f));
        viewHolder.mTransactionDescription.setText(mTransactionDescriptions.get(position));


        return convertView;
    }

    static class TransactionListItemViewHolder {

        @InjectView(R.id.transaction_amount) TextView mTransactionAmount;
        @InjectView(R.id.transaction_description) TextView mTransactionDescription;

        TransactionListItemViewHolder(View convertView) {

            // Map the member variables.
            ButterKnife.inject(this, convertView);
        }
    }


}
