package team.frostfield.xtrafight.cls.functions;

@FunctionalInterface
public interface TriAction<A, B, C> {
    void act(A a, B b, C c);
}
