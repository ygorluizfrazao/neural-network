/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ygorl
 */
public class NeuralLayer implements Serializable{
    
    private transient NeuralLayer incomingLayer;
    private transient NeuralLayer outgoingLayer;
    
    private ArrayList<Neuron> neurons;

    
    
    public NeuralLayer(NeuralLayer incomingLayer, NeuralLayer outgoingLayer, ArrayList<Neuron> neurons) {
        this.incomingLayer = incomingLayer;
        this.outgoingLayer = outgoingLayer;
        this.neurons = neurons;
    }

    
    
    public NeuralLayer() {
        neurons = new ArrayList<>();
    }
    
    public NeuralLayer getIncomingLayer() {
        return incomingLayer;
    }

    public void setIncomingLayer(NeuralLayer incomingLayer) {
        this.incomingLayer = incomingLayer;
    }

    public NeuralLayer getOutgoingLayer() {
        return outgoingLayer;
    }

    public void setOutgoingLayer(NeuralLayer outgoingLayer) {
        this.outgoingLayer = outgoingLayer;
    }

    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }

    public void setNeurons(ArrayList<Neuron> neurons) {
        this.neurons = neurons;
    }
    
}
