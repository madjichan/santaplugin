package com.github.madjichan.santaplugin.present.loot;

import java.util.*;

public class PresentLoot<Result> implements PresentLootHandler<Result> {
    public record ResultWrapper<Result>(Result res) implements PresentLootHandler<Result> {
        @Override
        public Result handle() {
            return this.res;
        }
    }

    public record TableRecord<Result>(int weight, PresentLootHandler<Result> value) {}

    private List<TableRecord<Result>> table;

    public PresentLoot(List<TableRecord<Result>> table) {
        this.table = new ArrayList<>();

        int keyWeightSum = 0;
        for (TableRecord<Result> resultTableRecord: table) {
            int weight = resultTableRecord.weight;
            PresentLootHandler<Result> handler = resultTableRecord.value;

            keyWeightSum += weight;
            this.table.add(new TableRecord<Result>(keyWeightSum, handler));
        }
    }

    @Override
    public Result handle() {
        Random rand = new Random();
        long seed = Integer.toUnsignedLong(rand.nextInt());
        seed %= this.table.getLast().weight;
        seed += 1;

        TableRecord<Result> forSearch = new TableRecord((int)seed, null);
        int chooseIndex = Collections.binarySearch(this.table, forSearch, new Comparator<TableRecord<Result>>() {
            @Override
            public int compare(TableRecord r1, TableRecord r2) {
                return r1.weight - r2.weight;
            }
        });
        chooseIndex = chooseIndex >= 0 ? chooseIndex : -chooseIndex - 1;

        return this.table.get(chooseIndex).value.handle();
    }

    public Result gen() {
        return this.handle();
    }
}
