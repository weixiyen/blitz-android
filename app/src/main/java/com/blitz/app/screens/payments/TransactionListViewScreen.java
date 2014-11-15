package com.blitz.app.screens.payments;

import android.widget.ListView;

import com.blitz.app.R;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.view_models.ViewModel;
import com.blitz.app.view_models.ViewModelTransactions;

import java.util.List;

import butterknife.InjectView;

/**
 * List view screen for transaction history.
 *
 * Created by Nate on 11/11/14.
 */
public class TransactionListViewScreen extends BaseActivity implements ViewModelTransactions.Callbacks {

    @InjectView(R.id.transaction_list)
    ListView mTransactions;

    ViewModelTransactions mViewModel;


    /**
     * Fetch view model.
     */
    @Override
    public ViewModel onFetchViewModel() {

        if (mViewModel == null) {
            mViewModel = new ViewModelTransactions(this, this);
        }

        return mViewModel;
    }
    public void onTransactions(List<Integer> transactionAmounts,
                               List<String> transactionDescriptions) {

        if(mTransactions != null) {

            mTransactions.setAdapter(new TransactionListAdapter(this, transactionAmounts,
                    transactionDescriptions));
        }
    }



}
