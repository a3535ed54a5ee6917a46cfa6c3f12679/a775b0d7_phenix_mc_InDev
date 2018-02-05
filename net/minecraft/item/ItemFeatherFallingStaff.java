package net.minecraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemFeatherFallingStaff extends Item
{	
	private String discript;
	
	public ItemFeatherFallingStaff(String desc)
	{
		discript = desc;
        
        this.maxStackSize = 1;
        this.setMaxDamage(4);
        this.setCreativeTab(CreativeTabs.tabBrewing);
	}
 
 
	
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par1ItemStack.damageItem(1, par3EntityPlayer);

        par3EntityPlayer.addPotionEffect(new PotionEffect(Potion.featherfalling.id, 2400, 0));

		return par1ItemStack;
 
    }
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    par3List.add(discript);
   }
 
}