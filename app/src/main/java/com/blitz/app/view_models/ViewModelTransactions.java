package com.blitz.app.view_models;

import com.blitz.app.rest_models.RestModelTransaction;
import com.blitz.app.utilities.android.BaseActivity;
import com.blitz.app.utilities.authentication.AuthHelper;
import com.blitz.app.utilities.rest.RestAPICallback;
import com.blitz.app.utilities.rest.RestAPIResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Transactions view model.
 *
 * Created by Nate on 11/11/14.
 */
public class ViewModelTransactions extends ViewModel {

    /**
     * Creating a new view model requires an activity and a callback.
     *
     * @param activity  Activity is used for any android context actions.
     * @param callbacks Callbacks so that the view model can communicate changes.
     */
    public ViewModelTransactions(BaseActivity activity, ViewModel.Callbacks callbacks) {

        super(activity, callbacks);
    }

    @Override
    public void initialize() {

        RestModelTransaction.listTransactionsForUserId(AuthHelper.instance().getUserId(), 150,
                new RestAPICallback<RestAPIResult<RestModelTransaction>>(mActivity) {

                    @Override
                    public void success(RestAPIResult<RestModelTransaction> jsonObject) {

                        int size = jsonObject.getResults().size();

                        List<Integer> amounts = new ArrayList<Integer>(size);
                        List<String> descriptions = new ArrayList<String>(size);

                        for(RestModelTransaction transaction : jsonObject.getResults()) {
                            amounts.add(transaction.getAmount());
                            descriptions.add(transaction.getType());
                        }

                        getCallbacks(Callbacks.class).onTransactions(amounts, descriptions);
                    }
                });
    }


    // region Callbacks Interface
    // ============================================================================================================

    public interface Callbacks extends ViewModel.Callbacks {

        public void onTransactions(List<Integer> transactionAmounts,
                                   List<String> transactionDescriptions);
    }

    // endregion
}
