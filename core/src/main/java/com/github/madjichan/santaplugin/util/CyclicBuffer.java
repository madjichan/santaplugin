package com.github.madjichan.santaplugin.util;

import java.util.ArrayList;

public class CyclicBuffer<T> {
    private ArrayList<T> arr;
    private int size;
    private int currentPushIndex;
    // private int currentPopIndex;

    public CyclicBuffer(int size) {
        this.size = size;
        this.currentPushIndex = 0;
        this.arr = new ArrayList<>(size);
        for(int i=0; i<size; i++) {
            this.arr.add(null);
        }
    }

    public int size() {
        return this.size;
    }

    public T get(int index) {
        return this.arr.get(index);
    }

    public T getWithPushOffset(int index) {
        return this.arr.get((this.currentPushIndex + this.size + index) % this.size);
    }

    public void set(int index, T value) {
        this.arr.set(index, value);
    }

    public void setNext(T value) {
        this.arr.set(this.currentPushIndex, value);
        this.currentPushIndex++;
        this.currentPushIndex %= this.size;
    }
}
