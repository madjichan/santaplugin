package com.github.madjichan.santaplugin.config;

import com.github.madjichan.santaplugin.present.PresentLoot;
import com.github.madjichan.santaplugin.santa.entity.SantaEntity;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
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

    private SantaConfiguration() {

    }

    public static SantaConfiguration getInstance() {
        return SantaConfiguration.config;
    }

    public static SantaConfiguration parse(FileConfiguration config) {
        SantaConfiguration res = new SantaConfiguration();

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
