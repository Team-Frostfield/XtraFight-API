package team.frostfield.xtrafight.cls.lists;

import java.util.List;
import java.util.function.Consumer;

public interface BasicList<T> extends List<T> {
    boolean addAsArray(T... array);

    boolean addOnlyOnce(T oneT);

    boolean removeAsArray(T... array);

    boolean containsAllArray(T... all);

    boolean containsAny(T... all);

    T[] getArray(Class<T> clazz);

    Object[] getObjectArray();

    T[] toArray();

    void forEach(Consumer<? super T> tConsumer);
}