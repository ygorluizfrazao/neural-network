/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import br.com.frazao.neuralnetwork.activationfunctions.NeuronActivatorFunction;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ygorl
 */
public class Neuron implements Serializable{
    
    
    
    private transient ArrayList<Synapse> outgoingConections;
    private transient ArrayList<Synapse> incomingConections;
    
    
    private NeuronActivatorFunction neuronActivatorFunction;
    private double errorInOutput;
    private double sigma;
    private double weightSum;
    private double activatedValue;
    private double biasWeigth=0.0;
    private double biasDelta=0.0;
    private double lastBiasDelta=0.0;
    //for RPROP
    private double biasGradient =0.0;
    private double lastBiasGradient =0.0;
    private double biasRPROPDelta=0.1;
    private double lastBiasRPROPDelta;

    public Neuron() {
    }
    
       
    public Neuron(double biasWeigth, NeuronActivatorFunction neuronActivatorFunction) {
        outgoingConections = new ArrayList<>();
        incomingConections = new ArrayList<>();
        this.biasWeigth=biasWeigth;
        this.neuronActivatorFunction = neuronActivatorFunction;
    }

    public Neuron(ArrayList<Synapse> outgoingConections, ArrayList<Synapse> incomingConections, NeuronActivatorFunction activator, double error, double weightSum, double activatedValue) {
        this.outgoingConections = outgoingConections;
        this.incomingConections = incomingConections;
        this.errorInOutput = error;
        this.weightSum = weightSum;
        this.activatedValue = activatedValue;
    }

    public void connectOutgoingNeuron(Neuron n, Synapse s)
    {
        s.setFromNeuron(this);
        s.setToNeuron(n);
        outgoingConections.add(s);
    }
    
    public void connectOutgoingNeuron(Neuron n, double weight)
    {
        Synapse s = new Synapse(this, n, weight);
        outgoingConections.add(s);
    }
    
    public void connectIncomingNeuron(Neuron n, Synapse s)
    {
        s.setFromNeuron(n);
        s.setToNeuron(this);
        incomingConections.add(s);
    }
    
    public void connectIncomingNeuron(Neuron n, double weight)
    {
        Synapse s = new Synapse(n, this, weight);
        incomingConections.add(s);
    }

    public ArrayList<Synapse> getOutgoingConections() {
        return outgoingConections;
    }

    public void setOutgoingConections(ArrayList<Synapse> outgoingConections) {
        this.outgoingConections = outgoingConections;
    }

    public ArrayList<Synapse> getIncomingConections() {
        return incomingConections;
    }

    public void setIncomingConections(ArrayList<Synapse> incomingConections) {
        this.incomingConections = incomingConections;
    }

    public double getErrorInOutput() {
        return errorInOutput;
    }

    public void setErrorInOutput(double error) {
        this.errorInOutput = error;
    }

    public double getWeightSum() {
        return weightSum;
    }

    public void setWeightSum(double weightSum) {
        this.weightSum = weightSum;
    }

    public double getActivatedValue() {
        return activatedValue;
    }

    public void setActivatedValue(double activatedValue) {
        this.activatedValue = activatedValue;
    }
    
    public void activate()
    {
        activatedValue = neuronActivatorFunction.activateFunction(this);
    }
    
    public void forwardSignal()
    {
        outgoingConections.forEach(synapse -> {
            synapse.putSignal(activatedValue);
        });
    }
    
    public void sumIncommingSignals(double bias)
    {
        //weightSum=bias*biasWeigth;
        weightSum=bias;
        incomingConections.forEach(synapse -> {
            weightSum+=synapse.consumeSignal()*synapse.getWeight();
        });
    }

//    public void calculateSigma(NeuronActivatorFunction neuronActivator)
//    {
//        //delta=outputNeuron.getActivatedValue()*(1-outputNeuron.getActivatedValue())*errors[i]
//        //deltaError = activatedValue*(1-activatedValue)*errorInOutput;
//        sigma = errorInOutput*neuronActivator.primeFunction(this);
//    }
    
    
    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }
    
    public void calculateSigma(double error)
    {
        setSigma(neuronActivatorFunction.primeFunction(this)*error);
    }

    public double getBiasWeigth() {
        return biasWeigth;
    }

    public void setBiasWeigth(double biasWeigth) {
        this.biasWeigth = biasWeigth;
    }

    public double getBiasDelta() {
        return biasDelta;
    }

    public void setBiasDelta(double biasDelta) {
        this.biasDelta = biasDelta;
    }
    
    public void updateBiasWeight()
    {
        biasWeigth+=biasDelta;
        lastBiasDelta = biasDelta;
        biasDelta =0;
    }

    public double getLastBiasDelta() {
        return lastBiasDelta;
    }

    public void setLastBiasDelta(double lastBiasDelta) {
        this.lastBiasDelta = lastBiasDelta;
    }

    public double getBiasGradient() {
        return biasGradient;
    }

    public void setBiasGradient(double biasGradient) {
        this.biasGradient = biasGradient;
    }

    public double getLastBiasGradient() {
        return lastBiasGradient;
    }

    public void setLastBiasGradient(double lastBiasGradient) {
        this.lastBiasGradient = lastBiasGradient;
    }

    public double getBiasRPROPDelta() {
        return biasRPROPDelta;
    }

    public void setBiasRPROPDelta(double biasRPROPDelta) {
        this.biasRPROPDelta = biasRPROPDelta;
    }

    public double getLastBiasRPROPDelta() {
        return lastBiasRPROPDelta;
    }

    public void setLastBiasRPROPDelta(double lastBiasRPROPDelta) {
        this.lastBiasRPROPDelta = lastBiasRPROPDelta;
    }
    
    
    
}
