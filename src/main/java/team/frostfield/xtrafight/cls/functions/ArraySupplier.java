package team.frostfield.xtrafight.cls.functions;

@FunctionalInterface
public interface ArraySupplier<T> {
    T[] get();

    @SafeVarargs
    static <T> ArraySupplier<T> of(T... elements) {
        return () -> elements;
    }
}