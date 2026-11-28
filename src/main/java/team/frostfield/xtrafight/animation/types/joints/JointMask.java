package team.frostfield.xtrafight.animation.types.joints;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.client.animation.property.JointMaskEntry;
import yesman.epicfight.api.utils.ExtendableEnum;
import yesman.epicfight.api.utils.ExtendableEnumManager;

public interface JointMask extends ExtendableEnum {
    ExtendableEnumManager<JointMask> ENUM_MANAGER = new ExtendableEnumManager<>("joint_mask");

    Joint[] getJoints();

    JointMaskEntry getAsEntry();

}
