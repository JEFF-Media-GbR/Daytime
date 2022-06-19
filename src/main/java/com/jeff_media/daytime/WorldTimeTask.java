package com.jeff_media.daytime;

import de.jeff_media.jefflib.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Map;

import static com.jeff_media.daytime.WorldTimeHandler.NOON;

public final class WorldTimeTask implements Runnable {

    private final Map<String,Double> dayOffsets;
    private final Map<String,Double> nightOffsets;
    private final Map<String,Double> daySpeed;
    private final Map<String,Double> nightSpeed;

    public WorldTimeTask(Map<String, Double> dayOffsets,
                         Map<String, Double> nightOffsets,
                         Map<String, Double> daySpeed,
                         Map<String, Double> nightSpeed) {
        this.dayOffsets = dayOffsets;
        this.nightOffsets = nightOffsets;

        this.daySpeed = daySpeed;
        this.nightSpeed = nightSpeed;
    }

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            String worldName = world.getName();
            if (world.getTime() > NOON && nightOffsets.containsKey(worldName)) {
                WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() - 1);
                if (nightOffsets.get(worldName) > 1) {
                    long skipAmount = Math.max((long) Math.floor(nightOffsets.get(worldName)),NOON);
                    WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() + skipAmount);
                    nightOffsets.put(worldName, nightOffsets.get(worldName) - skipAmount);
                    if (world.getTime() < NOON) {
                        nightOffsets.put(worldName, 0.0);
                    }
                }
                nightOffsets.put(worldName, nightSpeed.get(worldName) + nightOffsets.get(worldName));
            } else if (dayOffsets.containsKey(worldName)) {
                WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() - 1);
                if (dayOffsets.get(worldName) > 1) {
                    long skipAmount = Math.max((long) Math.floor(dayOffsets.get(worldName)),NOON);
                    WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() + skipAmount);
                    dayOffsets.put(worldName, dayOffsets.get(worldName) - skipAmount);
                    if (world.getTime() > NOON) {
                        dayOffsets.put(worldName, 0.0);
                    }
                }
                dayOffsets.put(worldName, daySpeed.get(worldName) + dayOffsets.get(worldName));
            }
        }
    }

}
