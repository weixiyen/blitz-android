package com.blitz.app.utilities.ui;

import android.widget.TextView;

import rx.Observer;

/**
 * Created by Nate on 8/25/14.
 */
public class UIObserver {

    /**
     * Makes an Observer that sets the TextView's value to the observed value.
     */
    public static<T> Observer<T> textField(final TextView textView) {
        // TODO should create a weak reference to textView?
        return new Observer<T>() {
            @Override
            public void onCompleted() {

                throw new IllegalStateException("TextView observables shouldn't ever finish");
            }

            @Override
            public void onError(Throwable e) {

                throw new RuntimeException("Unexpected error");
            }

            @Override
            public void onNext(T t) {

                textView.setText(t == null ? null : t.toString());
            }
        };

    }

    public static abstract class DefaultObserver<T> implements Observer<T> {
        @Override
        public void onCompleted() {
            throw new IllegalStateException("UI observers should never hit an end of stream");
        }

        @Override
        public void onError(Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
