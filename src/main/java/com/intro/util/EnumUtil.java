package com.intro.util;

public class EnumUtil {

    public static <T extends Enum<T>> T loadEnumState(ClassLoader loader, String classBinaryName, String instanceName) throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<T> eClass = (Class<T>)loader.loadClass(classBinaryName);
        return Enum.valueOf(eClass, instanceName);
    }

    public static <T extends Enum<T>> Class<T> loadEnum(ClassLoader loader, String classBinaryName) throws ClassNotFoundException {
        @SuppressWarnings("unchecked")
        Class<T> eClass = (Class<T>)loader.loadClass(classBinaryName);
        return eClass;
    }

    public static <T extends Enum<T>> T nextEnum(Enum<T> tEnum) {
        Class<T> tClass = tEnum.getDeclaringClass();
        T[] values = tClass.getEnumConstants();
        return values[(tEnum.ordinal()+1) % values.length];
    }

}
