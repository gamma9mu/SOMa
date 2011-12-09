package cs437.som.util;

import cs437.som.SOMError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class to load others through reflection.
 */
public class Reflector {
    /**
     * Create an object, from its single string constructor, by reflection.
     * The single argument is taken as the arguments provided on the line from
     * the input stream.
     *
     * @param pkg The package in which to find the class.
     * @param cls The class to instantiate.
     * @param args The arguments {@code String} to provide.
     * @return A constructed object of type {@code cls}.
     */
    public static Object instantiateFromString(String pkg, String cls, String args) {
        String className = pkg + '.' + cls;
        Object object;
        try {
            Class<?> clsObj = Class.forName(className);
            Constructor<?> ctor = clsObj.getConstructor(String.class);
            object = ctor.newInstance(args);
        } catch (ClassNotFoundException e) {
            throw new SOMError("Cannot find " + className);
        } catch (InstantiationException e) {
            throw new SOMError("Cannot create " + className);
        } catch (IllegalAccessException e) {
            throw new SOMError("Cannot create " + className);
        } catch (NoSuchMethodException e) {
            throw new SOMError("Cannot create " + className);
        } catch (InvocationTargetException e) {
            throw new SOMError("Cannot create " + className +
                    ": bad arguments.");
        }
        return object;
    }

    /**
     * Create an object, from its default constructor, by reflection.
     *
     * @param pkg The package in which to find the class.
     * @param cls The class to instantiate.
     * @return A default constructed object of type {@code cls}.
     */
    public static Object instantiateClass(String pkg, String cls) {
        String className = pkg + '.' + cls;
        Object object;
        try {
            Class<?> clsObj = Class.forName(className);
            object = clsObj.newInstance();
        } catch (ClassNotFoundException e) {
            throw new SOMError("Cannot find " + className);
        } catch (InstantiationException e) {
            throw new SOMError("Cannot create " + className);
        } catch (IllegalAccessException e) {
            throw new SOMError("Cannot create " + className);
        }
        return object;
    }

    /**
     * Create an object, from its single {@code double} constructor, by
     * reflection. The single argument is taken as the argument provided on the
     * line from the input stream.
     *
     * @param pkg The package in which to find the class.
     * @param cls The class to instantiate.
     * @param doubleStr The argument double in String form.
     * @return A constructed object of type {@code cls}.
     */
    public static Object instantiateFromDoubleString(String pkg, String cls, String doubleStr) {
        String className = pkg + '.' + cls;
        Object object;
        try {
            Class<?> clsObj = Class.forName(className);
            Constructor<?> ctor = clsObj.getConstructor(Double.class);
            double argument = Double.parseDouble(doubleStr);
            object = ctor.newInstance(argument);
        } catch (ClassNotFoundException e) {
            throw new SOMError("Cannot find " + className);
        } catch (InstantiationException e) {
            throw new SOMError("Cannot create " + className);
        } catch (IllegalAccessException e) {
            throw new SOMError("Cannot create " + className);
        } catch (NoSuchMethodException e) {
            throw new SOMError("Cannot create " + className);
        } catch (InvocationTargetException e) {
            throw new SOMError("Cannot create " + className +
                    ": bad arguments.");
        }
        return object;
    }
}
