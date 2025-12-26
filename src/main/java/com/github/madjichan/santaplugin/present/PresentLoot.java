package com.github.madjichan.santaplugin.present;

import org.bukkit.Material;

import java.util.*;

public class PresentLoot {
    public record PresentMaterialRecord(Material material, int weight) {}
    public record PresentItemRecord(int itemCount, int weight) {}
    public record GenerateResult(Material material, int itemCount) {}
    public record TableRecord(PresentMaterialRecord materialRecord, PresentItemRecord[] itemRecord) {}

    private List<TableRecord> table;

    public PresentLoot(PresentMaterialRecord[] materials, PresentItemRecord[][] presentItemRecords) {
        this.table = new ArrayList<>();

        int materialWeightSum = 0;
        for(int i=0; i<materials.length; i++) {
            PresentMaterialRecord materialRecord = materials[i];
            PresentItemRecord[] itemRecords = presentItemRecords[i].clone();

            int itemWeightSum = 0;
            for(int j=0; j<itemRecords.length; j++) {
                PresentItemRecord itemRecord = itemRecords[j];
                int itemCount = itemRecord.itemCount;

                itemWeightSum += itemRecord.weight;
                itemRecords[j] = new PresentItemRecord(itemCount, itemWeightSum);

            }

            Material material = materialRecord.material;
            materialWeightSum += materialRecord.weight;
            this.table.add(new TableRecord(new PresentMaterialRecord(material, materialWeightSum), itemRecords));
        }
    }

    public PresentLoot(List<TableRecord> table) {
        this.table = new ArrayList<>();

        int materialWeightSum = 0;
        for(int i=0; i<table.size(); i++) {
            PresentMaterialRecord materialRecord = table.get(i).materialRecord;
            PresentItemRecord[] itemRecords = table.get(i).itemRecord.clone();

            int itemWeightSum = 0;
            for(int j=0; j<itemRecords.length; j++) {
                PresentItemRecord itemRecord = itemRecords[j];
                int itemCount = itemRecord.itemCount;

                itemWeightSum += itemRecord.weight;
                itemRecords[j] = new PresentItemRecord(itemCount, itemWeightSum);

            }

            Material material = materialRecord.material;
            materialWeightSum += materialRecord.weight;
            this.table.add(new TableRecord(new PresentMaterialRecord(material, materialWeightSum), itemRecords));
        }
    }

    public GenerateResult gen() {
        Random rand = new Random();

        long materialSeed = Integer.toUnsignedLong(rand.nextInt());
        long itemSeed = Integer.toUnsignedLong(rand.nextInt());

        materialSeed %= this.table.get(this.table.size()-1).materialRecord.weight;
        materialSeed += 1;
        TableRecord forMaterialSearch = new TableRecord(new PresentMaterialRecord(Material.AIR, (int)materialSeed), null);
        int choosedMaterialIndex = Collections.binarySearch(this.table, forMaterialSearch, new Comparator<TableRecord>() {
            @Override
            public int compare(TableRecord r1, TableRecord r2) {
                return r1.materialRecord.weight - r2.materialRecord.weight;
            }
        });
        choosedMaterialIndex = choosedMaterialIndex >= 0 ? choosedMaterialIndex : -choosedMaterialIndex - 1;

        TableRecord choosedTableRecord = this.table.get(choosedMaterialIndex);
        itemSeed %= choosedTableRecord.itemRecord[choosedTableRecord.itemRecord.length - 1].weight;
        itemSeed += 1;
        PresentItemRecord forItemSearch = new PresentItemRecord(0, (int)itemSeed);
        int choosedItemIndex = Arrays.binarySearch(choosedTableRecord.itemRecord, forItemSearch, new Comparator<PresentItemRecord>() {
            @Override
            public int compare(PresentItemRecord r1, PresentItemRecord r2) {
                return r1.weight - r2.weight;
            }
        });
        choosedItemIndex = choosedItemIndex >= 0 ? choosedItemIndex : -choosedItemIndex - 1;

        PresentItemRecord choosedItemRecord = choosedTableRecord.itemRecord[choosedItemIndex];

        return new GenerateResult(choosedTableRecord.materialRecord.material, choosedItemRecord.itemCount);
    }
}
