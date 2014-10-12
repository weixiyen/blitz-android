package com.blitz.app.utilities.blitz;

import android.os.Handler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mrkcsc on 10/12/14. Copyright 2014 Blitz Studios
 */
public class BlitzDelay {

    private static ConcurrentHashMap<Handler, Runnable> mHandlerMap =
            new ConcurrentHashMap<Handler, Runnable>();

    /**
     * Runs a callback on a delay.
     *
     * @param runnable Callback runnable.
     * @param delayMillis Delay to execute.
     *
     * @return Associated handler responsible for running the callback.
     */
    public static Handler postDelayed(final Runnable runnable, long delayMillis) {

        // Create a new handler.
        final Handler handler = new Handler();

        // Run callback on a delay.
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                // Run user callback.
                runnable.run();

                // Remove handler from map.
                mHandlerMap.remove(handler);
            }
        }, delayMillis);

        // Store handler for the remove call.
        mHandlerMap.put(handler, runnable);

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
        }
    }
}
