package com.jeff_media.daytime;

import co.aikar.commands.PaperCommandManager;
import com.jeff_media.daytime.commands.MainCommand;
import com.jeff_media.jefflib.ConfigUtils;
import com.jeff_media.jefflib.JeffLib;
import com.jeff_media.updatechecker.UpdateCheckSource;
import com.jeff_media.updatechecker.UpdateChecker;
import com.jeff_media.updatechecker.UserAgentBuilder;
import de.jeff_media.daddy.Stepsister;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Daytime extends JavaPlugin {

    @SuppressWarnings("CanBeFinal")
    @Getter private static Daytime instance;
    private WorldTimeHandler worldTimeHandler;

    {
        JeffLib.init(this);
        instance = this;
    }

    @Override
    public void onEnable() {
        Stepsister.init(this);
        Stepsister.createVerificationFile();
        JeffLib.enableNMS();
        PaperCommandManager acf = new PaperCommandManager(this);
        acf.registerCommand(new MainCommand());
        initUpdateChecker();
        reload();
    }

    public void initUpdateChecker() {
        YamlConfiguration conf = ConfigUtils.getConfig("update-checker.yml");
        UpdateChecker.init(this, "https://api.jeff-media.com/daytime/latest-version.txt")
                .setDonationLink("https://paypal.me/mfnalex")
                .setChangelogLink(102748)
                .setDownloadLink(102748)
                .setColoredConsoleOutput(true)
                .setUserAgent(UserAgentBuilder.getDefaultUserAgent())
                .setNotifyRequesters(true)
                .setNotifyOpsOnJoin(true)
                .suppressUpToDateMessage(true)
                .setTimeout(10000);


        switch (conf.getString("check-for-updates","true").toLowerCase()) {
            case "true":
                UpdateChecker.getInstance()
                        .checkEveryXHours(conf.getDouble("check-interval",24))
                        .checkNow();
                break;
            case "false":
                break;
            default:
                UpdateChecker.getInstance().checkNow();
        }
    }

    public void reload() {
        if(worldTimeHandler != null) {
            worldTimeHandler.stop();
        }
        saveDefaultConfig();
        reloadConfig();
        worldTimeHandler = new WorldTimeHandler();
        worldTimeHandler.start();
    }

}
