/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.com.frazao.neuralnetwork;

/**
 *
 * @author Ygor
 */
public interface NNManagerListener {
    
    public void onUpdate(final long epoch, final long maxEpochs, final long iteration, 
            final long maxIterations, final double meanSquaredErrorTotal, final double rmseTotal, 
            final double meanSquaredErrorEpoch, final double rmseEpoch, final long totalIterations,
            final long backPropagations, final double lastOutput, final double lastTarget,
            final double correctAnswers);
}
