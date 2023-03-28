/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.mathfunctions;

/**
 *
 * @author Ygor
 */
public class MeanSquaredError implements MathFunction{

    public MeanSquaredError() {
    }

    
    
    @Override
    public double compute(double... variables) {
        return Math.pow(variables[0]-variables[1],2);
    }

    @Override
    public double primeCompute(double... variables) {
        return 2*(variables[0]-variables[1]);
    }

    @Override
    public int getNumComputationVariables() {
        return 2;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        return 2;
    }
    
}
