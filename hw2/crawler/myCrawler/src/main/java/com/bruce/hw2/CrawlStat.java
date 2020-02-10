package com.bruce.hw2;

import java.util.ArrayList;
import java.util.List;

public class CrawlStat {

    private final List<List<String>> recordList1;
    private final List<List<String>> recordList2;
    private final List<List<String>> recordList3;

    public CrawlStat() {
        recordList1 = new ArrayList<>();
        recordList2 = new ArrayList<>();
        recordList3 = new ArrayList<>();
    }

    public void task1(List<String> records1) {
        this.recordList1.add(records1);
    }

    public void task2(List<String> records) {
        this.recordList2.add(records);
    }

    public void task3(List<String> records) {
        this.recordList3.add(records);
    }

    public List<List<String>> getRecord2() {
        return recordList2;
    }

    public List<List<String>> getRecord3() {
        return recordList3;
    }

    public List<List<String>> getRecord1() {
        return recordList1;
    }

}