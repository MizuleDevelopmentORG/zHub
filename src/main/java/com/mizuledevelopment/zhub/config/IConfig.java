package com.mizuledevelopment.zhub.config;

import java.util.ArrayList;
import java.util.List;

public interface IConfig<T, F> {

    String name();

    T handle();

    String getString(String path, String def);

    default String getString(final String path) {
        return this.getString(path, null);
    }

    int getInt(String path, int def);

    default int getInt(final String path) {
        return this.getInt(path, 0);
    }

    long getLong(String path, long def);

    default long getLong(final String path) {
        return this.getLong(path, 0L);
    }

    double getDouble(String path, double def);

    default double getDouble(final String path) {
        return this.getDouble(path, 0.0D);
    }

    float getFloat(String path, float def);

    /**
     * Get a float from a path, or return {@literal 0.0F} if it doesn't exist.
     *
     * @param path the path to check
     * @return a {@link Float} primitive or {@literal 0.0F}
     */
    default float getFloat(final String path) {
        return this.getFloat(path, 0.0F);
    }

    boolean getBoolean(String path, boolean def);

    default boolean getBoolean(final String path) {
        return this.getBoolean(path, false);
    }

    Object get(String path, Object def);

    default Object get(final String path) {
        return this.get(path, null);
    }

    List<String> getStringList(String path, List<String> def);

    default List<String> getStringList(final String path) {
        return this.getStringList(path, new ArrayList<>());
    }

    List<Integer> getIntList(String path, List<Integer> def);

    default List<Integer> getIntList(final String path) {
        return this.getIntList(path, new ArrayList<>());
    }

    <K> List<K> getList(String path, List<K> def);

    default <K> List<K> getList(final String path) {
        return this.getList(path, new ArrayList<>());
    }

    /**
     * It sets the value of the given path to the given object.
     *
     * @param path   The path to the value you want to set.
     * @param object The object to set.
     */
    void set(String path, Object object);

    /**
     * "Given a path, return the section of the configuration that corresponds to
     * that path."
     *
     * <p>
     * The path is a string that represents the path to the section. For example, if
     * you have a configuration like this:
     *
     * @param path The path to the section you want to get.
     * @return A section of the file.
     */
    F section(String path);

    /**
     * Reloads the config
     */
    void reload();
}