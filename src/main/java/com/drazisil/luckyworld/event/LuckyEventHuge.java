package com.drazisil.luckyworld.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public class LuckyEventHuge extends LuckyEvent {


    @Override
    public void doAction(BlockBreakEvent event, World world, Location location, Player player) {

        Zombie giantRider = world.spawn(location, Zombie.class);
        EntityEquipment giantRiderEquipment = giantRider.getEquipment();

        Objects.requireNonNull(giantRiderEquipment).setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        giantRiderEquipment.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        giantRiderEquipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        giantRiderEquipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);

        giantRiderEquipment.setItemInMainHand(sword);


        Giant giant = world.spawn(location, Giant.class);

        EntityEquipment giantEquipment = giant.getEquipment();

        Objects.requireNonNull(giantEquipment).setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        giantEquipment.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        giantEquipment.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        giantEquipment.setHelmet(new ItemStack(Material.DIAMOND_HELMET));

        giantEquipment.setItemInMainHand(sword);
        giantEquipment.setItemInMainHandDropChance(1.0f);

        giant.addPassenger(giantRider);




    }
}
