package me.ichun.mods.attachablegrinder.client.core;

import me.ichun.mods.attachablegrinder.client.render.RenderGrinder;
import me.ichun.mods.attachablegrinder.common.Grinder;
import me.ichun.mods.attachablegrinder.common.core.ProxyCommon;
import me.ichun.mods.attachablegrinder.common.entity.EntityGrinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInitMod()
    {
        super.preInitMod();

        RenderingRegistry.registerEntityRenderingHandler(EntityGrinder.class, new RenderGrinder.RenderFactory());
    }

    @Override
    public void initRenders()
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Grinder.itemGrinder, 0, new ModelResourceLocation("attachablegrinder:Grinder", "inventory"));
    }
}
