package com.jeff_media.daytime;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public final class WorldTimeHandler {

    public static final long NOON = 12_000L;
    private static final Daytime main = Daytime.getInstance();
    private final Map<World, Double> dayTime = new HashMap<>();
    private final Map<World, Double> nightTime = new HashMap<>();
    private final Map<World, Double> daySpeed = new HashMap<>();
    private final Map<World, Double> nightSpeed = new HashMap<>();

    private int task = -1;

    public void start() {
        for (World world : Bukkit.getWorlds()) {
            long dayTicks = (long) (main.getConfig().getDouble("worlds." + world.getName() + ".daytime", 10) * 1200);
            long nightTicks = (long) (main.getConfig().getDouble("worlds." + world.getName() + ".nighttime", 10) * 1200);
            daySpeed.put(world, NOON / (double) dayTicks);
            dayTime.put(world, 0.0);
            nightSpeed.put(world, NOON / (double) nightTicks);
            nightTime.put(world, 0.0);
        }

        task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new WorldTimeTask(dayTime, nightTime, daySpeed, nightSpeed), 0, 1);
    }

    public void stop() {
        if (task != -1) Bukkit.getScheduler().cancelTask(task);
        task = -1;
    }

}
