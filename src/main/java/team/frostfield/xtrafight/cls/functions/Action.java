package team.frostfield.xtrafight.cls.functions;

@FunctionalInterface
public interface Action<T> {
    void act(T t);
}
