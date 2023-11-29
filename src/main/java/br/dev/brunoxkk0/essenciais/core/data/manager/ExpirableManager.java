package br.dev.brunoxkk0.essenciais.core.data.manager;

import br.dev.brunoxkk0.essenciais.Essenciais;
import br.dev.brunoxkk0.essenciais.core.data.Expirable;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ExpirableManager {

    private static final ConcurrentHashMap<ExpirableKey, BukkitTask> RUNNABLE_HASH_MAP = new ConcurrentHashMap<>();

    public static void scheduleExpiration(String key, Player player, Reason reason, Expirable expirable, long millis, Object... context) {
        ExpirableKey expirableKey = new ExpirableKey(key, player, reason);
        RUNNABLE_HASH_MAP.put(expirableKey, createRunnable(expirableKey, expirable, context).runTaskLater(Essenciais.getInstance(), millis / 50));
    }

    public static void scheduleAsyncExpiration(String key, Player player, Reason reason, Expirable expirable, long millis, Object... context) {
        ExpirableKey expirableKey = new ExpirableKey(key, player, reason);
        RUNNABLE_HASH_MAP.put(expirableKey, createRunnable(expirableKey, expirable, context).runTaskLaterAsynchronously(Essenciais.getInstance(), millis / 50));
    }

    public static void cancelExpiration(String key) {

        ExpirableKey expirableKey = findKey(key);

        doCancellation(expirableKey);
    }

    private static void doCancellation(ExpirableKey expirableKey) {
        if (expirableKey != null) {
            BukkitTask task = RUNNABLE_HASH_MAP.get(expirableKey);

            if (task != null)
                task.cancel();

            RUNNABLE_HASH_MAP.remove(expirableKey);
        }
    }

    public static boolean exists(String key) {
        return findKey(key) != null;
    }

    public static boolean exists(Player player, Reason reason) {
        return !keysOf(player, reason).isEmpty();
    }

    private static ExpirableKey findKey(String key) {
        return RUNNABLE_HASH_MAP.keySet().stream().filter(el -> el.key.equals(key))
                .findFirst().orElse(null);
    }

    private static List<ExpirableKey> keysOf(Player player, Reason reason) {
        return RUNNABLE_HASH_MAP.keySet().stream().filter(el ->
                el.player.getUniqueId().equals(player.getUniqueId()) && el.reason == reason
        ).collect(Collectors.toList());
    }

    public static void cancelExpirationEvery(Player player, Reason reason){
        keysOf(player, reason).forEach(ExpirableManager::doCancellation);
    }

    public static boolean isExpired(String key) {
        return !exists(key);
    }

    private static BukkitRunnable createRunnable(ExpirableKey key, Expirable expirable, Object... context) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                expirable.onExpire(key.key, context);
                RUNNABLE_HASH_MAP.remove(key);
            }
        };
    }

    public enum Reason {
        TELEPORT,
        TELEPORT_ALL,
        TELEPORT_ACCEPTED,
        TELEPORT_INVITED;
    }

    @AllArgsConstructor
    private static class ExpirableKey {
        private final String key;
        private final Player player;
        private final Reason reason;
    }

}
