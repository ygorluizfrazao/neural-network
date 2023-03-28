/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import java.util.Date;

/**
 *
 * @author ygorl
 */
public class ForexHistoryEntry {
    
    private int id;
    private Date datetime;
    private double bidOpen;
    private double bidHigh;
    private double bidLow;
    private double bidClose;
    private double volume;

    public ForexHistoryEntry() {
    }

    public ForexHistoryEntry(int id,Date datetime, double bidOpen, double bidHigh, double bidLow, double bidClose, double volume) {
        this.id = id;
        this.datetime = datetime;
        this.bidOpen = bidOpen;
        this.bidHigh = bidHigh;
        this.bidLow = bidLow;
        this.bidClose = bidClose;
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public double getBidOpen() {
        return bidOpen;
    }

    public void setBidOpen(double bidOpen) {
        this.bidOpen = bidOpen;
    }

    public double getBidHigh() {
        return bidHigh;
    }

    public void setBidHigh(double bidHigh) {
        this.bidHigh = bidHigh;
    }

    public double getBidLow() {
        return bidLow;
    }

    public void setBidLow(double bidLow) {
        this.bidLow = bidLow;
    }

    public double getBidClose() {
        return bidClose;
    }

    public void setBidClose(double bidClose) {
        this.bidClose = bidClose;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
