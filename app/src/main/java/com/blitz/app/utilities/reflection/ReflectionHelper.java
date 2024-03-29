package com.blitz.app.utilities.reflection;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.blitz.app.utilities.string.StringHelper;

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

    /**
     * Given a class and a resource class, find
     * associated resource id.
     *
     * @param variableClass Target class (class name is resource id).
     * @param resourceClass Resource class (drawable, layout, etc).
     *
     * @return Resource id, or -1 if not found.
     */
    public static int getResourceId(Class variableClass, Class<?> resourceClass) {

        // Fetch the class name string in the format of resource view.
        String underscoredClassName = StringHelper
                .camelCaseToLowerCaseUnderscores(variableClass.getSimpleName());

        // Call function using converted variable class.
        return getResourceId(underscoredClassName, resourceClass);
    }

    /**
     * Convert DP to pixels.
     *
     * @param context Context object.
     * @param densityPixels Target DP.
     *
     * @return Converted pixel value.
     */
    public static int densityPixelsToPixels(Context context, int densityPixels) {

        // Grab the resources object.
        Resources resources = context.getResources();

        // Return pixel value.
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                densityPixels, resources.getDisplayMetrics()));
    }
}