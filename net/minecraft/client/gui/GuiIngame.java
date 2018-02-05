package net.minecraft.client.gui;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiIngame extends Gui {
	private static final ResourceLocation inventoryPath = new ResourceLocation("textures/gui/container/inventory.png");
	private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
	private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
	private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
	
	private static final RenderItem itemRenderer = new RenderItem();
	private final Random rand = new Random();
	private final Minecraft mc;
	private EntityPlayer ep;
	private static final boolean REI_MINIMAP;
	/** ChatGUI instance that retains all previous chat data */
	private final GuiNewChat persistantChatGUI;
	private int updateCounter;

	/** The string specifying which record music is playing */
	private String recordPlaying = "";

	/** How many ticks the record playing message will be displayed */
	private int recordPlayingUpFor;
	private boolean recordIsPlaying;

	/** ToggleSneak */
	private static String textForHUD = "";
	private static int hudTextPosX = 1;
	private static int hudTextPosY = 1;
	/** Previous frame vignette brightness (slowly changes by 1% each frame) */
	public float prevVignetteBrightness = 1.0F;

	/** Remaining ticks the item highlight should be visible */
	private int remainingHighlightTicks;

	/** The ItemStack that is currently being highlighted */
	private ItemStack highlightingItemStack;
	private static final String __OBFID = "CL_00000661";

	public void showCenterMessage(String formattedText) {
		displayedCenterMessage = formattedText;
		centerMessageTimer = 8 * 20; // 3 * 20 ticks = 8 secondes
	}

	/**
	 * Dans cette variable sera stocké le message à afficher.
	 */
	private String displayedCenterMessage = null;

	/**
	 * Le temps restant du message à afficher (en ticks).
	 */
	private int centerMessageTimer = 0;

	public GuiIngame(Minecraft par1Minecraft) {
		this.mc = par1Minecraft;
		this.persistantChatGUI = new GuiNewChat(par1Minecraft);
	}

	public static void SetHUDText(String text) {
		textForHUD = text;
	}

	public static int GetHorizontalLocation() {
		return hudTextPosX;
	}

	public static int SetHorizontalLocation(int posX) {
		if (posX < 0) {
			posX = 0;
		} else if (posX > Minecraft.getMinecraft().displayWidth) {
			posX = Minecraft.getMinecraft().displayWidth;
		}
		hudTextPosX = posX;
		return hudTextPosX;
	}

	public static int GetVerticalLocation() {
		return hudTextPosY;
	}

	public static int SetVerticalLocation(int posY) {
		if (posY < 0) {
			posY = 0;
		} else if (posY > Minecraft.getMinecraft().displayHeight) {
			posY = Minecraft.getMinecraft().displayHeight;
		}
		hudTextPosY = posY;
		return hudTextPosY;
	}

	public void renderGameOverlay(float par1, boolean par2, int par3, int par4) {
		ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int var6 = var5.getScaledWidth();
		int var7 = var5.getScaledHeight();
		FontRenderer var8 = this.mc.fontRenderer;
		this.mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);

		if (Minecraft.isFancyGraphicsEnabled()) {
			this.renderVignette(this.mc.thePlayer.getBrightness(par1), var6, var7);
		} else {
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		}

		ItemStack var9 = this.mc.thePlayer.inventory.armorItemInSlot(3);

		if (this.mc.gameSettings.thirdPersonView == 0 && var9 != null
				&& var9.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
			this.renderPumpkinBlur(var6, var7);
		}

		if (!this.mc.thePlayer.isPotionActive(Potion.confusion)) {
			float var12 = this.mc.thePlayer.prevTimeInPortal
					+ (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * par1;

			if (var12 > 0.0F) {
				this.func_130015_b(var12, var6, var7);
			}
		}

		int var13 = 0;
		int var14 = 0;
		int var371;
		int var381;

		if (!this.mc.playerController.enableEverythingIsScrewedUpMode()) {
			if (displayedCenterMessage != null) {
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				// Transparence du message
				int alpha = centerMessageTimer < 10 ? ((int) (centerMessageTimer * 25)) : 255;

				// Couleur de base du message (simplement blanc auquel on a
				// ajouté la transparence)
				int color = 0xffffff | ((alpha & 255) << 24);

				// On dessine le message centré avec la couleur calculée
				var8.drawStringWithShadow(this.displayedCenterMessage,
						var6 / 2 - var8.getStringWidth(this.displayedCenterMessage) / 2 + 0, 30,
						var14 + (var13 << 24 & -16777216));
				// this.mc.fontRenderer.drawString(displayedCenterMessage, (var6
				// -
				// this.mc.fontRenderer.getStringWidth(displayedCenterMessage))
				// / 2, var7 / 2 - 100, color);
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(widgetsTexPath);
			InventoryPlayer var38 = this.mc.thePlayer.inventory;
			this.zLevel = -90.0F;
			this.drawTexturedModalRect(var6 / 2 - 91, var7 - 22, 0, 0, 182, 22);
			this.drawTexturedModalRect(var6 / 2 - 91 - 1 + var38.currentItem * 20, var7 - 22 - 1, 0, 22, 24, 22);
			this.mc.getTextureManager().bindTexture(icons);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(775, 769, 1, 0);
			this.drawTexturedModalRect(var6 / 2 - 7, var7 / 2 - 7, 0, 0, 16, 16);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			this.mc.mcProfiler.startSection("bossHealth");
			this.renderBossHealth();

			this.mc.mcProfiler.endSection();

			if (this.mc.playerController.shouldDrawHUD()) {
				this.func_110327_a(var6, var7);
			}

			this.mc.mcProfiler.startSection("actionBar");
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.enableGUIStandardItemLighting();

			for (var371 = 0; var371 < 9; ++var371) {
				var381 = var6 / 2 - 90 + var371 * 20 + 2;
				var13 = var7 - 16 - 3;
				this.renderInventorySlot(var371, var381, var13, par1);
			}
			this.HUD(par1); 
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			this.mc.mcProfiler.endSection();
			GL11.glDisable(GL11.GL_BLEND);
		}

		int var391;

		if (this.mc.thePlayer.getSleepTimer() > 0) {
			this.mc.mcProfiler.startSection("sleep");
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			var391 = this.mc.thePlayer.getSleepTimer();
			float var15 = (float) var391 / 100.0F;

			if (var15 > 1.0F) {
				var15 = 1.0F - (float) (var391 - 100) / 10.0F;
			}

			var381 = (int) (220.0F * var15) << 24 | 1052704;
			drawRect(0, 0, var6, var7, var381);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			this.mc.mcProfiler.endSection();
		}

		var391 = 16777215;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var371 = var6 / 2 - 91;
		int var16;
		int var17;
		short var37;
		float var33;
		int var39;
		int var401;

		if (this.mc.thePlayer.isRidingHorse()) {
			this.mc.mcProfiler.startSection("jumpBar");
			this.mc.getTextureManager().bindTexture(Gui.icons);
			var33 = this.mc.thePlayer.getHorseJumpPower();
			var37 = 182;
			var39 = (int) (var33 * (float) (var37 + 1));
			var401 = var7 - 32 + 3;
			this.drawTexturedModalRect(var371, var401, 0, 84, var37, 5);

			if (var39 > 0) {
				this.drawTexturedModalRect(var371, var401, 0, 89, var39, 5);
			}

			this.mc.mcProfiler.endSection();
		} else if (this.mc.playerController.gameIsSurvivalOrAdventure()) {
			this.mc.mcProfiler.startSection("expBar");
			this.mc.getTextureManager().bindTexture(Gui.icons);
			var381 = this.mc.thePlayer.xpBarCap();

			if (var381 > 0) {
				var37 = 182;
				var39 = (int) (this.mc.thePlayer.experience * (float) (var37 + 1));
				var401 = var7 - 32 + 3;
				this.drawTexturedModalRect(var371, var401, 0, 64, var37, 5);

				if (var39 > 0) {
					this.drawTexturedModalRect(var371, var401, 0, 69, var39, 5);
				}
			}

			this.mc.mcProfiler.endSection();

			if (this.mc.thePlayer.experienceLevel > 0) {
				this.mc.mcProfiler.startSection("expLevel");
				boolean var40 = false;
				var39 = var40 ? 16777215 : 8453920;
				String var22 = "" + this.mc.thePlayer.experienceLevel;
				var16 = (var6 - var8.getStringWidth(var22)) / 2;
				var17 = var7 - 31 - 4;
				boolean var51 = false;
				var8.drawString(var22, var16 + 1, var17, 0);
				var8.drawString(var22, var16 - 1, var17, 0);
				var8.drawString(var22, var16, var17 + 1, 0);
				var8.drawString(var22, var16, var17 - 1, 0);
				var8.drawString(var22, var16, var17, var39);
				this.mc.mcProfiler.endSection();
			}
		}

		String var411;

		if (this.mc.gameSettings.heldItemTooltips) {
			this.mc.mcProfiler.startSection("toolHighlight");

			if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
				var411 = this.highlightingItemStack.getDisplayName();
				var13 = (var6 - var8.getStringWidth(var411)) / 2;
				var39 = var7 - 59;

				if (!this.mc.playerController.shouldDrawHUD()) {
					var39 += 14;
				}

				var401 = (int) ((float) this.remainingHighlightTicks * 256.0F / 10.0F);

				if (var401 > 255) {
					var401 = 255;
				}

				if (var401 > 0) {
					GL11.glPushMatrix();
					GL11.glEnable(GL11.GL_BLEND);
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					var8.drawStringWithShadow(var411, var13, var39, 16777215 + (var401 << 24));
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glPopMatrix();
				}
			}

			this.mc.mcProfiler.endSection();
		}

		if (this.mc.isDemo()) {
			this.mc.mcProfiler.startSection("demo");
			var411 = "";

			if (this.mc.theWorld.getTotalWorldTime() >= 120500L) {
				var411 = I18n.format("demo.demoExpired", new Object[0]);
			} else {
				var411 = I18n.format("demo.remainingTime", new Object[] {
						StringUtils.ticksToElapsedTime((int) (120500L - this.mc.theWorld.getTotalWorldTime())) });
			}

			var13 = var8.getStringWidth(var411);
			var8.drawStringWithShadow(var411, var6 - var13 - 10, 5, 16777215);
			this.mc.mcProfiler.endSection();
		}

		String var52;
		int var53;
		int var42;
		int var4111;
		int var421;
		int var43;

		if (this.mc.gameSettings.showDebugInfo) {
			this.mc.mcProfiler.startSection("debug");
			GL11.glPushMatrix();

			var8.drawStringWithShadow("" + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + "Phenix"  + EnumChatFormatting.RESET + "Mc", 8, 12, this.phenix.getDefaultTextColor());
			
			var421 = MathHelper.floor_double(this.mc.thePlayer.posX);
			var4111 = MathHelper.floor_double(this.mc.thePlayer.posY);
			var53 = MathHelper.floor_double(this.mc.thePlayer.posZ);

			this.mc.fontRenderer.drawString(EnumChatFormatting.GOLD + "FPS "  + EnumChatFormatting.RESET +  ": " + EnumChatFormatting.YELLOW + "" +  this.mc.debug, 8, 35, this.phenix.getDefaultTextColor(), true);
			
			
			this.mc.fontRenderer.drawString(EnumChatFormatting.GOLD + "X" + EnumChatFormatting.RESET + ": " + EnumChatFormatting.YELLOW + "" + MathHelper.floor_double(this.mc.thePlayer.posX) + EnumChatFormatting.GOLD + " Y" + EnumChatFormatting.RESET + ": " + EnumChatFormatting.YELLOW + "" + MathHelper.floor_double(this.mc.thePlayer.boundingBox.minY) + EnumChatFormatting.GOLD + " Z" + EnumChatFormatting.RESET + ": " + EnumChatFormatting.YELLOW + "" + MathHelper.floor_double(this.mc.thePlayer.posZ), 8, 55, this.phenix.getDefaultTextColor(), true);
			
			int var25 = MathHelper.floor_double((double) (this.mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			this.drawString(var8,EnumChatFormatting.GOLD + "Direction" + EnumChatFormatting.RESET + ": " + EnumChatFormatting.YELLOW + "" + Direction.directions[var25], 7, 75, this.phenix.getDefaultTextColor());
			

			if (this.mc.theWorld != null && this.mc.theWorld.blockExists(var421, var4111, var53)) {
				Chunk var26 = this.mc.theWorld.getChunkFromBlockCoords(var421, var53);
			}

			this.drawString(var8,
					String.format("",
							new Object[] { Float.valueOf(this.mc.thePlayer.capabilities.getWalkSpeed()),
									Float.valueOf(this.mc.thePlayer.capabilities.getFlySpeed()),
									Boolean.valueOf(this.mc.thePlayer.onGround),
									Integer.valueOf(this.mc.theWorld.getHeightValue(var421, var53)) }),
					2, 104, 14737632);

			if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
				this.drawString(var8,
						String.format("",
								new Object[] { this.mc.entityRenderer.getShaderGroup().getShaderGroupName() }),
						2, 112, 14737632);
			}

			GL11.glPopMatrix();
			this.mc.mcProfiler.endSection();
		}

		if (this.recordPlayingUpFor > 0) {
			this.mc.mcProfiler.startSection("overlayMessage");
			var33 = (float) this.recordPlayingUpFor - par1;
			var13 = (int) (var33 * 255.0F / 20.0F);

			if (var13 > 255) {
				var13 = 255;
			}

			if (var13 > 8) {
				GL11.glPushMatrix();
				GL11.glTranslatef((float) (var6 / 2), (float) (var7 - 68), 0.0F);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				var39 = 16777215;

				if (this.recordIsPlaying) {
					var39 = Color.HSBtoRGB(var33 / 50.0F, 0.7F, 0.6F) & 16777215;
				}

				var8.drawString(this.recordPlaying, -var8.getStringWidth(this.recordPlaying) / 2, -4,
						var39 + (var13 << 24 & -16777216));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}

			this.mc.mcProfiler.endSection();
		}

		ScoreObjective var44 = this.mc.theWorld.getScoreboard().func_96539_a(1);

		if (var44 != null) {
			this.func_96136_a(var44, var7, var6, var8);
		}

		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, (float) (var7 - 48), 0.0F);
		this.mc.mcProfiler.startSection("chat");

		this.persistantChatGUI.func_146230_a(this.updateCounter);
		this.mc.mcProfiler.endSection();
		GL11.glPopMatrix();
		var44 = this.mc.theWorld.getScoreboard().func_96539_a(0);

		if (this.mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!this.mc.isIntegratedServerRunning()
				|| this.mc.thePlayer.sendQueue.playerInfoList.size() > 1 || var44 != null)) {
			this.mc.mcProfiler.startSection("playerList");
			NetHandlerPlayClient var41 = this.mc.thePlayer.sendQueue;
			List var451 = var41.playerInfoList;
			var401 = var41.currentServerMaxPlayers;
			var16 = var401;

			for (var17 = 1; var16 > 20; var16 = (var401 + var17 - 1) / var17) {
				++var17;
			}

			int var45 = 300 / var17;

			if (var45 > 150) {
				var45 = 150;
			}

			int var461 = (var6 - var17 * var45) / 2;
			byte var47 = 10;
			drawRect(var461 - 1, var47 - 1, var461 + var45 * var17, var47 + 9 * var16, Integer.MIN_VALUE);

			for (var42 = 0; var42 < var401; ++var42) {
				var421 = var461 + var42 % var17 * var45;
				var4111 = var47 + var42 / var17 * 9;
				drawRect(var421, var4111, var421 + var45 - 1, var4111 + 8, 553648127);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_ALPHA_TEST);

				if (var42 < var451.size()) {
					GuiPlayerInfo var471 = (GuiPlayerInfo) var451.get(var42);
					ScorePlayerTeam var48 = this.mc.theWorld.getScoreboard().getPlayersTeam(var471.name);
					var52 = ScorePlayerTeam.formatPlayerName(var48, var471.name);
					var8.drawStringWithShadow(var52, var421, var4111, 16777215);

					if (var44 != null) {
						var53 = var421 + var8.getStringWidth(var52) + 5;
						var43 = var421 + var45 - 12 - 5;

						if (var43 - var53 > 5) {
							Score var481 = var44.getScoreboard().func_96529_a(var471.name, var44);
							String var501 = EnumChatFormatting.YELLOW + "" + var481.getScorePoints();
							var8.drawStringWithShadow(var501, var43 - var8.getStringWidth(var501), var4111, 16777215);
						}
					}

					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					this.mc.getTextureManager().bindTexture(icons);
					byte var49 = 0;
					boolean var50 = false;
					byte var481;

					if (var471.responseTime < 0) {
						var481 = 5;
					} else if (var471.responseTime < 150) {
						var481 = 0;
					} else if (var471.responseTime < 300) {
						var481 = 1;
					} else if (var471.responseTime < 600) {
						var481 = 2;
					} else if (var471.responseTime < 1000) {
						var481 = 3;
					} else {
						var481 = 4;
					}

					this.zLevel += 100.0F;
					this.drawTexturedModalRect(var421 + var45 - 12, var4111, 0 + var49 * 10, 176 + var481 * 8, 10, 8);
					this.zLevel -= 100.0F;
				}
			}
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}

	private void func_96136_a(ScoreObjective par1ScoreObjective, int par2, int par3, FontRenderer par4FontRenderer) {
		Scoreboard var5 = par1ScoreObjective.getScoreboard();
		Collection var6 = var5.func_96534_i(par1ScoreObjective);

		if (var6.size() <= 15) {
			int var7 = par4FontRenderer.getStringWidth(par1ScoreObjective.getDisplayName());
			String var11;

			for (Iterator var221 = var6.iterator(); var221
					.hasNext(); var7 = Math.max(var7, par4FontRenderer.getStringWidth(var11))) {
				Score var241 = (Score) var221.next();
				ScorePlayerTeam var231 = var5.getPlayersTeam(var241.getPlayerName());
				var11 = ScorePlayerTeam.formatPlayerName(var231, var241.getPlayerName()) + ": " + EnumChatFormatting.RED
						+ var241.getScorePoints();
			}

			int var22 = var6.size() * par4FontRenderer.FONT_HEIGHT;
			int var23 = par2 / 2 + var22 / 3;
			byte var241 = 3;
			int var24 = par3 - var7 - var241;
			int var12 = 0;
			Iterator var13 = var6.iterator();

			while (var13.hasNext()) {
				Score var14 = (Score) var13.next();
				++var12;
				ScorePlayerTeam var15 = var5.getPlayersTeam(var14.getPlayerName());
				String var16 = ScorePlayerTeam.formatPlayerName(var15, var14.getPlayerName());
				String var17 = EnumChatFormatting.RED + "" + var14.getScorePoints();
				int var19 = var23 - var12 * par4FontRenderer.FONT_HEIGHT;
				int var20 = par3 - var241 + 2;
				drawRect(var24 - 2, var19, var20, var19 + par4FontRenderer.FONT_HEIGHT, 1342177280);
				par4FontRenderer.drawString(var16, var24, var19, 553648127);
				par4FontRenderer.drawString(var17, var20 - par4FontRenderer.getStringWidth(var17), var19, 553648127);

				if (var12 == var6.size()) {
					String var21 = par1ScoreObjective.getDisplayName();
					drawRect(var24 - 2, var19 - par4FontRenderer.FONT_HEIGHT - 1, var20, var19 - 1, 1610612736);
					drawRect(var24 - 2, var19 - 1, var20, var19, 1342177280);
					par4FontRenderer.drawString(var21, var24 + var7 / 2 - par4FontRenderer.getStringWidth(var21) / 2,
							var19 - par4FontRenderer.FONT_HEIGHT, 553648127);
				}
			}
		}
	}

	private void func_110327_a(int par1, int par2) {
		boolean var3 = this.mc.thePlayer.hurtResistantTime / 3 % 2 == 1;

		if (this.mc.thePlayer.hurtResistantTime < 10) {
			var3 = false;
		}

		int var4 = MathHelper.ceiling_float_int(this.mc.thePlayer.getHealth());
		int var5 = MathHelper.ceiling_float_int(this.mc.thePlayer.prevHealth);
		this.rand.setSeed((long) (this.updateCounter * 312871));
		boolean var6 = false;
		FoodStats var7 = this.mc.thePlayer.getFoodStats();
		int var8 = var7.getFoodLevel();
		int var9 = var7.getPrevFoodLevel();
		IAttributeInstance var10 = this.mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
		int var11 = par1 / 2 - 91;
		int var12 = par1 / 2 + 91;
		int var13 = par2 - 39;
		float var14 = (float) var10.getAttributeValue();
		float var15 = this.mc.thePlayer.getAbsorptionAmount();
		int var16 = MathHelper.ceiling_float_int((var14 + var15) / 2.0F / 10.0F);
		int var17 = Math.max(10 - (var16 - 2), 3);
		int var18 = var13 - (var16 - 1) * var17 - 10;
		float var19 = var15;
		int var20 = this.mc.thePlayer.getTotalArmorValue();
		int var21 = -1;

		if (this.mc.thePlayer.isPotionActive(Potion.regeneration)) {
			var21 = this.updateCounter % MathHelper.ceiling_float_int(var14 + 5.0F);
		}

		this.mc.mcProfiler.startSection("armor");
		int var22;
		int var23;

        for (var22 = 0; var22 < 10; ++var22)
        {
            if (var20 > 0)
            {
                var23 = var11 + var22 * 8;

                if (var22 * 2 + 1 < var20)
                {
                    this.drawTexturedModalRect(var23, var18, 34, 9, 9, 9);
                }

                if (var22 * 2 + 1 == var20)
                {
                    this.drawTexturedModalRect(var23, var18, 25, 9, 9, 9);
                }

                if (var22 * 2 + 1 > var20)
                {
                    this.drawTexturedModalRect(var23, var18, 16, 9, 9, 9);
                }
            }
        }
        if(var20 >20){
            var23 = var11 ;
        for (var22 = 0; var22 < 10; ++var22)
        {
            if (var20 > 0)
            {
                var23 = var11 + var22 * 8;

                if (var22 * 2 + 1 +20< var20)
                {
                    this.drawTexturedModalRect(var23, var18-9, 34, 9, 9, 9);
                }

                if (var22 * 2 + 1+20 == var20)
                {
                    this.drawTexturedModalRect(var23, var18-9, 25, 9, 9, 9);
                }
                if (var22 * 2 + 1 +20 > var20)
                {
                    this.drawTexturedModalRect(var23, var18-9, 16, 9, 9, 9);
                }
            }
        }
        }

		this.mc.mcProfiler.endStartSection("health");
		int var27;
		int var25;
		int var26;

		for (var22 = MathHelper.ceiling_float_int((var14 + var15) / 2.0F) - 1; var22 >= 0; --var22) {
			var23 = 16;

			if (this.mc.thePlayer.isPotionActive(Potion.poison)) {
				var23 += 36;
			} else if (this.mc.thePlayer.isPotionActive(Potion.wither)) {
				var23 += 72;
			}

			byte var371 = 0;

			if (var3) {
				var371 = 1;
			}

			var25 = MathHelper.ceiling_float_int((float) (var22 + 1) / 10.0F) - 1;
			var26 = var11 + var22 % 10 * 8;
			var27 = var13 - var25 * var17;

			if (var4 <= 4) {
				var27 += this.rand.nextInt(2);
			}

			if (var22 == var21) {
				var27 -= 2;
			}

			byte var391 = 0;

			if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
				var391 = 5;
			}

			this.drawTexturedModalRect(var26, var27, 16 + var371 * 9, 9 * var391, 9, 9);

			if (var3) {
				if (var22 * 2 + 1 < var5) {
					this.drawTexturedModalRect(var26, var27, var23 + 54, 9 * var391, 9, 9);
				}

				if (var22 * 2 + 1 == var5) {
					this.drawTexturedModalRect(var26, var27, var23 + 63, 9 * var391, 9, 9);
				}
			}

			if (var19 > 0.0F) {
				if (var19 == var15 && var15 % 2.0F == 1.0F) {
					this.drawTexturedModalRect(var26, var27, var23 + 153, 9 * var391, 9, 9);
				} else {
					this.drawTexturedModalRect(var26, var27, var23 + 144, 9 * var391, 9, 9);
				}

				var19 -= 2.0F;
			} else {
				if (var22 * 2 + 1 < var4) {
					this.drawTexturedModalRect(var26, var27, var23 + 36, 9 * var391, 9, 9);
				}

				if (var22 * 2 + 1 == var4) {
					this.drawTexturedModalRect(var26, var27, var23 + 45, 9 * var391, 9, 9);
				}
			}
		}

		Entity var371 = this.mc.thePlayer.ridingEntity;
		int var38;

		if (var371 == null) {
			this.mc.mcProfiler.endStartSection("food");

			for (var23 = 0; var23 < 10; ++var23) {
				var38 = var13;
				var25 = 16;
				byte var381 = 0;

				if (this.mc.thePlayer.isPotionActive(Potion.hunger)) {
					var25 += 36;
					var381 = 13;
				}

				if (this.mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F
						&& this.updateCounter % (var8 * 3 + 1) == 0) {
					var38 = var13 + (this.rand.nextInt(3) - 1);
				}

				if (var6) {
					var381 = 1;
				}

				var27 = var12 - var23 * 8 - 9;
				this.drawTexturedModalRect(var27, var38, 16 + var381 * 9, 27, 9, 9);

				if (var6) {
					if (var23 * 2 + 1 < var9) {
						this.drawTexturedModalRect(var27, var38, var25 + 54, 27, 9, 9);
					}

					if (var23 * 2 + 1 == var9) {
						this.drawTexturedModalRect(var27, var38, var25 + 63, 27, 9, 9);
					}
				}

				if (var23 * 2 + 1 < var8) {
					this.drawTexturedModalRect(var27, var38, var25 + 36, 27, 9, 9);
				}

				if (var23 * 2 + 1 == var8) {
					this.drawTexturedModalRect(var27, var38, var25 + 45, 27, 9, 9);
				}
			}
		} else if (var371 instanceof EntityLivingBase) {
			this.mc.mcProfiler.endStartSection("mountHealth");
			EntityLivingBase var391 = (EntityLivingBase) var371;
			var38 = (int) Math.ceil((double) var391.getHealth());
			float var37 = var391.getMaxHealth();
			var26 = (int) (var37 + 0.5F) / 2;

			if (var26 > 30) {
				var26 = 30;
			}

			var27 = var13;

			for (int var39 = 0; var26 > 0; var39 += 20) {
				int var29 = Math.min(var26, 10);
				var26 -= var29;

				for (int var30 = 0; var30 < var29; ++var30) {
					byte var31 = 52;
					byte var32 = 0;

					if (var6) {
						var32 = 1;
					}

					int var33 = var12 - var30 * 8 - 9;
					this.drawTexturedModalRect(var33, var27, var31 + var32 * 9, 9, 9, 9);

					if (var30 * 2 + 1 + var39 < var38) {
						this.drawTexturedModalRect(var33, var27, var31 + 36, 9, 9, 9);
					}

					if (var30 * 2 + 1 + var39 == var38) {
						this.drawTexturedModalRect(var33, var27, var31 + 45, 9, 9, 9);
					}
				}

				var27 -= 10;
			}
		}

		this.mc.mcProfiler.endStartSection("air");

		if (this.mc.thePlayer.isInsideOfMaterial(Material.water)) {
			var23 = this.mc.thePlayer.getAir();
			var38 = MathHelper.ceiling_double_int((double) (var23 - 2) * 10.0D / 300.0D);
			var25 = MathHelper.ceiling_double_int((double) var23 * 10.0D / 300.0D) - var38;

			for (var26 = 0; var26 < var38 + var25; ++var26) {
				if (var26 < var38) {
					this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 16, 18, 9, 9);
				} else {
					this.drawTexturedModalRect(var12 - var26 * 8 - 9, var18, 25, 18, 9, 9);
				}
			}
		}

		this.mc.mcProfiler.endSection();
	}

	/**
	 * Renders dragon's (boss) health on the HUD
	 */
	private void renderBossHealth() {
		if (BossStatus.bossName != null && BossStatus.statusBarTime > 0) {
			--BossStatus.statusBarTime;
			FontRenderer var1 = this.mc.fontRenderer;
			ScaledResolution var2 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth,
					this.mc.displayHeight);
			int var3 = var2.getScaledWidth();
			short var4 = 182;
			int var5 = var3 / 2 - var4 / 2;
			int var6 = (int) (BossStatus.healthScale * (float) (var4 + 1));
			byte var7 = 12;
			this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);
			this.drawTexturedModalRect(var5, var7, 0, 74, var4, 5);

			if (var6 > 0) {
				this.drawTexturedModalRect(var5, var7, 0, 79, var6, 5);
			}

			String var8 = BossStatus.bossName;
			var1.drawStringWithShadow(var8, var3 / 2 - var1.getStringWidth(var8) / 2, var7 - 10, 16777215);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.getTextureManager().bindTexture(icons);
		}
	}

	private void renderPumpkinBlur(int par1, int par2) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
		Tessellator var3 = Tessellator.instance;
		var3.startDrawingQuads();
		var3.addVertexWithUV(0.0D, (double) par2, -90.0D, 0.0D, 1.0D);
		var3.addVertexWithUV((double) par1, (double) par2, -90.0D, 1.0D, 1.0D);
		var3.addVertexWithUV((double) par1, 0.0D, -90.0D, 1.0D, 0.0D);
		var3.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var3.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	/**
	 * Renders the vignette. Args: vignetteBrightness, width, height
	 */

	private void renderVignette(float par1, int par2, int par3) {
		par1 = 1.0F - par1;

		if (par1 < 0.0F) {
			par1 = 0.0F;
		}

		if (par1 > 1.0F) {
			par1 = 1.0F;
		}

		this.prevVignetteBrightness = (float) ((double) this.prevVignetteBrightness
				+ (double) (par1 - this.prevVignetteBrightness) * 0.01D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(0, 769, 1, 0);
		GL11.glColor4f(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
		this.mc.getTextureManager().bindTexture(vignetteTexPath);
		Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		var4.addVertexWithUV(0.0D, (double) par3, -90.0D, 0.0D, 1.0D);
		var4.addVertexWithUV((double) par2, (double) par3, -90.0D, 1.0D, 1.0D);
		var4.addVertexWithUV((double) par2, 0.0D, -90.0D, 1.0D, 0.0D);
		var4.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var4.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	}

	private void func_130015_b(float par1, int par2, int par3) {
		if (par1 < 1.0F) {
			par1 *= par1;
			par1 *= par1;
			par1 = par1 * 0.8F + 0.2F;
		}

		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, par1);
		IIcon var4 = Blocks.portal.getBlockTextureFromSide(1);
		this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
		float var5 = var4.getMinU();
		float var6 = var4.getMinV();
		float var7 = var4.getMaxU();
		float var8 = var4.getMaxV();
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(0.0D, (double) par3, -90.0D, (double) var5, (double) var8);
		var9.addVertexWithUV((double) par2, (double) par3, -90.0D, (double) var7, (double) var8);
		var9.addVertexWithUV((double) par2, 0.0D, -90.0D, (double) var7, (double) var6);
		var9.addVertexWithUV(0.0D, 0.0D, -90.0D, (double) var5, (double) var6);
		var9.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Renders the specified item of the inventory slot at the specified
	 * location. Args: slot, x, y, partialTick
	 */
	private void renderInventorySlot(int par1, int par2, int par3, float par4) {
		ItemStack var5 = this.mc.thePlayer.inventory.mainInventory[par1];

		if (var5 != null) {
			float var6 = (float) var5.animationsToGo - par4;

			if (var6 > 0.0F) {
				GL11.glPushMatrix();
				float var7 = 1.0F + var6 / 5.0F;
				GL11.glTranslatef((float) (par2 + 8), (float) (par3 + 12), 0.0F);
				GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float) (-(par2 + 8)), (float) (-(par3 + 12)), 0.0F);
			}

			itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var5, par2,
					par3);

			if (var6 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var5, par2, par3);
		}
	}

	/**
	 * The update tick for the ingame UI
	 */
	public void updateTick() {
		if (this.recordPlayingUpFor > 0) {
			--this.recordPlayingUpFor;
		}

		++this.updateCounter;

		if (this.centerMessageTimer > 0) {
			// On réduit le temps restant
			this.centerMessageTimer--;

			// Et si le temps est écoulé...
			if (this.centerMessageTimer == 0) {
				// On arrête d'afficher le message
				displayedCenterMessage = null;
			}
		}

		if (this.mc.thePlayer != null) {
			ItemStack var1 = this.mc.thePlayer.inventory.getCurrentItem();

			if (var1 == null) {
				this.remainingHighlightTicks = 0;
			} else if (this.highlightingItemStack != null && var1.getItem() == this.highlightingItemStack.getItem()
					&& ItemStack.areItemStackTagsEqual(var1, this.highlightingItemStack)
					&& (var1.isItemStackDamageable()
							|| var1.getItemDamage() == this.highlightingItemStack.getItemDamage())) {
				if (this.remainingHighlightTicks > 0) {
					--this.remainingHighlightTicks;
				}
			} else {
				this.remainingHighlightTicks = 40;
			}

			this.highlightingItemStack = var1;
		}
	}

	public void setRecordPlayingMessage(String par1Str) {
		this.func_110326_a("Now playing: " + par1Str, true);
	}

	public void func_110326_a(String par1Str, boolean par2) {
		this.recordPlaying = par1Str;
		this.recordPlayingUpFor = 60;
		this.recordIsPlaying = par2;
	}

	public GuiNewChat getChatGUI() {
		return this.persistantChatGUI;
	}

	public int getUpdateCounter() {
		return this.updateCounter;
	}

	static {
		boolean b = false;

		try {
			Class.forName("fr.neptuniamc.minimap.ReiMinimap");
			b = true;
		} catch (Exception var2) {
			;
		}

		REI_MINIMAP = b;
	}

	private void renderArmorSlot(int p_73832_1_, int p_73832_2_, int p_73832_3_, float p_73832_4_) {
		ItemStack var5 = this.mc.thePlayer.inventory.armorItemInSlot(p_73832_1_);

		if (var5 != null) {
			float var6 = (float) var5.animationsToGo - p_73832_4_;

			if (var6 > 0.0F) {
				GL11.glPushMatrix();
				float var7 = 1.0F + var6 / 5.0F;
				GL11.glTranslatef((float) (p_73832_2_ + 8), (float) (p_73832_3_ + 12), 0.0F);
				GL11.glScalef(1.0F / var7, (var7 + 1.0F) / 2.0F, 1.0F);
				GL11.glTranslatef((float) (-(p_73832_2_ + 8)), (float) (-(p_73832_3_ + 12)), 0.0F);
			}

			itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var5, p_73832_2_,
					p_73832_3_);

			if (var6 > 0.0F) {
				GL11.glPopMatrix();
			}

			itemRenderer.renderArmorHUD(this.mc.fontRenderer, this.mc.getTextureManager(), var5, p_73832_2_ + 20,
					p_73832_3_ + 4, null);
		}
	}

	private void HUD(float p_73830_1_) {

	   	ScaledResolution var5 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int var6 = var5.getScaledWidth();
        int var7 = var5.getScaledHeight();
        
		ItemStack helmet = this.mc.thePlayer.inventory.armorItemInSlot(3);
		ItemStack chestplate = this.mc.thePlayer.inventory.armorItemInSlot(2);
		ItemStack leggings = this.mc.thePlayer.inventory.armorItemInSlot(1);
		ItemStack boots = this.mc.thePlayer.inventory.armorItemInSlot(0);
		ItemStack hand = this.mc.thePlayer.inventory.getCurrentItem();
			    
		if (this.mc.displayHUD && !this.mc.gameSettings.showDebugInfo) {
						  
			int y = 5;
			if (helmet != null) {
				this.renderArmorSlot(3, 5, y, p_73830_1_);
				y += 15;
			}
			if (chestplate != null) {
				this.renderArmorSlot(2, 5, y, p_73830_1_);
				y += 15;
			}
			if (leggings != null) {
				this.renderArmorSlot(1, 5, y, p_73830_1_);
				y += 15;
			}
			if (boots != null) {
				this.renderArmorSlot(0, 5, y, p_73830_1_);
				y += 15;
			}

			if (hand != null && (hand.getItem() instanceof ItemSword || hand.getItem() instanceof ItemBow
					|| hand.getItem() instanceof ItemTool || hand.getItem() instanceof ItemFlintAndSteel
					|| hand.getItem() instanceof ItemShears || hand.getItem() instanceof ItemFishingRod)) {
				itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), hand, 5, y);
				itemRenderer.renderArmorHUD(this.mc.fontRenderer, this.mc.getTextureManager(), hand, 25, y + 5, null);
				y += 20;
			}
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			if (!this.mc.thePlayer.getActivePotionEffects().isEmpty()) {
				for (Iterator var12 = this.mc.thePlayer.getActivePotionEffects().iterator(); var12.hasNext(); ++y) {
					PotionEffect var15 = (PotionEffect) var12.next();
					Potion var14 = Potion.potionTypes[var15.getPotionID()];
					this.mc.getTextureManager().bindTexture(inventoryPath);

					if (var14.hasStatusIcon()) {
						
						int stringPotion = var14.getStatusIconIndex();
						this.drawTexturedModalRect(3, y, 0 + stringPotion % 8 * 18, 198 + stringPotion / 8 * 18, 18,
								18);
						y += 5;
					}

					String var13 = Potion.getDurationString(var15);

					this.mc.fontRenderer.drawString(var13, 25, y, 16777215);
					y += 15;								 
				}			
			}
		}
	}
}
