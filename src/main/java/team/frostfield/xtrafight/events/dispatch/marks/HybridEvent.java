package team.frostfield.xtrafight.events.dispatch.marks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// marked on the events that can be
// either posted by a @SubscribeEvent annotation or dispatched by the EventDispatcher
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HybridEvent {}
