/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.utils;

import br.com.frazo.neuralnets.utils.NNFeeder;

/**
 *
 * @author Ygor
 */
public abstract class KFoldCrossValidationNNfeeder implements NNFeeder {
    
    protected int kFolds;
    protected int currentKFold = -1;
    protected int testSetSize;
    
    public int getkFolds() {
        return kFolds;
    }

    public void setkFolds(int kFolds) {
        this.kFolds = kFolds;
    }

    public int getCurrentKFold() {
        return currentKFold;
    }
    
    public int getTestSetSize(){
        return testSetSize;
    }
    
    public void nextKFoldTestSet(){
        this.currentKFold++;
        if(currentKFold>=kFolds)
            currentKFold=0;
    }
    
    public abstract double[] nextTestInput();
    public abstract double[] getTestExpectedResult();
    public abstract int getTestCurrentTupla();
    
}
