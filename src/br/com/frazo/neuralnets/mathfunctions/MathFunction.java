/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.mathfunctions;

/**
 *
 * @author Ygor
 */
public interface MathFunction{
    
    public double compute(double... variables);
    
    public int getNumComputationVariables();
    
    public double primeCompute(double... variables);
    
    public int getNumPrimeComputationVariables();
    
}
