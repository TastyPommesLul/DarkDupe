package net.tastypommeslul.darkDupe

import net.tastypommeslul.darkDupe.commands.DupeCommand
import org.bukkit.plugin.java.JavaPlugin

class DarkDupe : JavaPlugin() {

  override fun onEnable() {
    server.commandMap.register("darkdupe", DupeCommand())
  }

  override fun onDisable() {
    // Plugin shutdown logic
  }
}
