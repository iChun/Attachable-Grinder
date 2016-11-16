package me.ichun.mods.attachablegrinder.common.core;

import me.ichun.mods.ichunutil.common.core.config.ConfigBase;
import me.ichun.mods.ichunutil.common.core.config.annotations.ConfigProp;
import me.ichun.mods.ichunutil.common.core.config.annotations.IntBool;
import me.ichun.mods.ichunutil.common.core.config.annotations.IntMinMax;

import java.io.File;

public class Config extends ConfigBase
{
    @ConfigProp
    @IntMinMax(min = 0)
    public int frequency = 100;

    @ConfigProp
    @IntMinMax(min = 0)
    public int tossMultiplier = 4;

    @ConfigProp
    @IntMinMax(min = 0)
    public int grinderYield = 8;

    @ConfigProp
    @IntMinMax(min = 0)
    public int randomExtraYield = 8;

    @ConfigProp
    @IntMinMax(min = 0)
    public int tossPower = 5;//divide by 100F;

    @ConfigProp
    @IntMinMax(min = 0)
    public int explosionMagnitude = 150;//divide by 100F;

    @ConfigProp(category = "grinderEnabledOn")
    @IntBool
    public int allowPig = 1;

    @ConfigProp(category = "grinderEnabledOn")
    @IntBool
    public int allowCow = 1;

    @ConfigProp(category = "grinderEnabledOn")
    @IntBool
    public int allowChicken = 1;

    @ConfigProp(category = "grinderEnabledOn")
    @IntBool
    public int allowZombie = 1;

    @ConfigProp(category = "grinderEnabledOn")
    @IntBool
    public int allowCreeper = 1;

    @ConfigProp(category = "grinderEnabledOn")
    @IntBool
    public int allowSkeleton = 1;

    @ConfigProp(category = "grinderEnabledOn")
    @IntBool
    public int allowPlayer = 1;

    public Config(File file)
    {
        super(file);
    }

    @Override
    public String getModId()
    {
        return "grinder";
    }

    @Override
    public String getModName()
    {
        return "Attachable Grinder";
    }
}
