/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.mathfunctions;

/**
 *
 * @author Ygor
 */
public class ReLUFunction implements MathFunction{

    @Override
    public double compute(double... variables) {
        return Math.max(0, variables[0]);
    }

    @Override
    public double primeCompute(double... variables) {
        if(variables[0]>0)
            return 1;
        else
            return 0;
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
