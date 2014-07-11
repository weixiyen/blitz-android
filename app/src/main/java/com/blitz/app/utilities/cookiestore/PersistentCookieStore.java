package com.blitz.app.utilities.cookiestore;

import android.util.Log;

import java.net.HttpCookie;
import java.net.URI;

/**
 * Created by mrkcsc on 7/11/14.
 */
public class PersistentCookieStore extends PersistentCookieStoreBase {

    @Override
    public synchronized void add(URI uri, HttpCookie cookie) {
        super.add(uri, cookie);

        Log.e("Parrot", "Cookie URI: " + uri + " cookie name: " + cookie.getName() + " value: " + cookie.getValue());
    }
}