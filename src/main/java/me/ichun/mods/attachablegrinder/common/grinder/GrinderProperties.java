package me.ichun.mods.attachablegrinder.common.grinder;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import me.ichun.mods.ichunutil.common.resource.ResourceReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class GrinderProperties extends ResourceReloadListener<GrinderProperties.Properties>
{
    public GrinderProperties()
    {
        super("grinder", GrinderProperties.Properties.class);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> json, IResourceManager iResourceManager, IProfiler iProfiler)
    {
        super.apply(json, iResourceManager, iProfiler);
    }

    public Properties getFor(@Nonnull ResourceLocation key)
    {
        for(Map.Entry<ResourceLocation, Properties> entry : objects.entrySet())
        {
            Properties v = entry.getValue();
            if(v.entity.equals(key.toString()))
            {
                return v;
            }
        }
        return null;
    }

    public static class Properties
    {
        public String entity;
        @SerializedName("loot_table")
        public String lootTable;
        @SerializedName("explosion_bonus")
        public double explosionBonus;
        @SerializedName("orientation_is_vertical")
        public boolean isVertical;
        public boolean flip;
        @SerializedName("render_scale")
        public double renderScale;
        @SerializedName("offset_up")
        public double offsetUp;
        @SerializedName("offset_left")
        public double offsetLeft;
        @SerializedName("offset_front")
        public double offsetFront;

        public Properties(String entity, String lootTable, boolean isVertical, boolean flip, double offsetUp, double offsetLeft, double offsetFront, double explosionBonus, double renderScale)
        {
            this.entity = entity;
            this.lootTable = lootTable;
            this.isVertical = isVertical;
            this.flip = flip;
            this.offsetUp = offsetUp;
            this.offsetLeft = offsetLeft;
            this.offsetFront = offsetFront;
            this.explosionBonus = explosionBonus;
            this.renderScale = renderScale;
        }

        public PropertiesClient toClient()
        {
            return new PropertiesClient(entity, isVertical, flip, renderScale, offsetUp, offsetLeft, offsetFront);
        }
    }

    public static class PropertiesClient
    {
        @SerializedName("e")
        public String entity;
        @SerializedName("v")
        public boolean isVertical;
        @SerializedName("p")
        public boolean flip;
        @SerializedName("r")
        public double renderScale;
        @SerializedName("u")
        public double offsetUp;
        @SerializedName("l")
        public double offsetLeft;
        @SerializedName("f")
        public double offsetFront;

        public PropertiesClient(String entity, boolean isVertical, boolean flip, double renderScale, double offsetUp, double offsetLeft, double offsetFront)
        {
            this.entity = entity;
            this.isVertical = isVertical;
            this.flip = flip;
            this.renderScale = renderScale;
            this.offsetUp = offsetUp;
            this.offsetLeft = offsetLeft;
            this.offsetFront = offsetFront;
        }

        public Properties toServer()
        {
            return new Properties(entity, "", isVertical, flip, offsetUp, offsetLeft, offsetFront, 0D, renderScale);
        }
    }
}
