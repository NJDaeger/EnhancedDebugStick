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
    private long startTime;
    private final long totalTime;
    private final Player player;
    private final boolean fill;
    private final long updateInterval;
    private final Function<BossBarTimer, String> title;
    private final Predicate<Player> cancel;

    /**
     * Creates a Bossbar Timer for X seconds for a player with the following options
     *
     * @param player Player to display this timer to
     * @param fill Whether this timer is to be filling or to be emptying. (True fills it, otherwise it will
     *         empty)
     * @param time How long this timer should go on (millis).
     * @param updateInterval How frequently this timer should update
     * @param title The title of this timer (updated every update interval)
     * @param cancel An action which cancels this timer. (Whenever the predicate is true, it will stop the timer
     *         where it is and remove it)
     * @param onFinish What to perform when the timer completes. (This will not trigger on a cancel)
     * @return The new bossbar timer.
     */
    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval, Function<BossBarTimer, String> title, Predicate<Player> cancel, Runnable onFinish) {
        return new BossBarTimer(player, fill, time, updateInterval, title, cancel, onFinish);
    }

    /**
     * Creates a Bossbar Timer for X seconds for a player with the following options
     *
     * @param player Player to display this timer to
     * @param fill Whether this timer is to be filling or to be emptying. (True fills it, otherwise it will
     *         empty)
     * @param time How long this timer should go on (millis).
     * @param updateInterval How frequently this timer should update
     * @param title The title of this timer (updated every update interval)
     * @param cancel An action which cancels this timer. (Whenever the predicate is true, it will stop the timer
     *         where it is and remove it)
     * @return The new bossbar timer.
     */
    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval, Function<BossBarTimer, String> title, Predicate<Player> cancel) {
        return create(player, fill, time, updateInterval, title, cancel, null);
    }

    /**
     * Creates a Bossbar Timer for X seconds for a player with the following options
     *
     * @param player Player to display this timer to
     * @param fill Whether this timer is to be filling or to be emptying. (True fills it, otherwise it will
     *         empty)
     * @param time How long this timer should go on (millis).
     * @param updateInterval How frequently this timer should update
     * @param title The title of this timer (updated every update interval)
     * @return The new bossbar timer.
     */
    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval, Function<BossBarTimer, String> title) {
        return create(player, fill, time, updateInterval, title, null);
    }

    /**
     * Creates a Bossbar Timer for X seconds for a player with the following options
     *
     * @param player Player to display this timer to
     * @param fill Whether this timer is to be filling or to be emptying. (True fills it, otherwise it will
     *         empty)
     * @param time How long this timer should go on (millis).
     * @param updateInterval How frequently this timer should update
     * @return The new bossbar timer.
     */
    public static BossBarTimer create(Player player, boolean fill, long time, long updateInterval) {
        return create(player, fill, time, updateInterval, null);
    }

    /**
     * Creates a Bossbar Timer for X seconds for a player with the following options
     *
     * @param player Player to display this timer to
     * @param fill Whether this timer is to be filling or to be emptying. (True fills it, otherwise it will
     *         empty)
     * @param time How long this timer should go on (millis).
     * @return The new bossbar timer.
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
        this.totalTime = time;
        this.onFinish = onFinish;
    }

    /**
     * The time this timer started. (May be 0 if the timer hasnt started yet)
     * @return When the timer started.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * The amount of time (millis) this timer should run for
     * @return The total amount of time the timer runs for
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * The player this timer is displayed to
     * @return The player the timer is displayed to
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Starts the timer
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        BossBar bossBar = Bukkit.createBossBar(title != null ? title.apply(this) : "", BarColor.BLUE, BarStyle.SOLID, BarFlag.CREATE_FOG);
        bossBar.setProgress(fill ? 0 : 1);
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (((startTime + totalTime) - System.currentTimeMillis()) < 0 || (cancel != null && cancel.test(player))) {
                    bossBar.setVisible(false);
                    bossBar.removePlayer(player);
                    cancel();
                    if (cancel != null && !cancel.test(player) && onFinish != null) onFinish.run();
                    return;
                }
                if (title != null) bossBar.setTitle(title.apply(BossBarTimer.this));
                bossBar.setProgress(fill ? getFillProgress() : getEmptyProgress());
            }

        }.runTaskTimer(EnhancedDebugStick.getInstance(), 0, updateInterval);
    }

    private double getFillProgress() {
        double timeSec = totalTime / 1000.;
        double endTimeSec = (startTime + totalTime) / 1000.;
        double currTimeSec = System.currentTimeMillis() / 1000.;
        return (-1 / timeSec) * (endTimeSec - currTimeSec) + 1;
    }

    private double getEmptyProgress() {
        double timeSec = totalTime / 1000.;
        double endTimeSec = (startTime + totalTime) / 1000.;
        double currTimeSec = System.currentTimeMillis() / 1000.;
        return (1 / timeSec) * (endTimeSec - currTimeSec);
    }
}
