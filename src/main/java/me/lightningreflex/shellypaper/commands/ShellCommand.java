package me.lightningreflex.shellypaper.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellCommand implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		String command = String.join(" ", args);

		sender.sendMessage(Component.join(
			JoinConfiguration.noSeparators(),
			Component.text("Running command: ").color(NamedTextColor.AQUA),
			Component.text(command)
		));

		// Run the command
		try {
			ProcessBuilder builder = new ProcessBuilder(
				"/bin/bash", "-c", String.join(" ", args)
			);
			Process p = builder.start();

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			new Thread(() -> {
				try {
					String s;
					while ((s = stdInput.readLine()) != null) {
						sender.sendMessage(
							Component.text(s).clickEvent(ClickEvent.suggestCommand(s))
						);
					}
				} catch (IOException ignored) {}
			}).start();
			new Thread(() -> {
				try {
					String s;
					while ((s = stdError.readLine()) != null) {
						sender.sendMessage(
							Component.text(s).clickEvent(ClickEvent.suggestCommand(s))
						);
					}
				} catch (IOException ignored) {}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
