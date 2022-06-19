package com.jeff_media.daytime.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.jeff_media.daytime.Daytime;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("daytime")
@CommandPermission("daytime.admin")
public final class MainCommand extends BaseCommand {

    private static final Daytime main = Daytime.getInstance();

    @Default
    @Subcommand("reload")
    public static void reload(CommandSender sender) {
        main.reload();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4[&7Daytime&4] &aReloaded configuration."));
    }

}
