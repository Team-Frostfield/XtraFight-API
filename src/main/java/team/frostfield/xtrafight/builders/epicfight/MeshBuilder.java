package team.frostfield.xtrafight.builders.epicfight;

import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.api.asset.JsonAssetLoader;
import yesman.epicfight.api.client.model.ClassicMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.Meshes.MeshContructor;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.SkinnedMesh.SkinnedMeshPart;
import yesman.epicfight.api.client.model.VertexBuilder;
import yesman.epicfight.client.mesh.HumanoidMesh;

import java.util.function.Function;

public class MeshBuilder {
    private MeshBuilder() {}

    // Standard Overloads for ResourceLocation
    public static <ME extends SkinnedMesh> Meshes.MeshAccessor<ME> newMesh(ResourceLocation location, MeshType type) {
        return toAccessor(location.getNamespace(), location.getPath(), type);
    }

    public static <ME extends SkinnedMesh> Meshes.MeshAccessor<ME> newMesh(String parsedLocation, MeshType type) {
        ResourceLocation location = ResourceLocation.parse(parsedLocation);
        return toAccessor(location.getNamespace(), location.getPath(), type);
    }

    public static <ME extends SkinnedMesh> Meshes.MeshAccessor<ME> newMesh(String modid, String path, MeshType type) {
        return toAccessor(modid, path, type);
    }

    public static <ME extends SkinnedMesh> Meshes.MeshAccessor<ME> newMesh(String modid, String path) {
        return toAccessor(modid, path, MeshType.SKINNED);
    }

    public static <ME extends SkinnedMesh> Meshes.MeshAccessor<ME> newMesh(ResourceLocation location) {
        return toAccessor(location.getNamespace(), location.getPath(), MeshType.SKINNED);
    }

    public static <ME extends SkinnedMesh> Meshes.MeshAccessor<ME> newMesh(String parsedLocation) {
        ResourceLocation parsed = ResourceLocation.parse(parsedLocation);
        return toAccessor(parsed.getNamespace(), parsed.getPath(), MeshType.SKINNED);
    }

    private static <ME extends Mesh> Meshes.MeshAccessor<ME> toAccessor(String modid, String path, MeshType type) {
        return Meshes.MeshAccessor.create(modid, path, type::load);
    }

    public record MeshType(Function<JsonAssetLoader, Mesh> factory) {
        // used for entities since it can contain weight data and more
        public static final MeshType SKINNED = new MeshType(loader -> loader.loadSkinnedMesh(SkinnedMesh::new));
        public static final MeshType HUMANOID = new MeshType(loader -> loader.loadSkinnedMesh(HumanoidMesh::new));
        // used for cloth objects mostly?
        public static final MeshType COMPOSITE = new MeshType(JsonAssetLoader::loadCompositeMesh);
        // mostly used for projectiles, such as the laser beam
        public static final MeshType CLASSIC = new MeshType(loader -> loader.loadClassicMesh(ClassicMesh::new));
        // used to read the mesh loaded inside the .json itself (it needs to contain "mesh_loader" provider!)
        public static final MeshType FROM_JSON = new MeshType(JsonAssetLoader::loadMesh);

        public static MeshType of(MeshContructor<SkinnedMeshPart, VertexBuilder, SkinnedMesh> meshContructor) {
            return new MeshType(jsonAssetLoader -> jsonAssetLoader.loadSkinnedMesh(meshContructor));
        }

        @SuppressWarnings("unchecked")
        public <M extends Mesh> M load(JsonAssetLoader loader) {
            return (M) this.factory.apply(loader);
        }

    }
}