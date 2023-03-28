/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.utils;

/**
 *
 * @author Ygor
 */
public class IrisFlowerPOJO {
    
    private int id;
    private double sepalLen;
    private double sepalWid;
    private double petalLen;
    private double petalWid;
    private String species;
    private double speciesClass;

    public IrisFlowerPOJO(int id, double sepalLen, double sepalWid, double petalLen, double petalWid, String species, double speciesClass) {
        this.id = id;
        this.sepalLen = sepalLen;
        this.sepalWid = sepalWid;
        this.petalLen = petalLen;
        this.petalWid = petalWid;
        this.species = species;
        this.speciesClass = speciesClass;
    }

    public IrisFlowerPOJO() {
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSepalLen() {
        return sepalLen;
    }

    public void setSepalLen(double sepalLen) {
        this.sepalLen = sepalLen;
    }

    public double getSepalWid() {
        return sepalWid;
    }

    public void setSepalWid(double sepalWid) {
        this.sepalWid = sepalWid;
    }

    public double getPetalLen() {
        return petalLen;
    }

    public void setPetalLen(double petalLen) {
        this.petalLen = petalLen;
    }

    public double getPetalWid() {
        return petalWid;
    }

    public void setPetalWid(double petalWid) {
        this.petalWid = petalWid;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getSpeciesClass() {
        return speciesClass;
    }

    public void setSpeciesClass(double speciesClass) {
        this.speciesClass = speciesClass;
    }
    
    
}
