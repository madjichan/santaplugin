package com.github.madjichan.santaplugin.config;

import com.github.madjichan.santaplugin.present.PresentLoot;
import com.github.madjichan.santaplugin.santa.entity.SantaEntity;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SantaConfiguration {
    public double speed;
    public double rotationRadius;
    public double presentDropPeriod;
    public List<PresentLoot.TableRecord> table;
    public String presentTextureLink;
    public SantaEntity.SantaComponent marker;
    public SantaEntity.SantaComponent[] santaComponents;
    private static SantaConfiguration config;

    public Map<String, ItemStackConfiguration> items;
    public Map<String, EntityConfiguration> entities;

    private SantaConfiguration() {
        this.items = new HashMap<>();
        this.entities = new HashMap<>();
    }

    public static SantaConfiguration getInstance() {
        return SantaConfiguration.config;
    }

    public static Color hexToColor(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color: " + hex);
        }

        int rgb = Integer.parseInt(hex, 16);
        return Color.fromRGB(rgb);
    }

    public static SantaConfiguration parse(FileConfiguration config) {
        SantaConfiguration res = new SantaConfiguration();

        List<Map<?, ?>> configItems = config.getMapList("items");
        for(Map<?, ?> configItem: configItems) {
            ItemStackConfiguration nItem = ItemStackConfiguration.parse(configItem);
            res.items.put(nItem.getTagName(), nItem);
        }

        List<Map<?, ?>> configEntities = config.getMapList("entities");
        for(Map<?, ?> configEntity: configEntities) {
            EntityConfiguration nEnt = EntityConfiguration.parse(configEntity);
            res.entities.put(nEnt.getTagName(), nEnt);
        }

        double marker_dx = config.getDouble("santa.placement.marker.dx");
        double marker_dy = config.getDouble("santa.placement.marker.dy");
        res.marker = new SantaEntity.SantaComponent(null, null, marker_dx, marker_dy);

        List<Map<?, ?>> components = config.getMapList("santa.placement.components");
        res.santaComponents = new SantaEntity.SantaComponent[components.size()];
        for(int i=0; i<components.size(); i++) {
            Map<?, ?> component = components.get(i);
            String name = (String) component.get("name");
            EntityType ent = EntityType.valueOf((String) component.get("entity"));
            double dx = (double) component.get("dx");
            double dy = (double) component.get("dy");

            res.santaComponents[i] = new SantaEntity.SantaComponent(name, ent, dx, dy);
        }

        res.speed = config.getDouble("santa.speed");
        res.rotationRadius = config.getDouble("santa.rotationRadius");
        res.presentDropPeriod = config.getDouble("santa.presentDropPeriod");
        res.presentTextureLink = config.getString("present.texture");

        res.table = new ArrayList<>();
        List<Map<?, ?>> lootTable = config.getMapList("present.lootTable");
        for(Map<?, ?> tableEntry: lootTable) {
            Material material = Material.matchMaterial((String) tableEntry.get("material"));
            int materialWeight = (int) tableEntry.get("materialWeight");
            PresentLoot.PresentMaterialRecord materialRecord = new PresentLoot.PresentMaterialRecord(material, materialWeight);

            List<Map<?, ?>> itemTable = (List<Map<?, ?>>) tableEntry.get("itemTable");
            PresentLoot.PresentItemRecord[] itemRecords = new PresentLoot.PresentItemRecord[itemTable.size()];
            for(int i=0; i<itemTable.size(); i++) {
                int count = (int) itemTable.get(i).get("count");
                int itemWeight = (int) itemTable.get(i).get("itemWeight");

                itemRecords[i] = new PresentLoot.PresentItemRecord(count, itemWeight);
            }

            res.table.add(new PresentLoot.TableRecord(materialRecord, itemRecords));
        }

        SantaConfiguration.config = res;
        return res;
    }
}
