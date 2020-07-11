package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerState {

    int spawnX;
    int spawnY;

    Map<String, Integer> values;

    void addValue(String string, Integer integer) {
        values.put(string, integer);

    }

    Integer getValue(String valueName) {
        AtomicReference<Integer> out = new AtomicReference<>(0);
        AtomicBoolean found = new AtomicBoolean(false);

        values.forEach((v, k) -> {
            if (v.equals(valueName)) {
                out.set(k);
                found.set(true);
            }
        });

        if (!found.get()) {
            System.out.println("something wrong in playerState.getValue(); valueName: " + valueName);
        }

        return out.get();
    }

    void setSpawn(Integer X, Integer Y) {
        spawnX = X;
        spawnY = Y;
    }

    PlayerState() {
        values = new HashMap<>();
    }
}
