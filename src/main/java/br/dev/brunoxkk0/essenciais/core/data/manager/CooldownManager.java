package br.dev.brunoxkk0.essenciais.core.data.manager;

import br.dev.brunoxkk0.essenciais.core.data.Cooldown;
import br.dev.brunoxkk0.essenciais.core.data.CooldownItem;
import br.dev.brunoxkk0.essenciais.core.data.DataStore;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class CooldownManager extends DataStore<Cooldown> {

    private final Object lock = new Object();

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

