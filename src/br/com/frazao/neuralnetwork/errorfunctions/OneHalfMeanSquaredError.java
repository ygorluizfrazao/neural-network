/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork.errorfunctions;

import java.util.ArrayList;

/**
 *
 * @author ygorl
 */
public class OneHalfMeanSquaredError implements ErrorFunction{

    private ArrayList<Double> targets;
    private ArrayList<Double> outputs;
    private int size;
    

    public OneHalfMeanSquaredError() {
        targets = new ArrayList<>();
        outputs = new ArrayList<>();
        size = 0;
    }
    
//    @Override
//    public double calculateError(double desiredValue, double actualValue) {
//        count++;
//        //n.setErrorInOutput(n.getErrorInOutput()+(actualValue-desiredValue));
//        sumOfSquaredError+=Math.pow((actualValue-desiredValue),2);
//        return sumOfSquaredError/2*count;
//    }

    @Override
    public double getMeanError()
    {
        //return sumOfSquaredError/2*count;
        //return sumOfSquaredError/count;
        return getSumedError()/size;
    }
    
    @Override
    public void acumulateError(double target, double output) {
        //count++;
        //n.setErrorInOutput(n.getErrorInOutput()+(actualValue-desiredValue));
        //sumOfSquaredError+=Math.pow((target-output),2)/2;
        targets.add(target);
        outputs.add(output);
        size++;
    }
    
    @Override
    public double primeErrorFunction(double target, double output) {
        //soma((actual-desired))/qtd
        return -1*(target-output);
    }

    @Override
    public double primeErrorFunction(int index) {
        return primeErrorFunction(targets.get(index), outputs.get(index));
    }
    
    
    @Override
    public void reset()
    {
//        sumOfSquaredError = 0;
//        count = 0;
        targets.clear();
        outputs.clear();
        size = 0;
    }

    @Override
    public double getSumedError() {
        double sumedError=0.0;
        for(int i=0; i<size; i++)
        {
            sumedError+= Math.pow((targets.get(i)-outputs.get(i)),2)/2;
        }
        return sumedError;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Double> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<Double> targets) {
        this.targets = targets;
    }

    public ArrayList<Double> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<Double> outputs) {
        this.outputs = outputs;
    }

    
    
}
