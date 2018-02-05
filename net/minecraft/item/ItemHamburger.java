package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemHamburger extends ItemFood {

	public ItemHamburger(int p_i45341_1_, float p_i45341_2_, boolean p_i45341_3_) {
		super(p_i45341_1_, p_i45341_2_, p_i45341_3_);
		this.setHasSubtypes(true);
	}

	/**
	 * Return an item rarity from EnumRarity
	 */
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.epic;
	}

	protected void onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par2World.isClient) {
			par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, 2400, 0));
			par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.featherfalling.id, 2400, 0));
			par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 600, 0));
		}
		super.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
	}
}
