package team.frostfield.xtrafight.animation;

import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.utils.datastructure.ParameterizedHashMap;

public class XtraFightAPIAnimationStates extends EntityState {

    public static final StateFactor<Boolean> CAN_COUNTER_ATTACK = new StateFactor<>("canCounter", false);
    public static final StateFactor<Boolean> CAN_HEAVY_ATTACK = new StateFactor<>("canCounter", false);

    public XtraFightAPIAnimationStates(ParameterizedHashMap<StateFactor<?>> states) {
        super(states);
    }

}
