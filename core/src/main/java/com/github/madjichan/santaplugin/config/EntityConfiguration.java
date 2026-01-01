package com.github.madjichan.santaplugin.config;

import com.github.madjichan.santaplugin.config.entity.LivingEntityConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class EntityConfiguration {
    private String tagName;
    private EntityType entityType;
    private LivingEntityConfiguration livingEntityConfiguration;

    private EntityConfiguration() {

    }

    public static EntityConfiguration parse(Map<?, ?> configEntity) {
        EntityConfiguration res = new EntityConfiguration();

        res.tagName = (String) configEntity.get("tag");
        res.entityType = EntityType.valueOf((String) configEntity.get("type"));

        res.livingEntityConfiguration = LivingEntityConfiguration.parse(configEntity);
        return res;
    }

    public String getTagName() {
        return this.tagName;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public Entity configure(Entity entity) {
        if(entity instanceof LivingEntity livingEntity) {
            this.livingEntityConfiguration.configure(livingEntity);
        }
        return entity;
    }
}
