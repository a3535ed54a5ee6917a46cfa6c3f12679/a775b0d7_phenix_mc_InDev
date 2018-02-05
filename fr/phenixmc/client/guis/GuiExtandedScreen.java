package fr.phenixmc.client.guis;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.phenixmc.client.PhenixMc;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * @author DreaMzy
 * @category Guis
 * 
 * Toutes les nouvelles classes créées pour le projet PhenixMc sont créées spécialement pour une utilisation commerciale.
 * L'utilisation de code "Open Source" permet de ne pas lier les projets personnels et les demandes commerciales.
 * Merci de votre compréhension.
 * 
 */
public class GuiExtandedScreen extends GuiScreen {
	
	/**
	 * Dessiner le fond du menu principal
	 */
	public void drawnGuiBackground(String path) {
    	GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(this.phenix.getResourcePath() + path)); 
        this.drawTexturedModalRectWithOptionnalSize(0, 0, 0, 0, this.width, this.height);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	/**
	 * Dessiner le fond du menu principal avec un dégradé de gris
	 */
	public void drawGrayGradientBackground() {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.drawGradientRect(0, 0, this.width, this.width, -14212316, -16185078);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	/**
	 * Dessiner une barre horizontale avec un dégradé de gris en position haute
	 */
	public void drawGradientTopBar() {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.drawGradientRect(0, 0, this.width, 15, -14212316, -16185078);
		this.drawGradientRect(0, 16, this.width, 15, -4102912, -147666);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	/**
	 * Dessiner une barre horizontale avec un dégradé de gris en position basse
	 */
	public void drawGradientBottomBar() {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.drawGradientRect(0, this.height, this.width, this.height - 15, -14212316, -16185078);
		this.drawGradientRect(0, this.height - 16, this.width, this.height - 15, -10545406, -10545406);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	/**
	 * Dessiner le logo du projet
	 */
	public void drawLogo(int x, int y, int width, int height, String path) {
		GL11.glEnable(GL11.GL_BLEND);
		this.mc.getTextureManager().bindTexture(new ResourceLocation(this.phenix.getResourcePath() + path));
		this.drawTexturedModalRectWithOptionnalSize(x, y, 0, 0, width, height);
		GL11.glEnable(GL11.GL_BLEND);
	}
	
    /**
     * Draws the screen and all the components in it.
     */
    public void renderLivingEntity(int argX, int argY, int scale, EntityLivingBase par5EntityLivingBase)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)argX, (float)argY, 50.0F);
        GL11.glScalef((float)(-scale), (float)scale, (float)scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
        float f2 = par5EntityLivingBase.renderYawOffset;
        float f3 = par5EntityLivingBase.rotationYaw;
        float f4 = par5EntityLivingBase.rotationPitch;
        float f5 = par5EntityLivingBase.prevRotationYawHead;
        float f6 = par5EntityLivingBase.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        par5EntityLivingBase.renderYawOffset = 0.0F;
        par5EntityLivingBase.rotationYaw = 0.0F;
        par5EntityLivingBase.rotationPitch = 0.0F;
        par5EntityLivingBase.rotationYawHead = par5EntityLivingBase.rotationYaw;
        par5EntityLivingBase.prevRotationYawHead = par5EntityLivingBase.rotationYaw;
        GL11.glTranslatef(0.0F, par5EntityLivingBase.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        par5EntityLivingBase.moveForward = 1.0F;
        RenderManager.instance.func_147940_a(par5EntityLivingBase, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        par5EntityLivingBase.renderYawOffset = f2;
        par5EntityLivingBase.rotationYaw = f3;
        par5EntityLivingBase.rotationPitch = f4;
        par5EntityLivingBase.prevRotationYawHead = f5;
        par5EntityLivingBase.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
}

