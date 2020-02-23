package com.drazisil.luckyworld.ai;

import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;

public class AIManager {

    private static final ArrayList<LivingAIEntity> entities = new ArrayList<>();

    public static void addEntity(LivingEntity entity) {
        entities.add(new LivingAIEntity(entity));
    }

    public static void removeEntity(LivingEntity entity) {
        entities.removeIf(livingAIEntity -> livingAIEntity.entity.equals(entity));
    }

    public static LivingAIEntity getAIEntity(LivingEntity entity) {

        for (LivingAIEntity livingAIEntity: entities) {
            if (livingAIEntity.entity.equals(entity)) return livingAIEntity;
        }
        return null;
    }

    public static class LivingAIEntity {

        public final LivingEntity entity;

        public LivingAIEntity(LivingEntity entity) {
            this.entity = entity;
        }
    }
}
