package net.minecraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDebuffStaff extends Item
{	
	private String discript;
	
	public ItemDebuffStaff(String desc)
	{
		discript = desc;
        
        this.maxStackSize = 1;
        this.setMaxDamage(4);
        this.setCreativeTab(CreativeTabs.tabBrewing);
	}
 
 
	
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par1ItemStack.damageItem(1, par3EntityPlayer);

        par3EntityPlayer.removePotionEffect(Potion.poison.id);
        par3EntityPlayer.removePotionEffect(Potion.weakness.id);
        par3EntityPlayer.removePotionEffect(Potion.blindness.id);
        par3EntityPlayer.removePotionEffect(Potion.confusion.id);
        par3EntityPlayer.removePotionEffect(Potion.digSlowdown.id);
        par3EntityPlayer.removePotionEffect(Potion.moveSlowdown.id);
        par3EntityPlayer.removePotionEffect(Potion.hunger.id);
		return par1ItemStack;
 
    }
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    par3List.add(discript);
   }
 
}