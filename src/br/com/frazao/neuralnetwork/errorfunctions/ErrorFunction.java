/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork.errorfunctions;

/**
 *
 * @author ygorl
 */
public interface ErrorFunction {
    
    public void acumulateError(double target, double output);
    
    public double primeErrorFunction(double target, double output);
    
    public double primeErrorFunction(int index);
    
    public double getMeanError();
    
    public double getSumedError();
    
    public void reset();
}
