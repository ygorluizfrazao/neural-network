/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.utils;

import br.com.frazo.neuralnets.mathfunctions.MathFunction;
import java.util.stream.DoubleStream;

/**
 *
 * @author Ygor
 */
public class IndexOfMaxFunction implements MathFunction{

    @Override
    public double compute(double... variables) {
//        return DoubleStream.of(variables)
//                .reduce(Double.NEGATIVE_INFINITY, (e1,e2) -> Math.max(e1, e2));
        
        int maxIndex=0;
        double maxValue = variables[0];
        for(int i = 1; i<variables.length; i++)
        {
            if(variables[i]>maxValue){
                maxIndex = i;
                maxValue = variables[i];
            }
        }
        return maxIndex;
    }

    @Override
    public int getNumComputationVariables() {
        return -1;
    }

    @Override
    public double primeCompute(double... variables) {
        return 1;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        return -1;
    }
    
}
