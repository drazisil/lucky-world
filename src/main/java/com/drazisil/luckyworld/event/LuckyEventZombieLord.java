package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class LuckyEventZombieLord extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

       player.sendMessage("Boss Fight!");

       Zombie zombie = world.spawn(location, Zombie.class);

       zombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
       zombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
       zombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
       zombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
        sword.addEnchantment(Enchantment.MENDING, 1);

       zombie.getEquipment().setItemInMainHand(sword);
       zombie.getEquipment().setItemInMainHandDropChance(1.0f);

    }
}