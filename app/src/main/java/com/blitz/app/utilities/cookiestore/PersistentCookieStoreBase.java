package com.blitz.app.utilities.cookiestore;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An in-memory cookie store.
 */
public class PersistentCookieStoreBase implements CookieStore {

    /** this map may have null keys! */
    private final Map<URI, List<HttpCookie>> map = new HashMap<URI, List<HttpCookie>>();

    public synchronized void add(URI uri, HttpCookie cookie) {

        if (cookie == null) {
            throw new NullPointerException("cookie == null");
        }

        uri = cookiesUri(uri);
        List<HttpCookie> cookies = map.get(uri);

        if (cookies == null) {
            cookies = new ArrayList<HttpCookie>();
            map.put(uri, cookies);
        } else {
            cookies.remove(cookie);
        }

        cookies.add(cookie);
    }

    private URI cookiesUri(URI uri) {

        if (uri == null) {
            return null;
        }
        try {
            return new URI("http", uri.getHost(), null, null);
        } catch (URISyntaxException e) {
            return uri; // probably a URI with no host
        }
    }

    public synchronized List<HttpCookie> get(URI uri) {

        if (uri == null) {
            throw new NullPointerException("uri == null");
        }
        List<HttpCookie> result = new ArrayList<HttpCookie>();

        // get cookies associated with given URI. If none, returns an empty list
        List<HttpCookie> cookiesForUri = map.get(uri);
        if (cookiesForUri != null) {
            for (Iterator<HttpCookie> i = cookiesForUri.iterator(); i.hasNext(); ) {
                HttpCookie cookie = i.next();
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else {
                    result.add(cookie);
                }
            }
        }

        // get all cookies that domain matches the URI
        for (Map.Entry<URI, List<HttpCookie>> entry : map.entrySet()) {
            if (uri.equals(entry.getKey())) {
                continue; // skip the given URI; we've already handled it
            }
            List<HttpCookie> entryCookies = entry.getValue();
            for (Iterator<HttpCookie> i = entryCookies.iterator(); i.hasNext(); ) {
                HttpCookie cookie = i.next();
                if (!HttpCookie.domainMatches(cookie.getDomain(), uri.getHost())) {
                    continue;
                }
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else if (!result.contains(cookie)) {
                    result.add(cookie);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<HttpCookie> getCookies() {

        List<HttpCookie> result = new ArrayList<HttpCookie>();
        for (List<HttpCookie> list : map.values()) {
            for (Iterator<HttpCookie> i = list.iterator(); i.hasNext(); ) {
                HttpCookie cookie = i.next();
                if (cookie.hasExpired()) {
                    i.remove(); // remove expired cookies
                } else if (!result.contains(cookie)) {
                    result.add(cookie);
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<URI> getURIs() {

        List<URI> result = new ArrayList<URI>(map.keySet());
        result.remove(null); // sigh
        return Collections.unmodifiableList(result);
    }

    public synchronized boolean remove(URI uri, HttpCookie cookie) {

        if (cookie == null) {
            throw new NullPointerException("cookie == null");
        }
        List<HttpCookie> cookies = map.get(cookiesUri(uri));

        return cookies != null && cookies.remove(cookie);
    }

    public synchronized boolean removeAll() {

        boolean result = !map.isEmpty();
        map.clear();
        return result;
    }
}