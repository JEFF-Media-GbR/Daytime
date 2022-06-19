package com.jeff_media.daytime;

import co.aikar.commands.PaperCommandManager;
import com.jeff_media.daytime.commands.MainCommand;
import de.jeff_media.jefflib.JeffLib;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Daytime extends JavaPlugin implements Listener {

    @Getter private static Daytime instance;

    {
        instance = this;
    }

    @Override
    public void onEnable() {
        JeffLib.enableNMS();
        getServer().getPluginManager().registerEvents(this, this);
        PaperCommandManager acf = new PaperCommandManager(this);
        acf.registerCommand(new MainCommand());
        reload();
    }

    public void reload() {
        WorldTimeHandler.stop();
        saveDefaultConfig();
        reloadConfig();

        WorldTimeHandler.start();
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        System.out.println("TimeSkip: " + event.getWorld().getName() + " -> " + event.getSkipAmount() + " " + event.getSkipReason().name());
    }


}
