package team.frostfield.xtrafight.utils.managers;

import net.minecraft.server.packs.resources.ResourceManager;
import yesman.epicfight.api.client.model.Meshes;

public class MeshesManager {

    private static ResourceManager manager;
    private static boolean isManagerSet = false;

    private MeshesManager() {}

    public static void setManager(ResourceManager manager) {
        MeshesManager.manager = manager;
        isManagerSet = true;
    }

    public static void reloadMeshes() {
        if (!isManagerSet) throw new IllegalCallerException("Called reloadMeshes() too early!");
        Meshes.reload(manager);
    }
}