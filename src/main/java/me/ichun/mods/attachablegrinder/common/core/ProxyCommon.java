package me.ichun.mods.attachablegrinder.common.core;

import me.ichun.mods.attachablegrinder.common.Grinder;
import me.ichun.mods.attachablegrinder.common.entity.EntityGrinder;
import me.ichun.mods.attachablegrinder.common.item.ItemGrinder;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ProxyCommon
{
	public void preInitMod()
	{
		Grinder.itemGrinder = GameRegistry.register(new ItemGrinder());

		EntityRegistry.registerModEntity(EntityGrinder.class, "entityGrinder", 60, Grinder.instance, 160, Integer.MAX_VALUE, true);

		GameRegistry.addRecipe(new ItemStack(Grinder.itemGrinder, 1),
				"X#X", "#R#", "X#X", '#', Items.IRON_INGOT, 'X', Items.BRICK, 'R', Items.REDSTONE);
	}

	public void initRenders()
	{
	}
}
