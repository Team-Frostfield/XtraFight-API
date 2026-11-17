package team.frostfield.xtrafight.cls.loadable.enums;

import team.frostfield.xtrafight.cls.loadable.ILoadableClass;

public interface LoadableEnum {
    Class<? extends ILoadableClass> getLoadableClass();

    boolean isClientOnly();

}
