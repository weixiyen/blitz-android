package com.blitz.app.models.comet;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.ssl.SSLHelper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Miguel on 7/17/2014.
 */
public class CometAPI {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

    // Singleton instance.
    private static CometAPI instance = null;

    // Connection status.
    private boolean mWebSocketConnected;
    private boolean mWebSocketConnecting;

    // User to ping the socket for keep-alive.
    private Handler          mWebSocketPingHandler;
    private Runnable         mWebSocketPingRunnable;
    private final static int mWebSocketPingInterval = 60000;

    // Time to reconnect on failures.
    private Handler          mWebSocketReconnectHandler;
    private Runnable         mWebSocketReconnectRunnable;
    private boolean          mWebSocketReconnectPending;
    private final static int mWebSocketReconnectInterval = 30000;

    // Web socket client.
    private WebSocketClient mWebSocketClient;

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Open the web socket asynchronously.
     */
    @SuppressWarnings("unused")
    public static void openWebSocket()  {

        if (!instance().mWebSocketConnected) {

            // Create a client every time.
            instance().createWebSocketClient();

            // Send connect command.
            instance().mWebSocketClient.connect();
            instance().mWebSocketConnecting = true;
        }

        // Enable re-connect.
        instance().enableWebSocketReconnect();
    }

    /**
     * Close the web socket asynchronously.
     */
    @SuppressWarnings("unused")
    public static void closeWebSocket() {

        if (instance().mWebSocketConnected &&
                instance().mWebSocketClient != null) {

            // Send close command.
            instance().mWebSocketClient.close();
            instance().mWebSocketConnecting = false;
        }

        // Disable re-connect.
        instance().disableWebSocketReconnect();
    }

    @SuppressWarnings("unused")
    public void sendMessage(View view) {
        mWebSocketClient.send("Test message");
    }

    //==============================================================================================
    // Private Methods
    //==============================================================================================

    /**
     * Create an instance of the WebSocket client.
     */
    public void createWebSocketClient() {

        // Fetch URI from provided URL.
        URI webSocketURI = createWebSocketURI(AppConfig.WEBSOCKET_URL
                + AppDataObject.userId.getString());

        // Initialize the client.
        mWebSocketClient = new WebSocketClient(webSocketURI) {

            @Override
            public void onMessage(String s) {

                Log.i("Websocket", "Message received: " + s);

                /*
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {  }
                });
                */
            }

            @Override
            public void onOpen(ServerHandshake serverHandshake) {

                Log.i("Websocket", "Opened");

                // Now connected.
                mWebSocketConnected = true;

                // Start pinging.
                //enableWebSocketPings();

                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                Log.i("Websocket", "Closed " + reason);

                // Cleanup connection.
                cleanupWebSocket(mWebSocketConnecting);
            }

            @Override
            public void onError(Exception e) {

                Log.i("Websocket", "Error " + e.getMessage());

                // Cleanup connection.
                cleanupWebSocket(mWebSocketConnecting);
            }
        };

        // Create an ssl socket factory with our all-trusting manager.
        SSLSocketFactory sslSocketFactory = SSLHelper.createInsecureSSLSocketFactory();

        try {

            if (sslSocketFactory != null) {

                // Provide SSL socket to the client.
                mWebSocketClient.setSocket(sslSocketFactory.createSocket());
            }
        } catch (IOException ignored) { }
    }

    /**
     * Cleanup web socket on close or error.
     *
     * @param reconnect Should we attempt to re-connect.
     */
    private void cleanupWebSocket(boolean reconnect) {

        // Not connected.
        mWebSocketConnected = false;

        // Set a pending re-connect if requested.
        mWebSocketReconnectPending = reconnect;

        // Stop pinging.
        disableWebSocketPings();
    }

    /**
     * Enable re-connecting to the web socket
     * after a specified delay.
     */
    private void enableWebSocketReconnect() {

        if (mWebSocketReconnectHandler == null) {
            mWebSocketReconnectHandler = new Handler();
        }

        if (mWebSocketReconnectRunnable == null) {
            mWebSocketReconnectRunnable = new Runnable() {

                @Override
                public void run() {

                    // Re-connect if pending.
                    if (mWebSocketReconnectPending) {
                        mWebSocketReconnectPending = false;

                        // Open connection.
                        openWebSocket();
                    }

                    // Run again on an interval.
                    mWebSocketReconnectHandler.postDelayed(this, mWebSocketReconnectInterval);
                }
            };
        }

        // Attempt to re-connect web socket.
        mWebSocketReconnectHandler.postDelayed
                (mWebSocketReconnectRunnable, mWebSocketReconnectInterval);
    }

    /**
     * Disable re-connecting to the web socket.
     */
    private void disableWebSocketReconnect() {

        // If re-connect is initialized.
        if (mWebSocketReconnectHandler  != null &&
            mWebSocketReconnectRunnable != null) {

            // Remove any pending re-connect callbacks.
            mWebSocketReconnectHandler.removeCallbacks(mWebSocketReconnectRunnable);
        }

        // Disable pending re-connect.
        mWebSocketReconnectPending = false;
    }

    /**
     * Given a URL convert to a URI.
     *
     * @param webSocketURL Target URL.
     *
     * @return URI or null on failure.
     */
    private URI createWebSocketURI(String webSocketURL) {

        try {

            // Create from AppConfig URL.
            return new URI(webSocketURL);

        } catch (URISyntaxException ignored) { }

        return null;
    }

    /**
     * Enable pinging of the web socket.
     */
    private void enableWebSocketPings() {

        if (mWebSocketPingHandler == null) {
            mWebSocketPingHandler = new Handler();
        }

        if (mWebSocketPingRunnable == null) {
            mWebSocketPingRunnable = new Runnable() {

                @Override
                public void run() {

                    // Send a ping if connected.
                    if (mWebSocketConnected) {
                        mWebSocketClient.send("ping");
                    }

                    // Run again on an interval.
                    mWebSocketPingHandler.postDelayed(this, mWebSocketPingInterval);
                }
            };
        }

        // Kickoff ping timer.
        mWebSocketPingHandler.postDelayed
                (mWebSocketPingRunnable, mWebSocketPingInterval);
    }

    /**
     * Disable pinging of the web socket.
     */
    private void disableWebSocketPings() {

        if (mWebSocketPingHandler  != null &&
            mWebSocketPingRunnable != null) {

            // Remove callbacks which stops the ping cycle.
            mWebSocketPingHandler.removeCallbacks(mWebSocketPingRunnable);
        }
    }

    /**
     * Fetch singleton instance.
     *
     * @return Singleton instance.
     */
    private static CometAPI instance() {

        if (instance == null) {
            synchronized (CometAPI.class) {
                if (instance == null) {
                    instance = new CometAPI();
                }
            }
        }

        return instance;
    }
}