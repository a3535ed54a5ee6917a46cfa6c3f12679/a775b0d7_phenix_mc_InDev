package fr.phenixmc.client.guis;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenOnlineServers;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.mco.ExceptionMcoService;
import net.minecraft.client.mco.ExceptionRetryCall;
import net.minecraft.client.mco.GuiScreenClientOutdated;
import net.minecraft.client.mco.McoClient;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiExtandedScreen {

    public GuiMainMenu() {
       
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {

    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2) {}

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
       
    	this.buttonList.clear();
    	String buttonTexture = "button.png";
    	String buttonTextureHover = "buttonHover.png";
    	GuiTexturedButton singleplayer = new GuiTexturedButton(0, this.width / 2 - 310, this.height / 2 - 20, 170, 34, "Jouer Seul", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(singleplayer);
    	GuiTexturedButton multiplayer = new GuiTexturedButton(1, this.width / 2 - 80, this.height / 2 - 20, 170, 34, "Rejoindre " + EnumChatFormatting.YELLOW + " PhenixMc", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(multiplayer);
    	GuiTexturedButton options = new GuiTexturedButton(2, this.width / 2 - 310, this.height / 2 - 120, 170, 34, "Options", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(options);
    	GuiTexturedButton texturesPack = new GuiTexturedButton(3, this.width / 2 - 310, this.height / 2 + 80 , 170, 34, "Pack de Textures", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(texturesPack);
    	GuiTexturedButton shop = new GuiTexturedButton(4, this.width / 2 + 140, this.height / 2 - 120, 170, 34, "" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "Boutique", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(shop);
    	GuiTexturedButton leave = new GuiTexturedButton(5, this.width / 2 + 140, this.height / 2 + 80, 170, 34, "Quitter", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(leave);
    	GuiTexturedButton teamspeak = new GuiTexturedButton(6, this.width / 2 + 140, this.height / 2 - 20, 170, 34, "TeamSpeak", this.phenix.getDefaultTextColor(), buttonTexture, buttonTextureHover);
    	this.buttonList.add(teamspeak);
    
    }


    protected void actionPerformed(GuiButton button) {
    	
    	int id = button.id;
    	switch(id) {
    	
	    	default:
	    		//Null
	    		break;
	    	case 0:
	    		this.mc.displayGuiScreen(new GuiSelectWorld(this));
	    		break;
	    	case 1:
	    		this.mc.displayGuiScreen(new GuiConnecting(this, mc, new ServerData("PhenixMc", "149.202.137.200:25565")));
	    		break;
	    	case 2:
	    		this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
	    		break;
	    	case 3:
	            this.mc.gameSettings.saveOptions();
	            this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
	    		break;
	    	case 4:
	    		this.phenix.createNewBrowserConnection("http://www.phenixmc.fr/");
	    		break;
	    	case 5:
	            this.mc.shutdown();
	    		break;
	    	case 6:
	    		this.phenix.createNewTeamSpeakConnection("ts.phenixmc.fr", "9987", Minecraft.getMinecraft().getSession().getUsername());
    	}
    }




    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3) {

    	this.drawnGuiBackground("background.png");
    	this.drawGradientTopBar(); 	
    	this.drawGradientBottomBar();
    	String copyright = "" + EnumChatFormatting.RED + "www.PhenixMc.fr";
    	this.drawCenteredString(this.mc.fontRenderer, copyright, this.width + 30 - this.mc.fontRenderer.getStringWidth(copyright), this.height - 10, this.phenix.getDefaultTextColor());
        super.drawScreen(par1, par2, par3);
    }

	/**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
    }
}
 