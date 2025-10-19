package net.tastypommeslul.darkDupe.commands;

import io.papermc.paper.datacomponent.DataComponentTypes;
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
        final ArrayList<Material> items = new ArrayList<>();
        if (!(sender instanceof Player player)) {
          sender.sendMessage("This command can only be run by players.");
          return false;
        }
        Material currentItem = player.getInventory().getItemInMainHand().getType();
        if (currentItem == Material.AIR) {
          sender.sendRichMessage("<yellow>You must be holding an item to dupe!");
          return false;
        }
        boolean isShulker = BlockedItems.shulkers.contains(currentItem);

        logic(player.getInventory().getItemInMainHand(), items, player, isShulker);

        items.clear();
        return true;
    }

    void logic(ItemStack currentItem, ArrayList<Material> items, Player sender, boolean isShulkerOrBundle) {
        if (BlockedItems.bundles.contains(currentItem.getType())) {
            sender.sendRichMessage("<red><bold>Bundles can't be duped.");
            return;
        }
        if (isShulkerOrBundle) {
            List<ItemStack> contents = currentItem.getData(DataComponentTypes.CONTAINER).contents();
            for (ItemStack item : contents) {
                if (BlockedItems.items.contains(item.getType()) || BlockedItems.bundles.contains(item.getType())) {
                    items.add(item.getType());
                    sender.sendRichMessage("Found blocked item in your shulker box. <red><bold>" + item.getType().name());
                    sender.sendRichMessage("Remove them and try again.");
                    return;
                }
            }
        } else {
            if (BlockedItems.items.contains(currentItem.getType())) {
                items.add(currentItem.getType());
                sender.sendRichMessage("Blocked item. <red><bold>" + currentItem.getType().name());
                return;
            }
        }
        sender.getInventory().addItem(currentItem);
        sender.sendRichMessage("<green><bold>Duping Completed.");
        items.clear();
    }
}