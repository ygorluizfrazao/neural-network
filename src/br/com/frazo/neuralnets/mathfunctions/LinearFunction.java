/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.mathfunctions;

import br.com.frazo.neuralnets.mathfunctions.MathFunction;

/**
 *
 * @author Ygor
 */
public class LinearFunction implements MathFunction{

    public LinearFunction() {
    }

    
    
    @Override
    public double compute(double... variables) {
        return variables[0];
    }

    @Override
    public double primeCompute(double... variables) {
        return 1;
    }

    @Override
    public int getNumComputationVariables() {
        return 1;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        return 0;
    }
    
}
