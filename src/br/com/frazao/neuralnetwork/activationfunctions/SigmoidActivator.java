/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork.activationfunctions;

import br.com.frazao.neuralnetwork.Neuron;
import java.io.Serializable;

/**
 *
 * @author ygorl
 */
public class SigmoidActivator implements NeuronActivatorFunction, Serializable{

    @Override
    public double activateFunction(Neuron n) {
        return 1/(1+Math.exp(-1*n.getWeightSum()));
    }

    @Override
    public double primeFunction(Neuron n) {
        //return Math.exp(n.getWeightSum())/Math.pow((1+Math.exp(n.getWeightSum())),2);
        return activateFunction(n)*(1-activateFunction(n));
    }

    public SigmoidActivator() {
    }

    @Override
    public String printFormula() {
        String s = "";
        s+="Activation: "+"1/(1+Math.exp(-1*n.getWeightSum()))\n";
        s+="Prime     : "+"1/(1+Math.exp(-1*n.getWeightSum()))*(1-1/(1+Math.exp(-1*n.getWeightSum())))";
        return s;
    }
    
    
}
