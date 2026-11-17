package team.frostfield.xtrafight.network.interfaces;

@FunctionalInterface
public interface StreamMemberDecoder<O, T> {
    void decode(T value, O output);
}