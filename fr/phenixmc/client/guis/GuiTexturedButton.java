package fr.phenixmc.client.guis;

import org.lwjgl.opengl.GL11;

import fr.phenixmc.client.PhenixMc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
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
public class GuiTexturedButton extends GuiButton {
	
	/**
	 * Instance PhenixMc
	 */
	private PhenixMc phenix = PhenixMc.getPhenix();
	
	/**
	 * Datas
	 */
	private ResourceLocation buttonTexture;
	private ResourceLocation buttonTextureHover;  
	private int textColor = phenix.getDefaultTextColor();
    /**
     * @param id
     * @param x
     * @param y
     * @param width
     * @param height
     * @param text
     * @param color
     * @param buttonTextre
     * @param buttonTextureHover
     * 
     * Constructeur
     */
    public GuiTexturedButton(int id, int x, int y, int width, int height, String text, int color, String buttonTexture, String buttonTextureHover) {
        super(id, x, y, width, height, text);       
        this.id = id;
        this.buttonTexture = new ResourceLocation(this.phenix.getResourcePath() + buttonTexture);
        this.buttonTextureHover = new ResourceLocation(this.phenix.getResourcePath() + buttonTextureHover);
        this.displayString = text; 
        this.textColor = color;
    }

    /* 
     * Dessiner le bouton
     */
    public void drawButton(Minecraft minecraft, int x, int y) {

    	FontRenderer font = minecraft.fontRenderer;
    
        boolean flag = x >= this.field_146128_h && y >= this.field_146129_i && x < this.field_146128_h + this.field_146120_f && y < this.field_146129_i + this.field_146121_g;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (flag) {
            minecraft.getTextureManager().bindTexture(this.buttonTextureHover);
            this.drawTexturedModalRectWithOptionnalSize(this.field_146128_h, this.field_146129_i, 0, 0, this.field_146120_f, this.field_146121_g);     
            
        } else {
    
            minecraft.getTextureManager().bindTexture(this.buttonTexture);
            this.drawTexturedModalRectWithOptionnalSize(this.field_146128_h, this.field_146129_i, 0, 0, this.field_146120_f, this.field_146121_g);
        }
        this.drawCenteredString(font, this.displayString, this.field_146128_h + this.field_146120_f / 2, this.field_146129_i+ (this.field_146121_g - 8) / 2, this.textColor);  
    }
}
