package com.blitz.app.utilities.rest;

import java.util.List;

/**
 * Created by Nate on 9/18/14.
 */
public class RestAPIResult<T> {

    private List<T> results;

    public List<T> getResults() { return results; }
}
