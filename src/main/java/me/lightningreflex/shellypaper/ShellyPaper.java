package me.lightningreflex.shellypaper;

import me.lightningreflex.shellypaper.commands.ShellCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShellyPaper extends JavaPlugin {

	@Override
	public void onEnable() {
		// Plugin startup logic
		this.getCommand("shell").setExecutor(new ShellCommand());
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
