package com.blitz.app.models.comet;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Miguel on 7/17/2014.
 */
public class CometClient {

    private Activity mActivity;

    private WebSocketClient mWebSocketClient;

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://websockethost:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {

            @Override
            public void onMessage(String s) {
                final String message = s;

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("Websocket", "Message received: " + message);
                    }
                });
            }

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");

                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };

        mWebSocketClient.connect();
    }

    public void sendMessage(View view) {
        mWebSocketClient.send("Test message");
    }
}