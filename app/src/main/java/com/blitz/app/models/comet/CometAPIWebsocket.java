package com.blitz.app.models.comet;

import android.os.Handler;

import com.blitz.app.utilities.app.AppConfig;
import com.blitz.app.utilities.app.AppDataObject;
import com.blitz.app.utilities.ssl.SSLHelper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Miguel on 7/17/2014.
 */
class CometAPIWebsocket {

    //==============================================================================================
    // Member Variables
    //==============================================================================================

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

    // Messages that need to be sent when connected.
    private ArrayList<String> mPendingWebSocketMessages;

    // Interface for receiving messages.
    private OnMessageCallback mOnMessageCallback;

    //==============================================================================================
    // Constructor
    //==============================================================================================

    /**
     * Empty constructor disallowed.
     */
    @SuppressWarnings("unused")
    private CometAPIWebsocket() {

    }

    /**
     * Default constructor.
     */
    @SuppressWarnings("unused")
    public CometAPIWebsocket(OnMessageCallback callback) {

        // Initialize pending messages.
        mPendingWebSocketMessages = new ArrayList<String>();

        // Set callback.
        mOnMessageCallback = callback;
    }

    //==============================================================================================
    // Public Methods
    //==============================================================================================

    /**
     * Open the web socket asynchronously.
     */
    @SuppressWarnings("unused")
    public void openWebSocket()  {

        if (!mWebSocketConnected) {

            // Create a client every time.
            createWebSocketClient();

            // Send connect command.
            mWebSocketClient.connect();
            mWebSocketConnecting = true;
        }

        // Enable re-connect.
        enableWebSocketReconnect();

        // Enable pinging.
        enableWebSocketPings();
    }

    /**
     * Close the web socket asynchronously.
     */
    @SuppressWarnings("unused")
    public void closeWebSocket() {

        if (mWebSocketConnected &&
            mWebSocketClient != null) {

            // Send close command.
            mWebSocketClient.close();
            mWebSocketConnecting = false;
        }

        // Disable re-connect.
        disableWebSocketReconnect();

        // Disable pinging.
        disableWebSocketPings();
    }

    /**
     * Sends a message to the socket.
     *
     * @param jsonMessage Message should be in json format.
     */
    @SuppressWarnings("unused")
    public void sendMessageToWebSocket(String jsonMessage) {

        if (mWebSocketConnected) {

            // Send message to the web socket.
            mWebSocketClient.send(jsonMessage);
        } else {

            // Add to pending if not connected.
            mPendingWebSocketMessages.add(jsonMessage);
        }
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

            /**
             * When a message is received, send it
             * through the callback interface.
             *
             * @param message Message assumed to be a JSON string.
             */
            @Override
            public void onMessage(String message) {

                // Send message via callback.
                if (mOnMessageCallback != null) {
                    mOnMessageCallback.onMessage(new JsonParser().parse(message).getAsJsonObject());
                }
            }

            @Override
            public void onOpen(ServerHandshake serverHandshake) {

                // Now connected.
                mWebSocketConnected = true;

                // Iterate over any pending messages.
                for (String jsonMessage : mPendingWebSocketMessages) {

                    // Send to web socket.
                    sendMessageToWebSocket(jsonMessage);
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                // Cleanup connection.
                cleanupWebSocket(mWebSocketConnecting);
            }

            @Override
            public void onError(Exception e) {

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

    //==============================================================================================
    // Interface
    //==============================================================================================

    /**
     * Callback for when a websocket
     * message is received.
     */
    interface OnMessageCallback {

        public void onMessage(JsonObject jsonObject);
    }
}