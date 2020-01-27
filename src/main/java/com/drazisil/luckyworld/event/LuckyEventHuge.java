package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class LuckyEventHuge extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        Zombie giantMount = world.spawn(location, Zombie.class);
        giantMount.setBaby(true);
        giantMount.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        giantMount.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        giantMount.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        giantMount.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
        sword.addEnchantment(Enchantment.MENDING, 1);

        giantMount.getEquipment().setItemInMainHand(sword);


        Giant giant = world.spawn(location, Giant.class);

        giant.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        giant.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        giant.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        giant.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));

        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
        sword.addEnchantment(Enchantment.MENDING, 1);

        giant.getEquipment().setItemInMainHand(sword);
        giant.getEquipment().setItemInMainHandDropChance(1.0f);

        giant.addPassenger(giantMount);




    }
}
