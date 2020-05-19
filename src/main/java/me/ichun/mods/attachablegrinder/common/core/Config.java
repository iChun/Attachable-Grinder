package me.ichun.mods.attachablegrinder.common.core;

import me.ichun.mods.attachablegrinder.common.AttachableGrinder;
import me.ichun.mods.ichunutil.common.config.ConfigBase;
import me.ichun.mods.ichunutil.common.config.annotations.CategoryDivider;
import me.ichun.mods.ichunutil.common.config.annotations.Prop;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Config extends ConfigBase
{
    @CategoryDivider(name = "gameplay")
    @Prop(min = 0, comment = "Number of ticks until the next discharge. Lower = faster. # 20 ticks = 1 second. Default: 100 (5 seconds)")
    public int frequency = 100;

    @Prop(min = 0, comment = "Multiplier that is multiplied against Toss Power.\nDefault: 4")
    public int tossMultiplier = 4;

    @Prop(min = 0, comment = "Each grinder will yield at least this many times before exploding.\nSet to 0 for infinite.\nDefault: 8")
    public int grinderYield = 8;

    @Prop(min = 0, comment = "Each grinder has a minimum chance to yield this many times.\nDefault: 8")
    public int randomExtraYield = 8;

    @Prop(min = 0, comment = "Power at which the drop is tossed from the mob.\nDefault: 5")
    public int tossPower = 5;//divide by 100F;

    @Prop(min = 0, comment = "Magnitude of explosion caused by failed grinding. Default creeper explosion magnitude is 300.\nDefault: 150")
    public int explosionMagnitude = 150;//divide by 100F;

    @Prop(comment = "Entities to disable from having the attachable grinder attached. By resource name (eg: minecraft:pig)")
    public List<String> disabledEntities = new ArrayList<>();

    public Config(@Nonnull String fileName)
    {
        super(fileName);
    }

    @Nonnull
    @Override
    public String getModId()
    {
        return AttachableGrinder.MOD_ID;
    }

    @Nonnull
    @Override
    public String getConfigName()
    {
        return AttachableGrinder.MOD_NAME;
    }

    @Nonnull
    public ModConfig.Type getConfigType()
    {
        return ModConfig.Type.COMMON;
    }
}
