package me.ichun.mods.attachablegrinder.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.ichun.mods.attachablegrinder.common.AttachableGrinder;
import me.ichun.mods.attachablegrinder.common.entity.GrinderEntity;
import me.ichun.mods.attachablegrinder.common.grinder.GrinderProperties;
import me.ichun.mods.ichunutil.client.render.LatchedEntityRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class GrinderRenderer extends LatchedEntityRenderer<GrinderEntity>
{
    public static final ResourceLocation SIDES = new ResourceLocation(AttachableGrinder.MOD_ID, "textures/model/grinder_ent_sides.png");
    public static final ResourceLocation BLADES = new ResourceLocation(AttachableGrinder.MOD_ID, "textures/model/grinder_ent_blade.png");
    public static final RenderType RENDER_TYPE_SIDE = RenderType.getEntityCutoutNoCull(SIDES);
    public static final RenderType RENDER_TYPE_BLADE = RenderType.getEntityCutoutNoCull(BLADES);

    public GrinderRenderer(EntityRendererManager manager)
    {
        super(manager);
    }

    @Override
    public void render(GrinderEntity grinder, float entityYaw, float partialTick, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        GrinderProperties.Properties properties = grinder.properties;
        if(properties != null) //properties are only set when the entity has a parent.
        {
            Pose pose = grinder.parent.getPose(); //render if entity is sleeeeeeeeping?
            if (pose == Pose.SLEEPING || grinder.parent.isInvisible())
            {
                return;
            }

            //we need the parent's rendering position, not the grinder's
            //we need to get the camera position. Get the current active render info
            Vector3d vec3d = this.renderManager.info.getProjectedView();
            double camX = vec3d.getX();
            double camY = vec3d.getY();
            double camZ = vec3d.getZ();

            //taken from EntityRendererManager
            double d0 = MathHelper.lerp((double)partialTick, grinder.parent.lastTickPosX, grinder.parent.getPosX());
            double d1 = MathHelper.lerp((double)partialTick, grinder.parent.lastTickPosY, grinder.parent.getPosY());
            double d2 = MathHelper.lerp((double)partialTick, grinder.parent.lastTickPosZ, grinder.parent.getPosZ());
            float f = MathHelper.lerp(partialTick, grinder.parent.prevRotationYaw, grinder.parent.rotationYaw);

            matrixStackIn.pop(); // pop our current translate

            //      this.renderManager.renderEntityStatic(entityIn, d0 - camX, d1 - camY, d2 - camZ, f, partialTicks, matrixStackIn, bufferIn, this.renderManager.getPackedLight(entityIn, partialTicks));
            int parentPackedLight = this.renderManager.getPackedLight(grinder.parent, partialTick);
            Vector3d renderOffset = this.getRenderOffset(grinder, partialTick);
            double pX = d0 - camX + renderOffset.getX();
            double pY = d1 - camY + renderOffset.getY();
            double pZ = d2 - camZ + renderOffset.getZ();

            //we can render
            matrixStackIn.push(); // push to get to our entity's position
            matrixStackIn.translate(pX, pY, pZ); //translate to our parent.

            float renderYaw = MathHelper.interpolateAngle(partialTick, grinder.parent.prevRenderYawOffset, grinder.parent.renderYawOffset);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - renderYaw));

            //do border
            matrixStackIn.push();

            matrixStackIn.translate(-properties.offsetLeft, properties.offsetUp, -properties.offsetFront);
            if(!properties.isVertical)
            {
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90F));
            }
            if(properties.renderScale != 1D && properties.renderScale > 0D)
            {
                matrixStackIn.scale((float)properties.renderScale, (float)properties.renderScale, (float)properties.renderScale);
            }

            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE_SIDE);
            MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
            Matrix4f matrix4f = matrixstack$entry.getMatrix();
            Matrix3f matrix3f = matrixstack$entry.getNormal();

            ivertexbuilder.pos(matrix4f, -0.5F, -0.5F, 0F).color(255, 255, 255, 255).tex(0F, 0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            ivertexbuilder.pos(matrix4f, +0.5F, -0.5F, 0F).color(255, 255, 255, 255).tex(1F, 0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            ivertexbuilder.pos(matrix4f, +0.5F, +0.5F, 0F).color(255, 255, 255, 255).tex(1F, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            ivertexbuilder.pos(matrix4f, -0.5F, +0.5F, 0F).color(255, 255, 255, 255).tex(0F, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

            matrixStackIn.pop();
            //end do border

            matrixStackIn.translate(-properties.offsetLeft, properties.offsetUp * (properties.flip ? 1.001D : 0.999D), -properties.offsetFront);

            if(!properties.isVertical)
            {
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90F));
            }

            float tick = (grinder.ticks + partialTick) / 5F;
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-(tick * 90F)));

            if(properties.renderScale != 1D && properties.renderScale > 0D)
            {
                matrixStackIn.scale((float)properties.renderScale, (float)properties.renderScale, (float)properties.renderScale);
            }

            ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE_BLADE);
            matrixstack$entry = matrixStackIn.getLast();
            matrix4f = matrixstack$entry.getMatrix();
            matrix3f = matrixstack$entry.getNormal();

            ivertexbuilder.pos(matrix4f, -0.5F, -0.5F, 0F).color(255, 255, 255, 255).tex(0F, 0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            ivertexbuilder.pos(matrix4f, +0.5F, -0.5F, 0F).color(255, 255, 255, 255).tex(1F, 0F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            ivertexbuilder.pos(matrix4f, +0.5F, +0.5F, 0F).color(255, 255, 255, 255).tex(1F, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
            ivertexbuilder.pos(matrix4f, -0.5F, +0.5F, 0F).color(255, 255, 255, 255).tex(0F, 1F).overlay(OverlayTexture.NO_OVERLAY).lightmap(parentPackedLight).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();

            //            matrixStackIn.pop();
        }
    }

    @Override
    public Vector3d getRenderOffset(GrinderEntity grinder, float partialTicks)
    {
        if(grinder.properties != null && grinder.parent != null)
        {
            EntityRenderer<LivingEntity> renderer = (EntityRenderer<LivingEntity>)this.renderManager.getRenderer(grinder.parent);
            if(renderer != null)
            {
                return renderer.getRenderOffset(grinder.parent, partialTicks);
            }
        }
        return Vector3d.ZERO;
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
