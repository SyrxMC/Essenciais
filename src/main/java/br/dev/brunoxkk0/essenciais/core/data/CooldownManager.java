package br.dev.brunoxkk0.essenciais.core.data;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class CooldownManager extends DataStore<CooldownManager.Cooldown> {

    private final Object lock = new Object();

    public static class Cooldown {
        public ArrayList<CooldownItem> cooldownList = new ArrayList<>();
    }

    @Data
    public static class CooldownItem {
        private final String key;
        private final long time;
        private final long duration;
    }

    private static final ArrayList<Cooldown> cooldowns = new ArrayList<>();

    public CooldownManager(Path path) {
        super(Cooldown.class, path, cooldowns);
    }

    @Override
    public Path calculateFilePath(Cooldown item) {
        return new File(getDataStorePath().toFile(), "cooldown.json").toPath();

    }

    private Cooldown access() {
        if (cooldowns.isEmpty())
            cooldowns.add(new Cooldown());

        return cooldowns.stream().findFirst().get();
    }

    public void register(String key, long startTime, long duration) {
        access().cooldownList.add(new CooldownItem(key, startTime, duration));
    }

    @SneakyThrows
    public void remove(String key) {

        lock.wait();

        ArrayList<CooldownItem> cooldownItems = access().cooldownList;
        cooldownItems.removeIf(item -> item.getKey().equals(key));

        lock.notifyAll();
    }

}
