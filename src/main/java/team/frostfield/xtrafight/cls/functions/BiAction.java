package team.frostfield.xtrafight.cls.functions;

@FunctionalInterface
public interface BiAction<A, B> {
    void act(A a, B b);
}
