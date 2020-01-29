package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("DuplicatedCode")
public class LuckyEventHuge extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        Zombie giantRider = world.spawn(location, Zombie.class);
        giantRider.setBaby(true);
        EntityEquipment giantRiderEquipment = giantRider.getEquipment();
        assert giantRiderEquipment != null;
        giantRiderEquipment.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        giantRiderEquipment.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        giantRiderEquipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        giantRiderEquipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
        sword.addEnchantment(Enchantment.MENDING, 1);

        giantRiderEquipment.setItemInMainHand(sword);


        Giant giant = world.spawn(location, Giant.class);

        EntityEquipment giantEquipment = giant.getEquipment();
        assert  giantEquipment != null;
        giantEquipment.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        giantEquipment.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        giantEquipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        giantEquipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));

        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);
        sword.addEnchantment(Enchantment.MENDING, 1);

        giantEquipment.setItemInMainHand(sword);
        giantEquipment.setItemInMainHandDropChance(1.0f);

        giant.addPassenger(giantRider);




    }
}
