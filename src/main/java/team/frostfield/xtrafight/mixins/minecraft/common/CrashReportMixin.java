package team.frostfield.xtrafight.mixins.minecraft.common;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.List;
import java.util.Random;

@Mixin(value = CrashReport.class, remap = false)
public abstract class CrashReportMixin {

    @Shadow @Final @Mutable
    private String title;

    @Shadow @Final @Mutable
    private List<CrashReportCategory> details;


    // Access the private title field inside CrashReportCategory
    @Mixin(value = CrashReportCategory.class, remap = false)
    private interface CrashReportCategoryAccessor {
        @Accessor("title")
        String xtrafightAPI$getTitle();
    }

    @Unique
    private static final String[] RANDOM_MESSAGES = {
            "XtraFight-API is not xtra after all huh?",
            "This crash is definitely from XtraFight API. Go yell at the team here: https://github.com/Team-Frostfield/XtraFight-API/issues",
            "An animation frame tried to divide by zero. Please hand this log over to the authorities: https://github.com/Team-Frostfield/XtraFight-API/issues",
            "The combos were too xtra for the engine to handle. Report this absolute disaster!: https://github.com/Team-Frostfield/XtraFight-API/issues",
            "Don't panic, but a compiler gremlin just broke the combat pipeline. Open an issue at https://github.com/Team-Frostfield/XtraFight-API/issues!",
            "Local developer forgets how index boundaries work. Go roast them on GitHub: https://github.com/Team-Frostfield/XtraFight-API/issues"
    };

    @Inject(method = "saveToFile", at = @At("HEAD"))
    private void xtrafight$injectBlameHeader(File p_127513_, CallbackInfoReturnable<Boolean> cir) {
        try {
            CrashReport report = (CrashReport) (Object) this;
            boolean isXtraAtFault = false;

            // Check mid-game exception stack traces
            if (report.getException() != null) {
                Throwable currentThrowable = report.getException();
                while (currentThrowable != null) {
                    for (StackTraceElement element : currentThrowable.getStackTrace()) {
                        if (element.getClassName().contains("team.frostfield.xtrafight")) {
                            isXtraAtFault = true;
                            break;
                        }
                    }
                    if (isXtraAtFault) break;
                    currentThrowable = currentThrowable.getCause();
                }
            }

            // Check mod loading category descriptions (For early bootstrap failures)
            if (!isXtraAtFault) {

                if (this.details != null) {
                    for (CrashReportCategory category : this.details) {
                        String catTitle = ((CrashReportCategoryAccessor) category).xtrafightAPI$getTitle();
                        if (catTitle != null && catTitle.contains("xtrafight")) {
                            isXtraAtFault = true;
                            break;
                        }
                    }
                }
            }

            // Overwrite the main description title to include our message banner at the top
            if (isXtraAtFault && this.title != null && !this.title.contains("XtraFight-API Faced an Error:")) {
                Random rand = new Random();
                String chosenMessage = RANDOM_MESSAGES[rand.nextInt(RANDOM_MESSAGES.length)];

                // Format the top banner block neatly right into the title field string stream

                this.title = this.title + "\n\n" +
                        "XtraFight-API Faced an Error: " + chosenMessage + "\n";
            }
        } catch (Throwable ignored) {
            // Fail-safe wrapper
        }
    }
}