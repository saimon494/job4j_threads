package ru.job4j.cas;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.getId(), model) == null;
    }

    public boolean update(Base model) {
        return memory.computeIfPresent(
                model.getId(), (id, currentModel) -> {
                    if (currentModel.getVersion() != model.getVersion()) {
                        throw new OptimisticException("Versions are not equal");
                    }
                    Base newModel = new Base(id, currentModel.getVersion() + 1);
                    newModel.setName(model.getName());
                    return newModel;
                }) != null;
    }

    public void delete(Base model) {
        Base stored = memory.get(model.getId());
        if (stored.getVersion() != model.getVersion()) {
            throw new OptimisticException("Versions are not equal");
        }
        memory.remove(model.getId());
    }
}
