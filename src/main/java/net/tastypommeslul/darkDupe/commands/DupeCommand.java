package net.tastypommeslul.darkDupe.commands;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.tastypommeslul.darkDupe.BlockedItems;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

    boolean hasItemTag(ItemStack currentItem) {
        AtomicBoolean isUndupeable = new AtomicBoolean(false);
        Style style = Style.empty().decorations(Map.of(
                TextDecoration.BOLD, TextDecoration.State.FALSE,
                TextDecoration.ITALIC, TextDecoration.State.FALSE,
                TextDecoration.OBFUSCATED, TextDecoration.State.FALSE,
                TextDecoration.STRIKETHROUGH, TextDecoration.State.FALSE,
                TextDecoration.UNDERLINED, TextDecoration.State.FALSE
        ));
        if (currentItem.getItemMeta().hasLore()) {
            currentItem.getItemMeta().lore().forEach(text -> text.children().forEach(child -> {
                isUndupeable.set(child.equals(Component.text("Undupeable").style(style).color(NamedTextColor.GRAY)));
            }));
        }
        return isUndupeable.get();
    }

    void logic(ItemStack currentItem, ArrayList<Material> items, Player sender, boolean isShulkerOrBundle) {
        if (BlockedItems.bundles.contains(currentItem.getType())) {
            sender.sendRichMessage("<red><bold>Bundles can't be duped.");
            return;
        }
        if (isShulkerOrBundle) {
            List<ItemStack> contents = currentItem.getData(DataComponentTypes.CONTAINER).contents();
            for (ItemStack item : contents) {
                if (BlockedItems.items.contains(item.getType()) || BlockedItems.bundles.contains(item.getType()) || hasItemTag(item)) {
                    items.add(item.getType());
                    sender.sendRichMessage("Found blocked item in your shulker box.");
                    sender.sendRichMessage("Remove <red><bold>" + item.getType().name().toLowerCase() + "</bold></red> them and try again.");
                    return;
                }
            }
        } else {
            if (BlockedItems.items.contains(currentItem.getType()) || hasItemTag(currentItem)) {
                items.add(currentItem.getType());
                sender.sendRichMessage("Blocked item. <red><bold>" + currentItem.getType().name().toLowerCase());
                return;
            }
        }
        System.out.println("Duped?");
        sender.getInventory().addItem(currentItem);
        sender.sendRichMessage("<green><bold>Duping Completed.");
        items.clear();
    }
}