/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.mathfunctions;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Ygor
 */
public class SoftMaxFunction implements MathFunction{

    @Override
    public double compute(double... variables) {
//        return Math.exp(variables[0])/IntStream.range(0, variables.length)
//                .mapToDouble(i -> Math.exp(variables[i]))
//                .sum();
        //MathFunction sigmoid = new SigmoidFunction();
        double sum =0;
        double max = DoubleStream.of(variables).max().orElse(0);
        for(int i = 0 ; i < variables.length; i++)
            sum+=Math.exp(variables[i]-max);
        return Math.exp(variables[0]-max)/sum;
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
