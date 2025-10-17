package net.tastypommeslul.darkDupe;

import net.tastypommeslul.darkDupe.commands.DupeCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class DarkDupe extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getCommandMap().register("dupe", new DupeCommand());
    }
}
