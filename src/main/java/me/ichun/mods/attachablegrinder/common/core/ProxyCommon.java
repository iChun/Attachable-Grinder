package me.ichun.mods.attachablegrinder.common.core;

import me.ichun.mods.attachablegrinder.common.Grinder;
import me.ichun.mods.attachablegrinder.common.entity.EntityGrinder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ProxyCommon
{
	public void preInitMod()
	{
		EntityRegistry.registerModEntity(new ResourceLocation("attachablegrinder", "entityGrinder"), EntityGrinder.class, "entityGrinder", 60, Grinder.instance, 160, Integer.MAX_VALUE, true);
	}
}
