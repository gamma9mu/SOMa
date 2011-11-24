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
    static Object instantiateFromString(String pkg, String cls, String args) {
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
}
