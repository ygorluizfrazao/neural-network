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
public class HiperbolicTanActivator implements NeuronActivatorFunction, Serializable{

    @Override
    public double activateFunction(Neuron n) {
        return Math.tanh(n.getWeightSum());
    }

    @Override
    public double primeFunction(Neuron n) {
        return 1-Math.pow(Math.tanh(n.getWeightSum()),2);
    }

    public HiperbolicTanActivator() {
    }

    @Override
    public String printFormula() {
        String s = "";
        s+="Activation: "+"Math.tanh(n.getWeightSum())\n";
        s+="Prime     : "+"1-Math.pow(Math.tanh(n.getWeightSum()),2)";
        return s;
    }
    
    
}
