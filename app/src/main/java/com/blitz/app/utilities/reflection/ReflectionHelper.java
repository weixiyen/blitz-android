package com.blitz.app.utilities.reflection;

import java.lang.reflect.Field;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
@SuppressWarnings("unused")
public class ReflectionHelper {

    /**
     * Given a string and resource class, find
     * associated resource id.
     *
     * @param variableName Target variable name.
     * @param resourceClass Resource class (drawable, layout, etc).
     *
     * @return Resource id, or -1 if not found.
     */
    public static int getResourceId(String variableName, Class<?> resourceClass) {

        try {
            Field idField = resourceClass.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }
}