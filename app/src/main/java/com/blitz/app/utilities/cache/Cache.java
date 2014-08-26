package com.blitz.app.utilities.cache;

/**
 * Interface for a cache of (key, value) pairs. To get a singleton instance, use
 * CacheProvider.get(context)
 *
 * Created by Nate on 8/25/14.
 */
public interface Cache {

    /**
     * True iff there is a value associated with key
     */
    public boolean hasKey(String key);

    /**
     * Get a cached value for key
     * The result is undefined if hasKey(key) == false
     */
    public String get(String key);

    /**
     * Store the (key, value) pair, overwriting any existing value
     */
    public void put(String key, String value);

    /**
     * Remove all (key, value) pairs from the cache.
     */
    public void clear();
}
