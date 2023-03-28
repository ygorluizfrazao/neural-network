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
public class LeakyReLUFunction implements MathFunction{

    private double leakyConst = 0.01;
    
    @Override
    public double compute(double... variables) {
        return Math.max(leakyConst*variables[0], variables[0]);
    }

    @Override
    public double primeCompute(double... variables) {
        if(variables[0]>=0)
            return 1;
        else
            return leakyConst;
    }

    public LeakyReLUFunction() {
    }

    public LeakyReLUFunction(double leakyConst) {
        this.leakyConst = leakyConst;
    }

    @Override
    public int getNumComputationVariables() {
        return 1;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        return 1;
    }

    public double getLeakyConst() {
        return leakyConst;
    }

    public void setLeakyConst(double leakyConst) {
        this.leakyConst = leakyConst;
    }
    
}
