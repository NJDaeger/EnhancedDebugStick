package com.njdaeger.enhanceddebugstick.util;

import com.njdaeger.enhanceddebugstick.EnhancedDebugStick;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Function;
import java.util.function.Predicate;

public final class BossBarTimer {

    private final Runnable onFinish;
    private final long startTime;
    private final long totalTime;
    private final Player player;
    private final boolean fill;
    private final long updateInterval;
    private final Function<BossBarTimer, String> title;
    private final Predicate<Player> cancel;

    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval, Function<BossBarTimer, String> title, Predicate<Player> cancel, Runnable onFinish) {
        return new BossBarTimer(player, fill, time, updateInterval, title, cancel, onFinish);
    }

    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval, Function<BossBarTimer, String> title, Predicate<Player> cancel) {
        return create(player, fill, time, updateInterval, title, cancel, null);
    }

    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval, Function<BossBarTimer, String> title) {
        return create(player, fill, time, updateInterval, title, null);
    }

    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval) {
        return create(player, fill, time, updateInterval, null);
    }

    /**
     * Creates a Bossbar Timer for X seconds with no title, updating every 1/4 second
     * @param player Player to display this timer to
     * @param fill Whether this timer is to be filling or to be emptying. (True fills it, otherwise it will enpty)
     * @param time How long this timer should go on (millis).
     * A new bossbar timer.
     */
    public static BossBarTimer create(Player player, boolean fill, long time) {
        return create(player, fill, time, 5);
    }

    private BossBarTimer(Player player, boolean fill, long time, long updateInterval, Function<BossBarTimer, String> title, Predicate<Player> cancel, Runnable onFinish) {
        this.fill = fill;
        this.updateInterval = updateInterval;
        this.title = title;
        this.cancel = cancel;
        this.player = player;
        this.startTime = System.currentTimeMillis();
        this.totalTime = time;
        this.onFinish = onFinish;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public Player getPlayer() {
        return player;
    }

    public Runnable getOnFinish() {
        return onFinish;
    }

    public void start() {
        BossBar bossBar = Bukkit.createBossBar(title != null ? title.apply(this) : "", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG);
        bossBar.setProgress(fill ? 0 : 1);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        new BukkitRunnable() {
            boolean t = true;
            @Override
            public void run() {
                if (t) {
                    t = false;
                    System.out.println(startTime);
                    System.out.println(totalTime);
                    System.out.println(System.currentTimeMillis());
                    System.out.println("STARTED");
                }
                System.out.println(((startTime+totalTime) - System.currentTimeMillis()));
                if (((startTime+totalTime) - System.currentTimeMillis()) < 0 || (cancel != null && cancel.test(player))) {
                    bossBar.setVisible(false);
                    bossBar.removePlayer(player);
                    cancel();
                    if (cancel != null && !cancel.test(player) && onFinish != null) {
                        System.out.println("FINISH");
                        onFinish.run();
                    }
                    return;
                }
                if (title != null) bossBar.setTitle(title.apply(BossBarTimer.this));
                bossBar.setProgress(fill ? getFillProgress() : getEmptyProgress());
            }

        }.runTaskTimer(EnhancedDebugStick.getInstance(), 0, updateInterval);
    }

    private double getFillProgress() {
        double timeSec = totalTime/1000.;
        double endTimeSec = (startTime + totalTime)/1000.;
        double currTimeSec = System.currentTimeMillis()/1000.;
        return (1/timeSec) * (endTimeSec-currTimeSec);
    }

    private double getEmptyProgress() {
        double timeSec = totalTime/1000.;
        double endTimeSec = (startTime + totalTime)/1000.;
        double currTimeSec = System.currentTimeMillis()/1000.;
        return (-1/timeSec) * (endTimeSec-currTimeSec) + 1;
    }
}
