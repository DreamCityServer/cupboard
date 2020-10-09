package tw.mics.spigot.plugin.cupboard.listener;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import tw.mics.spigot.plugin.cupboard.Cupboard;
import tw.mics.spigot.plugin.cupboard.config.Locales;

public class TNTCraftListener extends MyListener {
	public TNTCraftListener(Cupboard instance)
	{
		super(instance);
	    overwriteTNTRecipes();
	}

    @EventHandler
	public void onCrafting(PrepareItemCraftEvent event){
		CraftingInventory inv = event.getInventory();
		if(inv.getResult() != null && inv.getResult().getType() == Material.TNT){
			if(!(
					inv.getItem(1).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) &&
					inv.getItem(2).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) &&
					inv.getItem(3).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) &&
					inv.getItem(4).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) &&
					inv.getItem(6).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) &&
					inv.getItem(7).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) &&
					inv.getItem(8).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) &&
					inv.getItem(9).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)
				))
			inv.setResult(null);
		}
		if(inv.getResult() != null && inv.getResult().getType() == Material.GUNPOWDER){
			if((
					inv.getItem(1).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ||
					inv.getItem(3).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ||
					inv.getItem(5).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ||
					inv.getItem(7).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE) ||
					inv.getItem(9).getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE)
				))
			inv.setResult(null);
		}
	}
    

	private void overwriteTNTRecipes(){
    	Iterator<Recipe> it = this.plugin.getServer().recipeIterator();
    	Recipe recipe;
    	
    	//remove TNT Recipes
    	while(it.hasNext()){
    		recipe = it.next();
    		if (recipe != null && recipe.getResult().getType() == Material.TNT){
				it.remove();
    		}
    	}
    	
    	//setup explosion
    	ItemStack item = new ItemStack(Material.GUNPOWDER);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(Locales.TNT_EXPLOTION_NAME.getString());
    	meta.setLore(Locales.TNT_EXPLOTION_LORE.getStringList());
    	meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
    	item.setItemMeta(meta);
    	
    	//setup explosion recipes
    	ShapedRecipe shapedRecipe = new ShapedRecipe(item);
    	shapedRecipe.shape("GSG", "SGS", "GSG");
    	shapedRecipe.setIngredient('S', Material.SAND);
    	shapedRecipe.setIngredient('G', Material.GUNPOWDER);
    	Bukkit.addRecipe(shapedRecipe);
    	
    	//setup TNT
    	item = new ItemStack(Material.TNT);
    	meta = item.getItemMeta();
    	meta.setLore(Locales.TNT_TNT_LORE.getStringList());
    	item.setItemMeta(meta);
    	
    	//setup TNT recipes
    	shapedRecipe = new ShapedRecipe(item);
    	shapedRecipe.shape("EEE", "EGE", "EEE");
    	shapedRecipe.setIngredient('E', Material.GUNPOWDER);
    	shapedRecipe.setIngredient('G', Material.GOLD_BLOCK);
    	Bukkit.addRecipe(shapedRecipe);
    }

}
