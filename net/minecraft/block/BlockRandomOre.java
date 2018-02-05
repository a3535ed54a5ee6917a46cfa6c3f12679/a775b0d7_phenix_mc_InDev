package net.minecraft.block;
 
import java.util.Arrays;
import java.util.List;
import java.util.Random;
 
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
 
 
public class BlockRandomOre extends Block {
  
    public BlockRandomOre() {
      
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
   
	public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
   
	List<Item> items = Arrays.asList(
  Items.opale, Items.hydralite, Items.boxyte, Items.phenix, Items.opale_sword, Items.opale_axe, Items.opale_shovel, Items.opale_pickaxe, Items.hydralite_sword, Items.hydralite_axe, Items.hydralite_shovel);
  Item dropped = items.get(random.nextInt(items.size()));	
  
  	return dropped;
	}
}