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
public class DistanceToGoalLearningRateAdjuster implements MathFunction{
    
    public double goal;
    public double goalUpperLimit;
    public double upperLimitValue;
    public double lowerLimitValue;

    public DistanceToGoalLearningRateAdjuster(double goal, double goalUpperLimit, double upperLimitValue, double lowerLimitValue) {
        this.goal = goal;
        this.goalUpperLimit = goalUpperLimit;
        this.upperLimitValue = upperLimitValue;
        this.lowerLimitValue = lowerLimitValue;
    }

    public DistanceToGoalLearningRateAdjuster() {
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public double getGoalUpperLimit() {
        return goalUpperLimit;
    }

    public void setGoalUpperLimit(double goalUpperLimit) {
        this.goalUpperLimit = goalUpperLimit;
    }

    public double getUpperLimitValue() {
        return upperLimitValue;
    }

    public void setUpperLimitValue(double upperLimitValue) {
        this.upperLimitValue = upperLimitValue;
    }

    public double getLowerLimitValue() {
        return lowerLimitValue;
    }

    public void setLowerLimitValue(double lowerLimitValue) {
        this.lowerLimitValue = lowerLimitValue;
    }

    @Override
    public double compute(double... variables) {
        double currentValue = (goalUpperLimit*variables[0])/upperLimitValue;
        if(currentValue>goalUpperLimit)
            return goalUpperLimit;
        else if(currentValue<goal)
            return goal;
        else
            return currentValue;
    }

    @Override
    public double primeCompute(double... variables) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumComputationVariables() {
        return 1;
    }

    @Override
    public int getNumPrimeComputationVariables() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
