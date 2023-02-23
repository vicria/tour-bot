package ar.vicria.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public final class ClassUtils {
    private ClassUtils() {
    }

    /**
     * Загружает класс с заданным именем и пакетом, и проверяет, что он унаследован от заданного базового класса
     *
     * @param packageName имя пакета
     * @param className   имя класса
     * @param baseClass   базовый класс
     * @return загруженный класс
     */
    public static <T> Class<? extends T> loadClass(String packageName, String className, Class<T> baseClass) {
        return loadClass(getFullClassName(packageName, className), baseClass);
    }

    private static String getFullClassName(String packageName, String className) {
        return packageName + '.' + className;
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<? extends T> loadClass(String fullClassName, Class<T> baseClass) {
        Class clazz = loadClass(fullClassName);
        try {
            return (Class<? extends T>) clazz.asSubclass(baseClass);
        } catch (ClassCastException e) {
            throw new RuntimeException(
                    String.format("Class '%s' is not subclass of '%s'", fullClassName, baseClass.getName()), e);
        }
    }

    /**
     * Загружает класс с заданным полным именем
     */
    private static Class loadClass(String fullClassName) {
        try {
            return Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load class: " + fullClassName, e);
        }
    }

    /**
     * Возвращает список полей класса type, аналогично Class.getDeclaredFields(), но не только для заданного класса,
     * а и для всей унаследованной им иерархии до toBaseType (включительно, если includeBase = true)
     */
    public static <T> List<Field> getFields(Class<? extends T> type, Class<T> toBaseType, boolean includeBase) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = type;

        while (clazz != toBaseType) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }

        if (includeBase) {
            Collections.addAll(fields, clazz.getDeclaredFields());
        }

        return fields;
    }

    /**
     * Выполняет поиск значеня в таблице, у которой ключ - это класс.
     * При этом, если значение для заданного класса не найдено, то пробует найти значение для базового класса,
     * а если и для него не найдено, то для базового-базового и т.д., пока не доберется до Object.
     * Но значение для ключа класса Object уже не ищем - если дошли до Object не найдя значения, то возвращает null.
     */
    public static <Value, Key> Value getValueFromClassKeyMap(
            Class<? extends Key> keyClass,
            Map<Class<? extends Key>, Value> map) {

        Class<?> tmpClass = keyClass;

        while (tmpClass != null && !tmpClass.equals(Object.class)) {
            Value value = map.get(tmpClass);
            if (value != null)
                return value;

            tmpClass = tmpClass.getSuperclass();
        }

        return null;
    }
}
