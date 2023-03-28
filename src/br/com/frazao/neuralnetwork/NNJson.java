/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork;

import java.util.List;

/**
 *
 * @author Ygor
 */
public class NNJson {
    
    public static final int SIGMOID = 0;
    public static final int HYPERBOLIC_TANGENT = 1;
    
    private int[] sizes;
    private double[] weigths;
    private int neuronActivatorFunction = SIGMOID;
    private double bias;
    
    public NNJson() {
    }

    public NNJson(int[] sizes, double[] weigths, double bias) {
        this.sizes = sizes;
        this.weigths = weigths;
        this.bias = bias;
    }

    public int[] getSizes() {
        return sizes;
    }

    public void setSizes(int[] sizes) {
        this.sizes = sizes;
    }

    public double[] getWeigths() {
        return weigths;
    }

    public void setWeigths(double[] weigths) {
        this.weigths = weigths;
    }

    public int getNeuronActivatorFunction() {
        return neuronActivatorFunction;
    }

    public void setNeuronActivatorFunction(int neuronActivatorFunction) {
        this.neuronActivatorFunction = neuronActivatorFunction;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

}
