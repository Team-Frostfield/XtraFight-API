package team.frostfield.xtrafight.animation;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import team.frostfield.xtrafight.animation.types.SimpleAttackAnimation;
import yesman.epicfight.api.animation.property.AnimationProperty.StaticAnimationProperty;

public class SimpleAnimationProperty<T> extends StaticAnimationProperty<T> {

    public static final SimpleAnimationProperty<Float> PLAY_SPEED = new SimpleAnimationProperty<>("play_speed", Codec.FLOAT);
    public static final SimpleAnimationProperty<SimpleAttackAnimation.TrailPreset> TRAIL_PRESET = new SimpleAnimationProperty<>("trail_preset");
    public static final SimpleAnimationProperty<SimpleAttackAnimation.TrailColor> TRAIL_COLOR = new SimpleAnimationProperty<>("trail_color");

    public SimpleAnimationProperty(String name, @Nullable Codec<T> codecs) {
        super(name, codecs);
    }

    public SimpleAnimationProperty(String name) {
        this(name, null);
    }
}
