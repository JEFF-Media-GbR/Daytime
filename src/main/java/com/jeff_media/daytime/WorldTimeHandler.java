package com.jeff_media.daytime;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public final class WorldTimeHandler {

    public static final long NOON = 12_000L;
    private static final Daytime main = Daytime.getInstance();
    private final Map<String, Double> dayOffsets = new HashMap<>();
    private final Map<String, Double> nightOffsets = new HashMap<>();
    private final Map<String, Double> daySpeed = new HashMap<>();
    private final Map<String, Double> nightSpeed = new HashMap<>();

    private int task = -1;

    public void start() {
        if(main.getConfig().isConfigurationSection("worlds")) {
            for (String worldName : Objects.requireNonNull(main.getConfig().getConfigurationSection("worlds")).getKeys(false)) {
                long dayTicks = (long) (main.getConfig().getDouble("worlds." + worldName + ".daytime", 10) * 1200);
                long nightTicks = (long) (main.getConfig().getDouble("worlds." + worldName + ".nighttime", 10) * 1200);
                daySpeed.put(worldName, NOON / (double) dayTicks);
                dayOffsets.put(worldName, 0.0);
                nightSpeed.put(worldName, NOON / (double) nightTicks);
                nightOffsets.put(worldName, 0.0);
            }
        }

        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new WorldTimeTask(dayOffsets, nightOffsets, daySpeed, nightSpeed), 0, 1);
    }

    public void stop() {
        if (task != -1) Bukkit.getScheduler().cancelTask(task);
        task = -1;
    }

}
