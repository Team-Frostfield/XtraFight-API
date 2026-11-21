package team.frostfield.xtrafight.builders.minecraft;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import yesman.epicfight.api.utils.math.Vec2f;

public class EntityBuilder {
    private final EntityType.Builder<?> builder;

    private EntityBuilder(EntityType.Builder<?> originalBuilder) {
        this.builder = originalBuilder;
    }

    public static <T extends Entity> EntityBuilder newEntity(EntityType.EntityFactory<T> constructor, MobCategory category) {
        EntityType.Builder<T> entityType = EntityType.Builder.of(constructor, category);
        return new EntityBuilder(entityType);
    }

    public EntityBuilder setImmuneToFire(boolean val) {
        this.builder.fireImmune = val;
        return this;
    }

    public EntityBuilder sized(Vec2f vector) {
        this.builder.sized(vector.x, vector.y);
        return this;
    }

    public EntityBuilder sized(float width, float height) {
        return this.sized(new Vec2f(width, height));
    }

    public EntityBuilder shouldSpawnFarFromPlayer(boolean val) {
        this.builder.canSpawnFarFromPlayer = val;
        return this;
    }

    public EntityBuilder shouldSave(boolean val) {
        this.builder.serialize = val;
        return this;
    }

    public EntityBuilder shouldBeSummonable(boolean val) {
        this.builder.summon = val;
        return this;
    }

    public EntityBuilder immuneToBlocks(Block... blocks) {
        this.builder.immuneTo(blocks);
        return this;
    }

    public Builder<?> getOriginalBuilder() {
        return this.builder;
    }
}
