package net.tastypommeslul.darkDupe.commands;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.tastypommeslul.darkDupe.BlockedItems;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DupeCommand extends Command {
    public DupeCommand() {
        super("dupe");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        final ArrayList<ItemStack> items = new ArrayList<>();
        if (!(sender instanceof Player player)) {
          sender.sendMessage("This command can only be run by players.");
          return false;
        }
        ItemStack currentItem = player.getInventory().getItemInMainHand();
        if (currentItem.getType() == Material.AIR) {
          sender.sendMessage("You must be holding an item to dupe.");
          return false;
        }
        boolean isShulker = currentItem.getType() == Material.SHULKER_BOX
                || currentItem.getType() == Material.RED_SHULKER_BOX
                || currentItem.getType() == Material.BLUE_SHULKER_BOX
                || currentItem.getType() == Material.BLACK_SHULKER_BOX
                || currentItem.getType() == Material.BROWN_SHULKER_BOX
                || currentItem.getType() == Material.CYAN_SHULKER_BOX
                || currentItem.getType() == Material.GRAY_SHULKER_BOX
                || currentItem.getType() == Material.GREEN_SHULKER_BOX
                || currentItem.getType() == Material.LIGHT_BLUE_SHULKER_BOX
                || currentItem.getType() == Material.LIGHT_GRAY_SHULKER_BOX
                || currentItem.getType() == Material.LIME_SHULKER_BOX
                || currentItem.getType() == Material.MAGENTA_SHULKER_BOX
                || currentItem.getType() == Material.ORANGE_SHULKER_BOX
                || currentItem.getType() == Material.PINK_SHULKER_BOX
                || currentItem.getType() == Material.PURPLE_SHULKER_BOX
                || currentItem.getType() == Material.WHITE_SHULKER_BOX
                || currentItem.getType() == Material.YELLOW_SHULKER_BOX;

        logic(currentItem, items, player, isShulker);

        items.clear();
        return true;
    }

    void logic(ItemStack currentItem, ArrayList<ItemStack> items, Player sender, boolean isShulker) {
        if (isShulker) {
            List<ItemStack> contents = currentItem.getData(DataComponentTypes.CONTAINER).contents();
            for (ItemStack item : contents) {
                if (BlockedItems.items.contains(item)) {
                    items.add(item);
                    sender.sendMessage(Component.text("Found blocked items in your shulker box."));
                    sender.sendMessage(Component.text("Remove them and try again."));
                    return;
                }
            }
        } else {
            if (BlockedItems.items.contains(currentItem)) {
                items.add(currentItem);
                sender.sendMessage(Component.text("Blocked item." + currentItem.getType().name()));
                return;
            }
        }
        sender.sendMessage(Component.text("No blocked items found."));
        sender.getInventory().addItem(currentItem);
        sender.sendMessage(Component.text("Duping Completed."));
        items.clear();
    }
}