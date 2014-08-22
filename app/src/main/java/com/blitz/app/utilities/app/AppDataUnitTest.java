package com.blitz.app.utilities.app;

import android.test.AndroidTestCase;

/**
 * Created by spiff on 8/21/14.
 */
public class AppDataUnitTest extends AndroidTestCase {
    public void testAppDataGetter() {
        AppData<String> data = AppData.string("test");
        String val = data.get();
        assertEquals("", val);
    }
}
