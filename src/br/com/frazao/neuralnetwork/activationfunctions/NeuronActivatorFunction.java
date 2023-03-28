/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork.activationfunctions;

import br.com.frazao.neuralnetwork.Neuron;

/**
 *
 * @author ygorl
 */
public interface NeuronActivatorFunction{
    
    public double activateFunction(Neuron n);
    
    public double primeFunction(Neuron n);
    
    public String printFormula();
}
