package udary.tfcplusudarymod.render.item;

import com.dunk.tfc.api.Interfaces.IEquipable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;
import udary.tfcplusudarymod.interfaces.IRender;
import udary.tfcplusudarymod.items.tools.ItemTuckerBag;
import udary.tfcplusudarymod.render.models.ModelTuckerBag;

import javax.annotation.Resource;

public class RenderTuckerBag implements IRender
{
	protected static final ResourceLocation texture_I = new ResourceLocation(ModDetails.ModID, "textures/models/tools/tuckerbag.png");
	protected static final ResourceLocation texture_II = new ResourceLocation(ModDetails.ModID, "textures/models/tools/tuckerbagII.png");
	
	protected ModelTuckerBag model = new ModelTuckerBag();

	public void render(Entity entity, ItemStack is)
	{
		if (entity instanceof EntityPlayer && is != null)
			this.doRender(entity, is);
	}
	
	protected void doRender(Entity entity, ItemStack is)
	{
		if (entity == null || is == null || !(is.getItem() instanceof ItemTuckerBag))
			return;
		
		ItemTuckerBag tuckerBag = (ItemTuckerBag)is.getItem();
		if (!EnumTuckerBagVersion.isValid(tuckerBag.getBagVersion(is)))
			return;
			
        GL11.glPushMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (tuckerBag.getBagVersion(is) == EnumTuckerBagVersion.VERSION_2) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture_II);
		} else {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture_I);
		}
        
		if (is.getItem() instanceof IEquipable)
			((IEquipable)is.getItem()).onEquippedRender();

		if (tuckerBag.hasEntity(is))
	        model.renderFull(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		else
	        model.render(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);

        GL11.glPopMatrix();
	}

	private double interpolatePositions(double prev, double current, float partialRenderTick) {
		return prev + (current - prev) * (double)partialRenderTick;
	}

	public void doRender(Entity entity, ItemStack item, float partialRenderTick) {
		ItemTuckerBag tuckerBag = (ItemTuckerBag)item.getItem();
		if (!EnumTuckerBagVersion.isValid(tuckerBag.getBagVersion(item)))
			return;

		float entityTranslateY = entity instanceof EntityPlayer ? 0.0F : -1.5F;
		GL11.glPushMatrix();

		if (tuckerBag.getBagVersion(item) == EnumTuckerBagVersion.VERSION_2) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture_II);
		} else {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture_I);
		}

		EntityPlayer cp = Minecraft.getMinecraft().thePlayer;
		if (!cp.equals(entity) && entity instanceof EntityPlayer) {
			GL11.glTranslated(-this.interpolatePositions(cp.prevPosX, cp.posX, partialRenderTick), -this.interpolatePositions(cp.prevPosY, cp.posY, partialRenderTick), -this.interpolatePositions(cp.prevPosZ, cp.posZ, partialRenderTick));
		}

		if (!entity.isSneaking()) {
			GL11.glTranslatef(0.0F, entityTranslateY + 0.0F, 0.1F);
		} else {
			GL11.glTranslatef(0.0F, entityTranslateY + 0.1F, 0.1F);
			GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
		}

		if (item.getItem() instanceof IEquipable) {
			((IEquipable) item.getItem()).onEquippedRender();
		}

		if (tuckerBag.hasEntity(item))
			model.renderFull(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		else
			model.render(entity, 0F, 0F, 0F, 0F, 0F, 0.0625F);

		GL11.glPopMatrix();
	}

	public void render(Entity entity, ItemStack item, float partialRenderTick) {
		this.doRender(entity, item, partialRenderTick);
	}
}
