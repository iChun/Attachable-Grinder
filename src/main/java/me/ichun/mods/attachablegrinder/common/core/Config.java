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
    @Prop(min = 0)
    public int frequency = 100;

    @Prop(min = 0)
    public int tossMultiplier = 4;

    @Prop(min = 0)
    public int grinderYield = 8;

    @Prop(min = 0)
    public int randomExtraYield = 8;

    @Prop(min = 0)
    public int tossPower = 5;//divide by 100F;

    @Prop(min = 0)
    public int explosionMagnitude = 150;//divide by 100F;

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
