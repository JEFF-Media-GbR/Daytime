package com.jeff_media.daytime;

import de.jeff_media.jefflib.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Map;

import static com.jeff_media.daytime.WorldTimeHandler.NOON;

public final class WorldTimeTask implements Runnable {

    private final Map<World,Double> dayOffset;
    private final Map<World,Double> nightOffset;
    private final Map<World,Double> daySpeed;
    private final Map<World,Double> nightSpeed;

    public WorldTimeTask(Map<World, Double> dayOffset,
                         Map<World, Double> nightOffset,
                         Map<World, Double> daySpeed,
                         Map<World, Double> nightSpeed) {
        this.dayOffset = dayOffset;
        this.nightOffset = nightOffset;

        this.daySpeed = daySpeed;
        this.nightSpeed = nightSpeed;
    }

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            if (world.getTime() > NOON && nightOffset.containsKey(world)) {
                WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() - 1);
                if (nightOffset.get(world) > 1) {
                    long skipAmount = Math.max((long) Math.floor(nightOffset.get(world)),NOON);
                    //System.out.println("Skipping " + skipAmount);
                    WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() + skipAmount);
                    nightOffset.put(world, nightOffset.get(world) - skipAmount);
                    if (world.getTime() < NOON) {
                        nightOffset.put(world, 0.0);
                    }
                }
                nightOffset.put(world, nightSpeed.get(world) + nightOffset.get(world));
            } else if (dayOffset.containsKey(world)) {
                WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() - 1);
                if (dayOffset.get(world) > 1) {
                    long skipAmount = Math.max((long) Math.floor(dayOffset.get(world)),NOON);
                    WorldUtils.setFullTimeWithoutTimeSkipEvent(world,world.getFullTime() + skipAmount);
                    dayOffset.put(world, dayOffset.get(world) - skipAmount);
                    if (world.getTime() > NOON) {
                        dayOffset.put(world, 0.0);
                    }
                }
                dayOffset.put(world, daySpeed.get(world) + dayOffset.get(world));
            }
        }
    }

}
