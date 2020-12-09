package com.group4.softwareanalytics;

public class Pair<L,R> {
    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getKey(){ return l; }
    public R getVal(){ return r; }
    public void setKey(L l){ this.l = l; }
    public void setVal(R r){ this.r = r; }
}