package fr.phenixmc.client.guis;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiIngameMenu extends GuiExtandedScreen {
	
	public void initGui() {
		this.buttonList.clear();
		
    	String buttonTexture = "button.png";
    	String buttonTextureHover = "buttonHover.png";
    	GuiTexturedButton options = new GuiTexturedButton(0, this.width / 2 - 80, this.height / 2 - 40, 170, 34, "Options", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(options);
    	GuiTexturedButton texturesPack = new GuiTexturedButton(1, this.width / 2 - 80, this.height / 2 - 0, 170, 34, "Pack de Textures", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(texturesPack);
    	GuiTexturedButton shop = new GuiTexturedButton(2, this.width / 2 - 80, this.height / 2 + 40, 170, 34, "" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "Boutique", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(shop);
    	GuiTexturedButton teamspeak = new GuiTexturedButton(3, this.width / 2 - 80, this.height / 2 + 80, 170, 34, "TeamSpeak", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(teamspeak);
    	GuiTexturedButton disconnect = new GuiTexturedButton(4, this.width / 2 - 80, this.height / 2 + 120, 170, 34, EnumChatFormatting.YELLOW + "" + EnumChatFormatting.BOLD + "Deconnexion", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(disconnect);
	}

    protected void actionPerformed(GuiButton button) {
    	
    	int id = button.id;
    	
        switch (id) {
        
        	default:
        		break;
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
            	this.mc.gameSettings.saveOptions();
 	            this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
                break;
            case 2:
            	this.phenix.createNewBrowserConnection("http://www.phenixmc.fr/");
            	break;
            case 3:
            	this.phenix.createNewTeamSpeakConnection("ts.phenixmc.fr", null, this.mc.getSession().getUsername());
            	break;
            case 4:
            	
            	 button.enabled = false;
                 this.mc.theWorld.sendQuittingDisconnectingPacket();
                 this.mc.loadWorld((WorldClient)null);
                 this.mc.displayGuiScreen(new GuiMainMenu());             
                break;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();

    }
 
	public void drawScreen(int par1, int par2, float par3) {
		
		Boolean defaultunicode = fontRendererObj.getUnicodeFlag();
		
		this.drawDefaultBackground();
		this.drawLogo(this.width / 2 - 240, this.height / 2 - 250, 500, 90, "logo.png");
		this.renderLivingEntity(this.width - 50, this.height / 2 + 240, 60, this.mc.thePlayer);
		
		GL11.glPushMatrix();
		GL11.glScalef(5F, 5F, 5F);
		fontRendererObj.setUnicodeFlag(true);
		String title = EnumChatFormatting.GOLD + "Hey, " + EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + this.mc.getSession().getUsername();
		this.drawCenteredString(this.fontRendererObj, title, (int)((float)(width  / 2 + (this.fontRendererObj.getStringWidth(title) / 4 )) / 5F), (int)((float)(this.height / 2 - 150) / 5F), this.phenix.getDefaultTextColor());
		fontRendererObj.setUnicodeFlag(defaultunicode);
		GL11.glPopMatrix();
		String descriptionLineOne = EnumChatFormatting.DARK_GRAY + "Bienvenue sur le menu "+  EnumChatFormatting.YELLOW + "" + EnumChatFormatting.BOLD + "Pause";
		this.drawCenteredString(this.fontRendererObj, descriptionLineOne, this.width / 2 + 10, this.height / 2 - 100, this.phenix.getDefaultTextColor());
		String descriptionLineTwo = EnumChatFormatting.DARK_GRAY + "Retrouvez ici plusieurs" + EnumChatFormatting.GOLD + " racourcis" + EnumChatFormatting.DARK_GRAY + " ainsi que des " + EnumChatFormatting.GOLD + "informations " + EnumChatFormatting.DARK_GRAY  + " en temps réel";
		this.drawCenteredString(this.fontRendererObj, descriptionLineTwo, this.width / 2 + 10, this.height / 2 - 90, this.phenix.getDefaultTextColor());

		super.drawScreen(par1, par2, par3);
	} 
}
