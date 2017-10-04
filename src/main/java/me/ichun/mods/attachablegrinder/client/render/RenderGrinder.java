package me.ichun.mods.attachablegrinder.client.render;

import me.ichun.mods.attachablegrinder.common.Grinder;
import me.ichun.mods.attachablegrinder.common.entity.EntityGrinder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;

public class RenderGrinder extends Render<EntityGrinder>
{
    public RenderGrinder(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(EntityGrinder grinder, double d, double d1, double d2, float f, float f1)
    {
        if(grinder.animal != null)
        {
            if(grinder.animal instanceof EntityPlayer && grinder.animal.isSneaking())
            {
                return;
            }
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();

            GlStateManager.translate(d + (grinder.animal.posX - grinder.posX), d1 + (grinder.animal.posY - grinder.posY), d2 + (grinder.animal.posZ - grinder.posZ));

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            this.bindEntityTexture(grinder);

            float yOffset = 1.0F;
            double scale = 0.6D;
            float rendYawOffset = grinder.animal.prevRenderYawOffset + (grinder.animal.renderYawOffset - grinder.animal.prevRenderYawOffset) * f1;

            boolean isWitherSkele = false;
            if(grinder.animal instanceof EntityPig)
            {
                EntityPig pig = (EntityPig)grinder.animal;
                if(pig.getSaddled())
                {
                    yOffset = 1.05F;
                }
            }
            if(grinder.animal instanceof EntityCow)
            {
                yOffset = 1.82F;
            }
            else if(grinder.animal instanceof EntityCreeper)
            {
                yOffset = 2.02F;
                scale = 0.45D;
            }
            else if(grinder.animal instanceof EntityZombie || grinder.animal instanceof EntityPlayer)
            {
                yOffset = 2.52F;
                scale = 0.45D;
            }
            else if(grinder.animal instanceof AbstractSkeleton)
            {
                AbstractSkeleton skele = (AbstractSkeleton)grinder.animal;
                if(skele instanceof EntityWitherSkeleton)
                {
                    yOffset = 3.2F;
                    scale = 0.5D;
                    isWitherSkele = true;
                }
                else
                {
                    yOffset = 2.82F;
                    scale = 0.45D;
                }
            }
            else if(grinder.animal instanceof EntityChicken)
            {
                yOffset = 1.9F;
                scale = 0.3F;
            }
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0.0F, yOffset, 0.0F);

            while(rendYawOffset < 0F)
            {
                rendYawOffset += 360F;
            }

            rendYawOffset = rendYawOffset % 360F;

            GlStateManager.rotate(-rendYawOffset, 0.0F, 1.0F, 0.0F);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.disableLighting();
            RenderHelper.disableStandardItemLighting();
            if(grinder.animal instanceof EntityCow || grinder.animal instanceof EntityPig || grinder.animal instanceof EntityChicken)
            {
                renderHorizontal(tessellator, bufferbuilder, -0.5D, -0.495D, -0.5D, Grinder.grinderSides);

                float tick = (float)(grinder.ticks % 5) + f1;

                GlStateManager.rotate(-(tick / 5F * 90F), 0.0F, 1.0F, 0.0F);

                renderHorizontal(tessellator, bufferbuilder, -0.5D, -0.5D, -0.5D, Grinder.grinderBlades);
            }
            else
            {
                renderVertical(tessellator, bufferbuilder, -0.5D, -0.5D, isWitherSkele ? -0.65D : -0.71D, Grinder.grinderSides);

                float tick = (float)(grinder.ticks % 5) + f1;

                GlStateManager.rotate(-(tick / 5F * 90F), 0.0F, 0.0F, 1.0F);

                renderVertical(tessellator, bufferbuilder, -0.5D, -0.5D, isWitherSkele ? -0.655D : -0.715D, Grinder.grinderBlades);
            }
            GlStateManager.enableLighting();

            GlStateManager.disableBlend();

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
        }
    }

    public void renderVertical(Tessellator tessellator, BufferBuilder bufferbuilder, double p_147734_2_, double p_147734_4_, double p_147734_6_, TextureAtlasSprite p_147734_8_)
    {
        double d3 = (double)p_147734_8_.getInterpolatedU(0.0D * 16.0D);
        double d4 = (double)p_147734_8_.getInterpolatedU(1.0D * 16.0D);
        double d5 = (double)p_147734_8_.getInterpolatedV(16.0D - 1.0D * 16.0D);
        double d6 = (double)p_147734_8_.getInterpolatedV(16.0D - 0.0D * 16.0D);

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        double d11 = p_147734_2_ + 0.0D;
        double d12 = p_147734_2_ + 1.0D;
        double d13 = p_147734_4_ + 0.0D;
        double d14 = p_147734_4_ + 1.0D;
        double d15 = p_147734_6_ + 1.0D;

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(d11, d14, d15).tex(d3, d5).endVertex();
        bufferbuilder.pos(d11, d13, d15).tex(d8, d10).endVertex();
        bufferbuilder.pos(d12, d13, d15).tex(d4, d6).endVertex();
        bufferbuilder.pos(d12, d14, d15).tex(d7, d9).endVertex();
        tessellator.draw();
    }

    public void renderHorizontal(Tessellator tessellator, BufferBuilder bufferbuilder, double p_147806_2_, double p_147806_4_, double p_147806_6_, TextureAtlasSprite p_147806_8_)
    {
        double d3 = (double)p_147806_8_.getInterpolatedU(0.0D * 16.0D);
        double d4 = (double)p_147806_8_.getInterpolatedU(1.0D * 16.0D);
        double d5 = (double)p_147806_8_.getInterpolatedV(0.0D * 16.0D);
        double d6 = (double)p_147806_8_.getInterpolatedV(1.0D * 16.0D);

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        double d11 = p_147806_2_ + 0.0D;
        double d12 = p_147806_2_ + 1.0D;
        double d13 = p_147806_4_ + 1.0D;
        double d14 = p_147806_6_ + 0.0D;
        double d15 = p_147806_6_ + 1.0D;

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(d12, d13, d15).tex(d4, d6).endVertex();
        bufferbuilder.pos(d12, d13, d14).tex(d7, d9).endVertex();
        bufferbuilder.pos(d11, d13, d14).tex(d3, d5).endVertex();
        bufferbuilder.pos(d11, d13, d15).tex(d8, d10).endVertex();
        tessellator.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGrinder par1Entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public static class RenderFactory implements IRenderFactory<EntityGrinder>
    {
        @Override
        public Render<EntityGrinder> createRenderFor(RenderManager manager)
        {
            return new RenderGrinder(manager);
        }
    }
}
