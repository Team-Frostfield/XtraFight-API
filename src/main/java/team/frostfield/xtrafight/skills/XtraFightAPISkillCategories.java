package team.frostfield.xtrafight.skills;

import yesman.epicfight.skill.SkillCategory;

public enum XtraFightAPISkillCategories implements SkillCategory {
    HEAVY_ATTACK(false, false, false),
    COUNTER_ATTACK(false, false, false);

    private final boolean shouldSave;
    private final boolean synchronize;
    private final boolean learnable;
    private final int id;

    XtraFightAPISkillCategories(boolean shouldSave, boolean synchronize, boolean learnable) {
        this.shouldSave = shouldSave;
        this.synchronize = synchronize;
        this.learnable = learnable;
        this.id = SkillCategory.ENUM_MANAGER.assign(this);
    }

    @Override
    public boolean shouldSave() {
        return this.shouldSave;
    }

    @Override
    public boolean shouldSynchronize() {
        return this.synchronize;
    }

    @Override
    public boolean learnable() {
        return this.learnable;
    }

    @Override
    public int universalOrdinal() {
        return this.id;
    }
}
