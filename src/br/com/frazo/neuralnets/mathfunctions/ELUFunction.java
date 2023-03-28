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
public class ELUFunction implements MathFunction{
    
    private double alpha = 1;
    
    @Override
    public double compute(double... variables) {
        return variables[0]>=0?variables[0]:alpha*(Math.exp(variables[0])-1);
    }

    @Override
    public double primeCompute(double... variables) {
        return variables[0]>=0?1:compute(variables)+alpha;
    }

    @Override
    public int getNumComputationVariables() {
        return 1;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        return 1;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
    
    
}
