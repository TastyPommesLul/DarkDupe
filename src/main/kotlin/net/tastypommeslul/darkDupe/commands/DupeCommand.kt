package net.tastypommeslul.darkDupe.commands

import io.papermc.paper.datacomponent.DataComponentTypes
import net.kyori.adventure.text.Component
import net.tastypommeslul.darkDupe.BlockedItems
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DupeCommand: Command("dupe") {
  override fun execute(
    sender: CommandSender,
    label: String,
    args: Array<out String>
  ): Boolean {
    val items: MutableList<ItemStack> = mutableListOf()
    if (sender !is Player) {
      sender.sendMessage("This command can only be run by players.")
      return false
    }
    val currentItem = sender.inventory.itemInMainHand
    if (currentItem.type == Material.AIR) {
      sender.sendMessage("You must be holding an item to dupe.")
      return false
    }
    val isShulker = currentItem.type == Material.SHULKER_BOX
            || currentItem.type == Material.RED_SHULKER_BOX
            || currentItem.type == Material.BLUE_SHULKER_BOX
            || currentItem.type == Material.BLACK_SHULKER_BOX
            || currentItem.type == Material.BROWN_SHULKER_BOX
            || currentItem.type == Material.CYAN_SHULKER_BOX
            || currentItem.type == Material.GRAY_SHULKER_BOX
            || currentItem.type == Material.GREEN_SHULKER_BOX
            || currentItem.type == Material.LIGHT_BLUE_SHULKER_BOX
            || currentItem.type == Material.LIGHT_GRAY_SHULKER_BOX
            || currentItem.type == Material.LIME_SHULKER_BOX
            || currentItem.type == Material.MAGENTA_SHULKER_BOX
            || currentItem.type == Material.ORANGE_SHULKER_BOX
            || currentItem.type == Material.PINK_SHULKER_BOX
            || currentItem.type == Material.PURPLE_SHULKER_BOX
            || currentItem.type == Material.WHITE_SHULKER_BOX
            || currentItem.type == Material.YELLOW_SHULKER_BOX

    logic(currentItem, items, sender, isShulker)

    items.clear()
    return true
  }

  fun logic(currentItem: ItemStack, items: MutableList<ItemStack>, sender: Player, isShulker: Boolean) {
    if (isShulker) {
      val contents = currentItem.getData(DataComponentTypes.CONTAINER)!!.contents()
      for (item in contents) {
        if (BlockedItems.items.contains(item)) {
          items.add(item)
          println("found in shulker ${item.type.name}")
          sender.sendMessage(Component.text("Found blocked items in your shulker box."))
          sender.sendMessage(Component.text("Remove them and try again."))
          return
        }
      }
    } else {
      if (BlockedItems.items.contains(currentItem)) {
        items.add(currentItem)
        sender.sendMessage(Component.text("Blocked item. ${currentItem.type.name}"))
        println("found single")
        return
      }
    }
    println("none found")
    sender.sendMessage(Component.text("No blocked items found."))
    sender.inventory.addItem(currentItem)
    sender.sendMessage(Component.text("Duping Completed."))
    items.clear()
  }
}