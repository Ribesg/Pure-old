package fr.ribesg.bukkit.pure.util;

import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Ribesg
 */
public final class ReflectionUtils {

    /**
     * Creates a new instance of the provided class without calling any
     * constructor.
     *
     * @param clazz the class
     * @param <T>   the type
     *
     * @return an instance of the provided class
     *
     * @throws ReflectiveOperationException fi anything goes wrong
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Class<T> clazz) throws ReflectiveOperationException {
        try {
            final ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
            final Constructor<?> objectConstructor = Object.class.getDeclaredConstructor();
            final Constructor<?> serializeConstructor = reflectionFactory.newConstructorForSerialization(clazz, objectConstructor);
            return (T) serializeConstructor.newInstance();
        } catch (final NoSuchMethodException ignored) {
            // Can't be thrown when getting the constructor of Object!
            return null; // Dead code
        }
    }

    /**
     * Sets the provided field to the provided value in the provided instance
     * of the provided class.
     *
     * @param clazz     the class
     * @param obj       the object
     * @param fieldName the name of the field
     * @param value     the new value of the field
     *
     * @throws NoSuchFieldException   if the field doesn't exist in the class
     * @throws IllegalAccessException if security issues prevent this call
     */
    public static void set(final Class<?> clazz, final Object obj, final String fieldName, final Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);

        if (Modifier.isFinal(field.getModifiers())) {
            // Field is final, work around it
            final Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        }

        field.set(obj, value);
    }

    /**
     * Gets the value of the provided field from the provided instance of the
     * provided class.
     *
     * @param clazz      the class of the object
     * @param obj        the object
     * @param fieldName  the name of the field
     * @param fieldClass the class of the field
     * @param <T>        the type of the field
     *
     * @return the field's value
     *
     * @throws NoSuchFieldException   if the field doesn't exist in the class
     * @throws IllegalAccessException if security issues prevent this call
     */
    public static <T> T get(final Class<?> clazz, final Object obj, final String fieldName, final Class<T> fieldClass) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return fieldClass.cast(field.get(obj));
    }
}
