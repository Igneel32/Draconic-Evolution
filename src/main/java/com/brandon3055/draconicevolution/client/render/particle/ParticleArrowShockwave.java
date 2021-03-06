package com.brandon3055.draconicevolution.client.render.particle;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCOBJParser;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Matrix4;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import com.brandon3055.brandonscore.client.particle.BCParticle;
import com.brandon3055.brandonscore.client.particle.IBCParticleFactory;
import com.brandon3055.brandonscore.lib.Vec3D;
import com.brandon3055.draconicevolution.client.handler.ClientEventHandler;
import com.brandon3055.draconicevolution.helpers.ResourceHelperDE;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Map;

/**
 * Created by brandon3055 on 17/05/2017.
 */
public class ParticleArrowShockwave extends BCParticle {
    private static CCModel model = null;
    public double size = 0;
    public double maxSize;

    public ParticleArrowShockwave(World worldIn, Vec3D pos) {
        super(worldIn, pos);
        if (model == null) {
            Map<String, CCModel> map = CCOBJParser.parseObjModels(ResourceHelperDE.getResource("models/reactor_core_model.obj"));
            model = CCModel.combine(map.values());
            model.apply(new Scale(1, 0.5, 1));
        }
    }

    public ParticleArrowShockwave(World worldIn, Vec3D pos, Vec3D speed) {
        this(worldIn, pos);
    }

    @Override
    public void onUpdate() {
        particleAge++;
        size += 1.2;
        if (size > maxSize * 1.2) {
            setExpired();
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    @Override
    public void renderParticle(VertexBuffer vertexbuffer, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.draw();

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0F);
        float a = (float) Math.max(0D, 0.5D - (((size + partialTicks) / (maxSize)) * 0.5D));
        GlStateManager.color(1F, 0.1F, 0F, a);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        float xx = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
        float yy = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
        float zz = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);


            double baseScale = size + partialTicks;

            GlStateManager.translate((double) xx + 0.5, (double) yy + 0.5, (double) zz + 0.5);

            for (int i = 10; i > 0; i--) {
                double scale = baseScale / i * 2D;

                GlStateManager.color(1F - (i / 5F), 0.1F, i / 8F, a);

                ccrs.startDrawing(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
                Matrix4 mat = RenderUtils.getMatrix(new Vector3(0, 0, 0), new Rotation((ClientEventHandler.elapsedTicks + partialTicks) / 40F, 0, 1, 0), -1 * scale);
                model.render(ccrs, mat);
                ccrs.draw();

            }



        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();

        vertexbuffer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

    }

    public static class Factory implements IBCParticleFactory {

        @Override
        public Particle getEntityFX(int particleID, World world, Vec3D pos, Vec3D speed, int... args) {
            ParticleArrowShockwave arrowShockwave = new ParticleArrowShockwave(world, pos, speed);

            world.playSound(pos.x, pos.y, pos.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 10, 0.9F + world.rand.nextFloat() * 0.2F, false);
            if (args.length >= 1){
                arrowShockwave.maxSize = args[0] / 100D;
            }
            else {
                arrowShockwave.maxSize = 10;
            }

            return arrowShockwave;
        }
    }
}
