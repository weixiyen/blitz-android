package com.blitz.app.libraries.general.backgrounded;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by mrkcsc on 12/11/14. Copyright 2014 Blitz Studios
 */
@SuppressWarnings("UnusedDeclaration")
public class MGBackgrounded {

    // Tracks the backgrounded state internally, but can be fired many times.
    private static final PublishSubject<Boolean> backgrounded = PublishSubject.create();

    @Getter
    private static final Observable<Boolean> observable
            = backgrounded.distinctUntilChanged().cache(1);

    @Getter
    private static Config config = new Config();

    /**
     * Is the application currently in the background.
     */
    public static boolean isBackgrounded() {

        // Fetch the most recent backgrounded value.
        return observable.toBlocking().mostRecent(false).iterator().next();
    }

    /**
     * Configuration methods for the backgrounded
     * utility.  Should be invoked during ALL activity
     * life-cycle methods for this class to be reliable.
     */
    public static class Config {

        /**
         * Clears existing observable subscription and
         * emits a backgrounded true flag on a delay.
         */
        public void activityPaused() {

            activityResumed();

            Observable.timer(1, TimeUnit.SECONDS).takeUntil(backgrounded)
                    .subscribe(timestamp -> backgrounded.onNext(true));
        }

        /**
         * Emits a no longer backgrounded flag
         * when an activity app controls is resumed.
         */
        public void activityResumed() {

            backgrounded.onNext(false);
        }
    }
}
