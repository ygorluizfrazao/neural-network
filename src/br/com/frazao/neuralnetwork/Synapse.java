/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import java.io.Serializable;

/**
 *
 * @author ygorl
 */
public class Synapse implements Serializable{
    
    private Neuron fromNeuron;
    private Neuron toNeuron;
    private double weight;
    private double deltaWeight=0.0;
    private double gradient =0.0;
    private double lastGradient =0.0;
    private double RPROPDelta=0.1;
    private double lastRPROPDelta;
    private double lastDeltaWeight=0.0;
    private boolean hasSignal=false;
    private double signalValue;

    public Synapse() {
    }

    public double getSignalValue() {
        return signalValue;
    }

    public void setSignalValue(double signalValue) {
        this.signalValue = signalValue;
    }

    
    
    public Synapse(Neuron fromNeuron, Neuron toNeuron, double weight) {
        this.fromNeuron = fromNeuron;
        this.toNeuron = toNeuron;
        this.weight = weight;
    }

    public void putSignal(double signalValue)
    {
        hasSignal = true;
        this.signalValue = signalValue;
    }
    
    public double consumeSignal()
    {
        hasSignal = false;
        return this.signalValue;
    }

    public boolean isHasSignal() {
        return hasSignal;
    }
    
    public Neuron getFromNeuron() {
        return fromNeuron;
    }

    public void setFromNeuron(Neuron fromNeuron) {
        this.fromNeuron = fromNeuron;
    }

    public Neuron getToNeuron() {
        return toNeuron;
    }

    public void setToNeuron(Neuron toNeuron) {
        this.toNeuron = toNeuron;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }

    public void updateWeight()
    {
        weight+=deltaWeight;
        lastDeltaWeight=deltaWeight;
        deltaWeight = 0;
    }

    public double getLastDeltaWeight() {
        return lastDeltaWeight;
    }

    public void setLastDeltaWeight(double lastDeltaWeight) {
        this.lastDeltaWeight = lastDeltaWeight;
    }

    public double getGradient() {
        return gradient;
    }

    public void setGradient(double gradient) {
        this.gradient = gradient;
    }

    public double getLastGradient() {
        return lastGradient;
    }

    public void setLastGradient(double lastGradient) {
        this.lastGradient = lastGradient;
    }

    public double getRPROPDelta() {
        return RPROPDelta;
    }

    public void setRPROPDelta(double RPROPDelta) {
        this.RPROPDelta = RPROPDelta;
    }

    public double getLastRPROPDelta() {
        return lastRPROPDelta;
    }

    public void setLastRPROPDelta(double lastRPROPDelta) {
        this.lastRPROPDelta = lastRPROPDelta;
    }
    
    
}
