package team.frostfield.xtrafight.animation.client;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import yesman.epicfight.api.asset.JsonAssetLoader;
import yesman.epicfight.api.exception.AssetLoadingException;
import yesman.epicfight.api.utils.ParseUtil;

public class ExtendedJsonAssetLoader extends JsonAssetLoader {

    public ExtendedJsonAssetLoader(JsonObject rootJson, ResourceLocation rl) {
        super(rootJson, rl);
        // TODO Auto-generated constructor stub
    }

    public ExtendedJsonAssetLoader(ResourceManager manager, ResourceLocation rl) throws AssetLoadingException {
        super(manager, rl);
    }

 @Nullable 
 public FovTrack getFovTrack(){
    JsonObject root = this.getRootJson();

  if(!root.has("fov") || !root.get("fov").isJsonObject()){
    return null;
  }

  JsonObject fov = root.getAsJsonObject("fov");
  JsonArray timeJsonArray = GsonHelper.convertToJsonArray(fov.get("time"), "time");
  float times[] = ParseUtil.toFloatArrayPrimitive(timeJsonArray);
  JsonArray valuesJsonArray = GsonHelper.convertToJsonArray(fov.get("value"), "value");
  float values[] = ParseUtil.toFloatArrayPrimitive(valuesJsonArray);

  return new FovTrack(times, values);

 }

}