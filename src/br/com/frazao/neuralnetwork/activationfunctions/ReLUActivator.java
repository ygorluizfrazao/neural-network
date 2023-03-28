/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.activationfunctions;

import br.com.frazao.neuralnetwork.Neuron;

/**
 *
 * @author Ygor
 */
public class ReLUActivator implements NeuronActivatorFunction{

    @Override
    public double activateFunction(Neuron n) {
        return Math.max(0, n.getWeightSum());
    }

    @Override
    public double primeFunction(Neuron n) {
        if(n.getWeightSum()>=0)
            return 1;
        else
            return 0;
    }

    @Override
    public String printFormula() {
        String s = "";
        s+="Activation: "+"Math.max(0, n.getWeightSum())\n";
        s+="Prime     : "+"        if(n.getWeightSum()>=0)\n" +
                            "            return 1;\n" +
                            "        else\n" +
                            "            return 0;";
        return s;
    }
    
}
