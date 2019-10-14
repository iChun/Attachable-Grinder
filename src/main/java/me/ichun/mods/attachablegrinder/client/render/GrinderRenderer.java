package me.ichun.mods.attachablegrinder.client.render;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import me.ichun.mods.attachablegrinder.common.AttachableGrinder;
import me.ichun.mods.attachablegrinder.common.entity.GrinderEntity;
import me.ichun.mods.attachablegrinder.common.grinder.GrinderProperties;
import me.ichun.mods.ichunutil.client.render.LatchedEntityRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class GrinderRenderer extends LatchedEntityRenderer<GrinderEntity>
{
    public static final ResourceLocation SIDES = new ResourceLocation(AttachableGrinder.MOD_ID, "textures/model/grinder_ent_sides.png");
    public static final ResourceLocation BLADES = new ResourceLocation(AttachableGrinder.MOD_ID, "textures/model/grinder_ent_blade.png");

    public GrinderRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void doRender(GrinderEntity grinder, double x, double y, double z, float entityYaw, float partialTick)
    {
        GrinderProperties.Properties properties = grinder.properties;
        if(properties != null) //properties are only set when the entity has a parent.
        {
            Pose pose = grinder.parent.getPose(); //render if entity is sleeeeeeeeping?
            if (pose == Pose.SLEEPING || grinder.parent.isInvisible())
            {
                return;
            }

            GlStateManager.normal3f(0.0F, 0.0F, 1.0F); //fucking nameplates fuck up the normal without resetting.

            //we need the parent's rendering position, not the grinder's
            //taken from EntityRendererManager
            double d0 = MathHelper.lerp((double)partialTick, grinder.parent.lastTickPosX, grinder.parent.posX);
            double d1 = MathHelper.lerp((double)partialTick, grinder.parent.lastTickPosY, grinder.parent.posY);
            double d2 = MathHelper.lerp((double)partialTick, grinder.parent.lastTickPosZ, grinder.parent.posZ);
            float f = MathHelper.lerp(partialTick, grinder.parent.prevRotationYaw, grinder.parent.rotationYaw);
            int i = grinder.parent.getBrightnessForRender();
            if (grinder.parent.isBurning()) {
                i = 15728880;
            }

            int j = i % 65536;
            int k = i / 65536;
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float)j, (float)k);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            double px = d0 - renderManager.renderPosX;
            double py = d1 - renderManager.renderPosY;
            double pz = d2 - renderManager.renderPosZ;

            //set the brightness to match the parent
            EntityRenderer parentRenderer = renderManager.getRenderer(grinder.parent);
            boolean resetBrightness = false;
            if(parentRenderer instanceof LivingRenderer)
            {
                resetBrightness = ((LivingRenderer)parentRenderer).setDoRenderBrightness(grinder.parent, partialTick);
            }

            //we can render
            GlStateManager.disableCull();

            GlStateManager.pushMatrix();
            GlStateManager.translated(px, py, pz);

            float renderYaw = MathHelper.func_219805_h(partialTick, grinder.parent.prevRenderYawOffset, grinder.parent.renderYawOffset);
            GlStateManager.rotatef(180.0F - renderYaw, 0.0F, 1.0F, 0.0F);

            //do border
            GlStateManager.pushMatrix();

            GlStateManager.translated(-properties.offsetLeft, properties.offsetUp, -properties.offsetFront);
            if(!properties.isVertical)
            {
                GlStateManager.rotatef(-90F, 1.0F, 0.0F, 0.0F);
            }
            if(properties.renderScale != 1D && properties.renderScale > 0D)
            {
                GlStateManager.scaled(properties.renderScale, properties.renderScale, properties.renderScale);
            }

            bindTexture(SIDES);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-0.5D, -0.5D, 0D).tex(0D, 0D).endVertex();
            bufferbuilder.pos(+0.5D, -0.5D, 0D).tex(1D, 0D).endVertex();
            bufferbuilder.pos(+0.5D, +0.5D, 0D).tex(1D, 1D).endVertex();
            bufferbuilder.pos(-0.5D, +0.5D, 0D).tex(0D, 1D).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
            //end do border

            GlStateManager.translated(-properties.offsetLeft, properties.offsetUp * (properties.flip ? 1.001D : 0.999D), -properties.offsetFront);

            if(!properties.isVertical)
            {
                GlStateManager.rotatef(-90F, 1.0F, 0.0F, 0.0F);
            }

            float tick = (grinder.ticks + partialTick) / 5F;
            GlStateManager.rotatef(-(tick * 90F), 0.0F, 0.0F, 1.0F);

            if(properties.renderScale != 1D && properties.renderScale > 0D)
            {
                GlStateManager.scaled(properties.renderScale, properties.renderScale, properties.renderScale);
            }

            bindTexture(BLADES);

            tessellator = Tessellator.getInstance();
            bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-0.5D, -0.5D, 0D).tex(0D, 0D).endVertex();
            bufferbuilder.pos(+0.5D, -0.5D, 0D).tex(1D, 0D).endVertex();
            bufferbuilder.pos(+0.5D, +0.5D, 0D).tex(1D, 1D).endVertex();
            bufferbuilder.pos(-0.5D, +0.5D, 0D).tex(0D, 1D).endVertex();
            tessellator.draw();


            GlStateManager.popMatrix();

            GlStateManager.enableCull();

            //unsets the brightness by the parent renderer
            if(resetBrightness)
            {
                ((LivingRenderer)parentRenderer).unsetBrightness();
            }
        }
    }

    public static class RenderFactory implements IRenderFactory<GrinderEntity>
    {
        @Override
        public EntityRenderer<GrinderEntity> createRenderFor(EntityRendererManager manager)
        {
            return new GrinderRenderer(manager);
        }
    }
}
