package com.blitz.app.utilities.blitz;

import android.os.Handler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mrkcsc on 10/12/14. Copyright 2014 Blitz Studios
 */
public class BlitzDelay {

    // region Member Variables
    // ============================================================================================================

    // Store handlers so the user has the option to cancel them.
    private static ConcurrentHashMap<Handler, Runnable> mHandlerMap =
            new ConcurrentHashMap<>();

    // endregion

    // region Public Methods
    // ============================================================================================================

    /**
     * Runs a callback on a delay.
     *
     * @param runnable Callback runnable.
     * @param delayMillis Delay to execute.
     *
     * @return Associated handler responsible for running the callback.
     */
    public static Handler postDelayed(final Runnable runnable, long delayMillis) {

        return postDelayed(runnable, delayMillis, false);
    }

    /**
     * Runs a callback on a delay.
     *
     * @param runnable Callback runnable.
     * @param delayMillis Delay to execute.
     * @param loop Should we loop the callback.
     *
     * @return Associated handler responsible for running the callback.
     */
    public static Handler postDelayed(final Runnable runnable, final long delayMillis, final boolean loop) {

        return postDelayed(runnable, delayMillis, loop, false);
    }

    /**
     * Runs a callback on a delay.
     *
     * @param runnable Callback runnable.
     * @param delayMillis Delay to execute.
     * @param loop Should we loop the callback.
     * @param postImmediately Should we start right away.
     *
     * @return Associated handler responsible for running the callback.
     */
    public static Handler postDelayed(final Runnable runnable, final long delayMillis,
                                      final boolean loop, final boolean postImmediately) {

        // Create a new handler.
        final Handler handler = new Handler();

        Runnable callback = new Runnable() {

            @Override
            public void run() {

                // Run user callback.
                if (runnable != null) {
                    runnable.run();
                }

                if (loop) {

                    // Post forever if we are looping.
                    handler.postDelayed(this, delayMillis);
                } else {

                    // Remove handler from map.
                    mHandlerMap.remove(handler);
                }
            }
        };

        // Run immediately or on delay.
        if (postImmediately) {

            handler.post(callback);
        } else {
            handler.postDelayed(callback, delayMillis);
        }

        // Store handler for the remove call.
        mHandlerMap.put(handler, callback);

        return handler;
    }

    /**
     * Remove any associated callbacks (runnables)
     * associated with a handler.
     *
     * @param handler Handler.
     */
    public static void remove(Handler handler) {

        // Remove if handler and associated runnable still exist.
        if (handler != null && mHandlerMap.containsKey(handler)) {
            handler.removeCallbacks(mHandlerMap.get(handler));

            // Remove it from map.
            mHandlerMap.remove(handler);
        }
    }

    // endregion
}