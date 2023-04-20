package com.example.demo1;

public class PhanSo {
    private int TuSo;
    private int MauSo;

    public PhanSo(int ts, int ms){
        this.TuSo = ts;
        this.MauSo = ms;
    }

    @Override
    public String toString() {
        return this.TuSo + "/" + this.MauSo;
    }
}
