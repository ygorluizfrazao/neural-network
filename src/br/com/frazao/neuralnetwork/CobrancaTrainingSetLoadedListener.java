/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.frazao.neuralnetwork;

import java.util.List;

/**
 *
 * @author Ygor
 */
public interface CobrancaTrainingSetLoadedListener {
    
    public void newTrainingSet(List<CobrancaPOJO> trainingSet);
    
}
