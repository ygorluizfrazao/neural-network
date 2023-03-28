/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.utils;

import br.com.frazo.neuralnets.mathfunctions.MathFunction;

/**
 *
 * @author Ygor
 */
public class CrossEntropyError implements MathFunction{

    @Override
    public double compute(double... variables) {
        double error=0;
        for(int i = 0; i<variables.length/2;i+=2)
            error+=variables[i]*Math.log(variables[i+1]);
        return -error;
    }

    @Override
    public int getNumComputationVariables() {
        return 2;
    }

    @Override
    public double primeCompute(double... variables) {
        double error=0;
        for(int i = 0; i<variables.length/2;i+=2)
            error+=variables[i+1] - variables[i];
        return error;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        return 2;
    }
    
}
