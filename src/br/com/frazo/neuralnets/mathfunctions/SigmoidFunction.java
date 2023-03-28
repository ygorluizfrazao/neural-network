/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.mathfunctions;

/**
 *
 * @author Ygor
 */
public class SigmoidFunction implements MathFunction{

    public SigmoidFunction() {
    }

    
    
    @Override
    public double compute(double... variables) {
        return 1/(1+Math.exp(-1*variables[0]));
    }

    @Override
    public double primeCompute(double... variables) {
        double s = compute(variables[0]);
        return s*(1-s);
    }

    @Override
    public int getNumComputationVariables() {
        return 1;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        return 1;
    }
    
}
