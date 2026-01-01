package com.github.madjichan.santaplugin.config.entity;

import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class LivingEntityConfiguration {
    private LivingEntityConfiguration() {

    }

    public static LivingEntityConfiguration parse(Map<?, ?> configLivingEntity) {
        LivingEntityConfiguration res = new LivingEntityConfiguration();

        return res;
    }

    public LivingEntity configure(LivingEntity livingEntity) {
        return livingEntity;
    }
}
