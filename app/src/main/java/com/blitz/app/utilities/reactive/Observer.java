package com.blitz.app.utilities.reactive;

import com.blitz.app.view_models.ViewModel;

/**
 * A simple generic callback interface.
 *
 * Created by Nate on 9/30/14.
 */
public interface Observer<T> extends ViewModel.ViewModelCallbacks {

    void onNext(T next);
}
