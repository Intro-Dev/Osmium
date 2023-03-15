package dev.lobstershack.client.util;

public class EnumUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T loadEnumState(ClassLoader loader, String classBinaryName, String instanceName) {
        try {
            return Enum.valueOf((Class<T>) loader.loadClass(classBinaryName), instanceName);
        } catch (ClassNotFoundException e) {
            return null;
        }

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
