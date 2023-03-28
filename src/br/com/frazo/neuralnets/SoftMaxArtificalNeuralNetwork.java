/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets;

import br.com.frazo.neuralnets.utils.CrossEntropyError;
import br.com.frazo.neuralnets.mathfunctions.MathFunction;
import br.com.frazo.neuralnets.utils.MatrixUtils;
import br.com.frazo.neuralnets.utils.NNFeeder;
import br.com.frazo.neuralnets.mathfunctions.SoftMaxFunction;
import br.com.frazo.neuralnets.StochasticArtificialNeuralNetwork;
import java.util.LinkedList;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Ygor
 */
public class SoftMaxArtificalNeuralNetwork extends StochasticArtificialNeuralNetwork{

    public SoftMaxArtificalNeuralNetwork(double learningRate, double momentum, int inputSize, 
            int outputSize, Class<? extends MathFunction> hiddenActivationFunctions, int... hiddenSizes) throws Exception {
        super(learningRate, momentum, inputSize, SoftMaxFunction.class, outputSize, hiddenActivationFunctions, hiddenSizes);
        //getHiddenActivationFunctions().set(getHiddenActivationFunctions().size()-1, MatrixUtils.createObjectColumnMatrix(SigmoidFunction.class, hiddenSizes[hiddenSizes.length-1]));
        
    }
    
    public double softMaxAdjust(NNFeeder nNFeeder, MathFunction learninDecayFunction) throws Exception
    {
        return adjust(nNFeeder, new CrossEntropyError(), learninDecayFunction);
    }

    @Override
    public double[][] feedForward(double... inputs) throws Exception
    {
        //Input Layer
        double[] nextInput = inputs;
        for(int i = 0; i < getInputNodes().length; i++)
            getInputNodes()[i][0] = nextInput[i];
        //Hidden Layer
        getHiddenNodesWeightSum().set(0,MatrixUtils.add(MatrixUtils.dotProduct(MatrixUtils.transposed(getWeightsIH()), getInputNodes() ),getBiasIH()));
        getHiddenNodes().set(0,computeFunctions(getHiddenNodesWeightSum().getFirst(), getHiddenActivationFunctions().getFirst()));
        for(int h = 1 ; h<getHiddenNodes().size(); h++)
        {
            getHiddenNodesWeightSum().set(h, MatrixUtils.add(MatrixUtils.dotProduct(MatrixUtils.transposed(getWeightsHH().get(h-1)), getHiddenNodes().get(h-1)),getBiasHH().get(h-1)));
            getHiddenNodes().set(h, computeFunctions(getHiddenNodesWeightSum().get(h), getHiddenActivationFunctions().get(h)));
        }
        
        //Output Layer
        setOutputNodesWeightSum(MatrixUtils.add(MatrixUtils.dotProduct(MatrixUtils.transposed(getWeightsHO()), getHiddenNodes().getLast()),getBiasHO()));
        //setOutputNodes(computeFunctions(getOutputNodesWeightSum(), getOutputActivationFunctions()));
        double[][] newOutNodes = new double[getOutputNodes().length][getOutputNodes()[0].length];
        for(int i = 0; i< getOutputNodes().length; i++)
        {
            double[][] outNods = MatrixUtils.transposed(getOutputNodesWeightSum());
            double currentV = outNods[0][i];
            outNods[0][i] = outNods[0][0];
            outNods[0][0] = currentV;
            newOutNodes[i][0] = getOutputActivationFunctions()[i][0].compute(outNods[0]);
        }
        setOutputNodes(newOutNodes);
        //applyFunctions(outputNodes, outputActivationFunctions);
        return getOutputNodes();
    }
    
    @Override
    public double adjust(MathFunction lossFunction, MathFunction learningDecayFunction, double... expectedResult) throws Exception {

        double errorTotal=0;
        for(int i = 0; i < getOutputNodes().length; i++)
        {
            errorTotal+=lossFunction.compute(expectedResult[i],getOutputNodes()[i][0]);
        }
        
        
        double[][] expectedResultMatrix = new double[expectedResult.length][1];
        for(int i =0; i< expectedResultMatrix.length; i++)
            expectedResultMatrix[i][0] = expectedResult[i];
        
        double[][] errorO = computePrimeFunctions(expectedResultMatrix
                , MatrixUtils.createObjectColumnMatrix(lossFunction.getClass(), getOutputNodes().length)
                , getOutputNodes());
        
        double[][] deltaHO = MatrixUtils.dotProduct(getHiddenNodes().getLast(),MatrixUtils.transposed(errorO));
        if(getGradientClipNorm()>0 && MatrixUtils.vectorNorm(deltaHO, 2)>getGradientClipNorm())
            deltaHO = MatrixUtils.scaleToVectorNorm(deltaHO, 2, getGradientClipNorm());
        
        LinkedList<double[][]> errorH = new LinkedList<>();
        LinkedList<double[][]> sigmaH = new LinkedList<>();
        LinkedList<double[][]> deltaHH = new LinkedList<>();
        
        
        double[][] errorHLast = MatrixUtils.dotProduct(getWeightsHO(), errorO);
        double[][] sigmaHLast = computePrimeFunctions(getHiddenNodes().getLast(), getHiddenActivationFunctions().getLast());
        
        sigmaHLast = MatrixUtils.hadamardProduct(sigmaHLast, errorHLast);
        errorH.add(errorHLast);
        sigmaH.add(sigmaHLast);
        for(int w = getWeightsHH().size()-1; w>=0; w--)
        {
            double[][] currentDeltaH = MatrixUtils.dotProduct(getHiddenNodes().get(w), MatrixUtils.transposed(sigmaH.getLast()));
            if(getGradientClipNorm()>0 && MatrixUtils.vectorNorm(currentDeltaH, 2)>getGradientClipNorm())
                currentDeltaH = MatrixUtils.scaleToVectorNorm(currentDeltaH, 2, getGradientClipNorm());
            deltaHH.add(0,currentDeltaH);
            double[][] currentErrorH = MatrixUtils.dotProduct(getWeightsHH().get(w),sigmaH.getLast());
            errorH.add(currentErrorH);
            double[][] currentSigmaH = computePrimeFunctions(getHiddenNodes().get(w), getHiddenActivationFunctions().get(w));

            currentSigmaH = MatrixUtils.hadamardProduct(currentSigmaH, currentErrorH);
            sigmaH.add(currentSigmaH);
        }
        double[][] deltaIH = MatrixUtils.dotProduct(getInputNodes(),MatrixUtils.transposed(sigmaH.getLast()));
        if(getGradientClipNorm()>0 && MatrixUtils.vectorNorm(deltaIH, 2)>getGradientClipNorm())
            deltaIH = MatrixUtils.scaleToVectorNorm(deltaIH, 2, getGradientClipNorm());

        deltaHO = MatrixUtils.add(MatrixUtils.scalarProduct(-getLearningRate(), deltaHO),MatrixUtils.scalarProduct(getMomentum(),getPreviousDeltaWeightsHO()));
        setWeightsHO(MatrixUtils.add(getWeightsHO(), deltaHO));
        
        for(int w = getWeightsHH().size()-1; w>=0 ; w--){
            double[][] delta = MatrixUtils.add(MatrixUtils.scalarProduct(-getLearningRate(), deltaHH.get(w)),MatrixUtils.scalarProduct(getMomentum(), getPreviousDeltaWeightsHH().get(w)));
            deltaHH.set(w, delta);
            getWeightsHH().set(w, MatrixUtils.add(getWeightsHH().get(w), delta));
        }
        deltaIH = MatrixUtils.add(MatrixUtils.scalarProduct(-getLearningRate(), deltaIH),MatrixUtils.scalarProduct(getMomentum(), getPreviousDeltaWeightsIH()));
        setWeightsIH(MatrixUtils.add(getWeightsIH(), deltaIH));
        
        setPreviousDeltaWeightsHO(deltaHO);
        setPreviousDeltaWeightsIH(deltaIH);
        setPreviousDeltaWeightsHH(deltaHH);
        
        if(learningDecayFunction!=null)
            setLearningRate(learningDecayFunction.compute(getLearningRate()));
        return errorTotal;
    }
    
//    public void adjustByCumulativeError(MathFunction learningDecayFunction, double[] errorOut) throws Exception {
//
//        
//        double[][] errorO = new double[getOutputNodes().length][getOutputNodes()[0].length];
//        for(int i = 0; i< errorOut.length; i++)
//            errorO[i][0] = errorOut[i];
////        double[][] sigmaO = IntStream.range(0, getOutputNodes().length)
////                                .mapToObj(a -> 
////                                            DoubleStream.generate(() -> Double.valueOf(1))
////                                            .limit(1)
////                                            .toArray())
////                                .toArray(double[][]::new);
////        //sigmaO = MatrixUtils.transposed(sigmaO);
////        sigmaO = MatrixUtils.hadamardProduct(sigmaO, errorO);
//        
//        double[][] deltaHO = MatrixUtils.dotProduct(getHiddenNodes().getLast(),MatrixUtils.transposed(errorO));
//        if(getGradientClipNorm()>0 && MatrixUtils.vectorNorm(deltaHO, 2)>getGradientClipNorm())
//            deltaHO = MatrixUtils.scaleToVectorNorm(deltaHO, 2, getGradientClipNorm());
//        
//        LinkedList<double[][]> errorH = new LinkedList<>();
//        LinkedList<double[][]> sigmaH = new LinkedList<>();
//        LinkedList<double[][]> deltaHH = new LinkedList<>();
//        
//        
//        double[][] errorHLast = MatrixUtils.dotProduct(getWeightsHO(), errorO);
//        double[][] sigmaHLast = computePrimeFunctions(getHiddenNodes().getLast(), getHiddenActivationFunctions().getLast());
//        
//        sigmaHLast = MatrixUtils.hadamardProduct(sigmaHLast, errorHLast);
//        errorH.add(errorHLast);
//        sigmaH.add(sigmaHLast);
//        for(int w = getWeightsHH().size()-1; w>=0; w--)
//        {
//            double[][] currentDeltaH = MatrixUtils.dotProduct(getHiddenNodes().get(w), MatrixUtils.transposed(sigmaH.getLast()));
//            if(getGradientClipNorm()>0 && MatrixUtils.vectorNorm(currentDeltaH, 2)>getGradientClipNorm())
//                currentDeltaH = MatrixUtils.scaleToVectorNorm(currentDeltaH, 2, getGradientClipNorm());
//            deltaHH.add(0,currentDeltaH);
//            double[][] currentErrorH = MatrixUtils.dotProduct(getWeightsHH().get(w),sigmaH.getLast());
//            errorH.add(currentErrorH);
//            double[][] currentSigmaH = computePrimeFunctions(getHiddenNodes().get(w), getHiddenActivationFunctions().get(w));
//            
//            currentSigmaH = MatrixUtils.hadamardProduct(currentSigmaH, currentErrorH);
//            sigmaH.add(currentSigmaH);
//        }
//        double[][] deltaIH = MatrixUtils.dotProduct(getInputNodes(),MatrixUtils.transposed(sigmaH.getLast()));
//        if(getGradientClipNorm()>0 && MatrixUtils.vectorNorm(deltaIH, 2)>getGradientClipNorm())
//            deltaIH = MatrixUtils.scaleToVectorNorm(deltaIH, 2, getGradientClipNorm());
//
//        deltaHO = MatrixUtils.add(MatrixUtils.scalarProduct(-getLearningRate(), deltaHO),MatrixUtils.scalarProduct(getMomentum(),getPreviousDeltaWeightsHO()));
//        setWeightsHO(MatrixUtils.add(getWeightsHO(), deltaHO));
//        
//        for(int w = getWeightsHH().size()-1; w>=0 ; w--){
//            double[][] delta = MatrixUtils.add(MatrixUtils.scalarProduct(-getLearningRate(), deltaHH.get(w)),MatrixUtils.scalarProduct(getMomentum(), getPreviousDeltaWeightsHH().get(w)));
//            deltaHH.set(w, delta);
//            getWeightsHH().set(w, MatrixUtils.add(getWeightsHH().get(w), delta));
//        }
//        deltaIH = MatrixUtils.add(MatrixUtils.scalarProduct(-getLearningRate(), deltaIH),MatrixUtils.scalarProduct(getMomentum(), getPreviousDeltaWeightsIH()));
//        setWeightsIH(MatrixUtils.add(getWeightsIH(), deltaIH));
//        
//        setPreviousDeltaWeightsHO(deltaHO);
//        setWeightsIH(deltaIH);
//        setPreviousDeltaWeightsHH(deltaHH);
//        
//        if(learningDecayFunction!=null)
//            setLearningRate(learningDecayFunction.compute(getLearningRate()));
//    }
}
