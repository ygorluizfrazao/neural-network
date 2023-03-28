/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets;

import br.com.frazo.neuralnets.mathfunctions.MathFunction;
import br.com.frazo.neuralnets.utils.MatrixUtils;
import br.com.frazo.neuralnets.utils.NNFeeder;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Ygor
 */
public class StochasticArtificialNeuralNetwork {
    
    private double[][] inputNodes;
    private double[][] weightsIH;
    private double[][] biasIH;
    private double[][] outputNodes;
    private double[][] outputNodesWeightSum;
    private double[][] weightsHO;
    private double[][] biasHO;
    private MathFunction[][] outputActivationFunctions;
    private LinkedList<double[][]> hiddenNodes;
    private LinkedList<double[][]> hiddenNodesWeightSum;
    private LinkedList<double[][]> weightsHH;
    private LinkedList<MathFunction[][]> hiddenActivationFunctions;
    private LinkedList<double[][]> biasHH;
    
    private double learningRate;
    private double momentum;
    
    private double[][] previousDeltaWeightsIH;
    private double[][] previousDeltaWeightsHO;
    private LinkedList<double[][]> previousDeltaWeightsHH;
    private double gradientClipNorm = 0;

    public StochasticArtificialNeuralNetwork() {
    }
    
    
    
    public StochasticArtificialNeuralNetwork(double learningRate, double momentum, int inputSize, Class<? extends MathFunction> outputActivationFunctions, 
            int outputSize, Class<? extends MathFunction> hiddenActivationFunctions, int... hiddenSizes) throws Exception {
        
        this.learningRate = learningRate;
        this.momentum = momentum;
        
        //Input Layer
        this.inputNodes = MatrixUtils.createColumnMatrix(inputSize);
        this.biasIH = MatrixUtils.createRandomColumnMatrix(hiddenSizes[0],1);
        this.weightsIH = MatrixUtils.createRandomMatrix(inputSize, hiddenSizes[0]);
        
        //Hidden Layer
        this.hiddenNodes = new LinkedList<>();
        this.biasHH = new LinkedList<>();
        this.weightsHH  = new LinkedList<>();
        this.hiddenActivationFunctions = new LinkedList<>();
        this.hiddenNodesWeightSum = new LinkedList<>();
        for(int i = 0; i < hiddenSizes.length; i++)
        {
            this.hiddenNodes.add(MatrixUtils.createColumnMatrix(hiddenSizes[i]));
            this.hiddenNodesWeightSum.add(MatrixUtils.createColumnMatrix(hiddenSizes[i]));
            this.hiddenActivationFunctions.add(MatrixUtils.createObjectColumnMatrix(hiddenActivationFunctions, hiddenSizes[i]));
            if((i+1)<hiddenSizes.length)
            {
                this.biasHH.add(MatrixUtils.createRandomColumnMatrix(hiddenSizes[i+1], 1));
                this.weightsHH.add(MatrixUtils.createRandomMatrix(hiddenSizes[i], hiddenSizes[i+1]));
            }
        }
        
        //Output Layer
        this.outputActivationFunctions = MatrixUtils.createObjectColumnMatrix(outputActivationFunctions, outputSize);
        this.outputNodes = MatrixUtils.createColumnMatrix(outputSize);
        this.outputNodesWeightSum = MatrixUtils.createColumnMatrix(outputSize);
        this.biasHO = MatrixUtils.createRandomColumnMatrix(outputSize, 1);
        this.weightsHO = MatrixUtils.createRandomMatrix(hiddenSizes[hiddenSizes.length-1],outputSize);
        
        
        //momentum related stuff
        this.previousDeltaWeightsHO = new double[this.weightsHO.length][this.weightsHO[0].length];
        this.previousDeltaWeightsHH = new LinkedList<>();
        for(int i = 0 ; i < this.weightsHH.size(); i++)
            this.previousDeltaWeightsHH.add(new double[this.weightsHH.get(i).length][this.weightsHH.get(i)[0].length]);
        this.previousDeltaWeightsIH = new double[this.weightsIH.length][this.weightsIH[0].length];
    }
    
    public double[][] feedForward(NNFeeder feeder) throws Exception
    {
        //Input Layer
        double[] nextInput = feeder.getNextInput();
        return feedForward(nextInput);
    }
    
    public double[][] feedForward(double... inputs) throws Exception
    {
        //Input Layer
        double[] nextInput = inputs;
        for(int i = 0; i < inputNodes.length; i++)
            inputNodes[i][0] = nextInput[i];
        
        //Hidden Layer
        hiddenNodesWeightSum.set(0,MatrixUtils.add(MatrixUtils.dotProduct(MatrixUtils.transposed(weightsIH), inputNodes ),biasIH));
        hiddenNodes.set(0,computeFunctions(hiddenNodesWeightSum.getFirst(), hiddenActivationFunctions.getFirst()));
        //applyFunctions(hiddenNodes.getFirst(), hiddenActivationFunctions.getFirst());
        for(int h = 1 ; h<hiddenNodes.size(); h++)
        {
            hiddenNodesWeightSum.set(h, MatrixUtils.add(MatrixUtils.dotProduct(MatrixUtils.transposed(weightsHH.get(h-1)), hiddenNodes.get(h-1)),biasHH.get(h-1)));
            hiddenNodes.set(h, computeFunctions(hiddenNodesWeightSum.get(h), hiddenActivationFunctions.get(h)));
        }
        
        //Output Layer
        outputNodesWeightSum = MatrixUtils.add(MatrixUtils.dotProduct(MatrixUtils.transposed(weightsHO), hiddenNodes.getLast()),biasHO);
        outputNodes = computeFunctions(outputNodesWeightSum, outputActivationFunctions);
        //applyFunctions(outputNodes, outputActivationFunctions);
        return outputNodes;
    }
    
    
    public double adjust(NNFeeder feeder, MathFunction lossFunction) throws Exception
    {
        double[] expectedResult = feeder.getExpectedResult();
        return adjust(lossFunction, null, expectedResult);
    }
    
    public double adjust(NNFeeder feeder, MathFunction lossFunction, MathFunction learningDecayFunction) throws Exception
    {
        double[] expectedResult = feeder.getExpectedResult();
        return adjust(lossFunction, learningDecayFunction, expectedResult);
    }
    
    public double adjust(MathFunction lossFunction, MathFunction learningDecayFunction, double... expectedResult) throws Exception
    {
//        double[] variables = new double[outputNodes.length+expectedResult.length];

        double errorTotal=0;
        for(int i = 0; i < outputNodes.length; i++)
        {
//            variables[j] = expectedResult[i];
//            variables[j+1] = outputNodes[i][0];
            errorTotal+=lossFunction.compute(expectedResult[i],outputNodes[i][0]);
        }
        errorTotal = errorTotal/outputNodes.length;
        
        
        double[][] expectedResultMatrix = new double[expectedResult.length][1];
        for(int i =0; i< expectedResultMatrix.length; i++)
            expectedResultMatrix[i][0] = expectedResult[i];
        
        double[][] errorO = computePrimeFunctions(expectedResultMatrix
                , MatrixUtils.createObjectColumnMatrix(lossFunction.getClass(), outputNodes.length)
                , outputNodes);
        double[][] sigmaO = computePrimeFunctions(outputNodes, outputActivationFunctions);
        //sigmaO = MatrixUtils.transposed(sigmaO);
        sigmaO = MatrixUtils.hadamardProduct(sigmaO, errorO);
        
        double[][] deltaHO = MatrixUtils.dotProduct(hiddenNodes.getLast(),MatrixUtils.transposed(sigmaO));
        
        if(gradientClipNorm>0 && MatrixUtils.vectorNorm(deltaHO, 2)>gradientClipNorm)
            deltaHO = MatrixUtils.scaleToVectorNorm(deltaHO, 2, gradientClipNorm);
        
        LinkedList<double[][]> errorH = new LinkedList<>();
        LinkedList<double[][]> sigmaH = new LinkedList<>();
        LinkedList<double[][]> deltaHH = new LinkedList<>();
        
        
        double[][] errorHLast = MatrixUtils.dotProduct(weightsHO, sigmaO);
        double[][] sigmaHLast = computePrimeFunctions(hiddenNodes.getLast(), hiddenActivationFunctions.getLast());
        sigmaHLast = MatrixUtils.hadamardProduct(sigmaHLast, errorHLast);
        
        errorH.add(errorHLast);
        sigmaH.add(sigmaHLast);
        for(int w = weightsHH.size()-1; w>=0; w--)
        {
            double[][] currentDeltaH = MatrixUtils.dotProduct(hiddenNodes.get(w), MatrixUtils.transposed(sigmaH.getLast()));
            if(gradientClipNorm>0 && MatrixUtils.vectorNorm(currentDeltaH, 2)>gradientClipNorm)
                currentDeltaH = MatrixUtils.scaleToVectorNorm(currentDeltaH, 2, gradientClipNorm);
            deltaHH.add(0,currentDeltaH);
            double[][] currentErrorH = MatrixUtils.dotProduct(weightsHH.get(w),sigmaH.getLast());
            errorH.add(currentErrorH);
            double[][] currentSigmaH = computePrimeFunctions(hiddenNodes.get(w), hiddenActivationFunctions.get(w));
            
            currentSigmaH = MatrixUtils.hadamardProduct(currentSigmaH, currentErrorH);
            sigmaH.add(currentSigmaH);
        }
        double[][] deltaIH = MatrixUtils.dotProduct(inputNodes,MatrixUtils.transposed(sigmaH.getLast()));
        if(gradientClipNorm>0 && MatrixUtils.vectorNorm(deltaIH, 2)>gradientClipNorm)
            deltaIH = MatrixUtils.scaleToVectorNorm(deltaIH, 2, gradientClipNorm);

        deltaHO = MatrixUtils.add(MatrixUtils.scalarProduct(-learningRate, deltaHO),MatrixUtils.scalarProduct(momentum,previousDeltaWeightsHO));
        weightsHO = MatrixUtils.add(weightsHO, deltaHO);
        biasHO = MatrixUtils.add(biasHO, MatrixUtils.scalarProduct(-learningRate, sigmaO));
        
        Collections.reverse(sigmaH);
        for(int w = weightsHH.size()-1; w>=0 ; w--){
            double[][] delta = MatrixUtils.add(MatrixUtils.scalarProduct(-learningRate, deltaHH.get(w)),MatrixUtils.scalarProduct(momentum, previousDeltaWeightsHH.get(w)));
            deltaHH.set(w, delta);
            weightsHH.set(w, MatrixUtils.add(weightsHH.get(w), delta));
            biasHH.set(w, MatrixUtils.add(biasHH.get(w), MatrixUtils.scalarProduct(-learningRate, sigmaH.get(w+1))));
        }
        deltaIH = MatrixUtils.add(MatrixUtils.scalarProduct(-learningRate, deltaIH),MatrixUtils.scalarProduct(momentum, previousDeltaWeightsIH));
        weightsIH = MatrixUtils.add(weightsIH, deltaIH);
        biasIH = MatrixUtils.add(biasIH, MatrixUtils.scalarProduct(-learningRate, sigmaH.getFirst()));
        
        previousDeltaWeightsHO = deltaHO;
        previousDeltaWeightsIH = deltaIH;
        previousDeltaWeightsHH = deltaHH;
        
        if(learningDecayFunction!=null)
            learningRate = learningDecayFunction.compute(errorTotal, learningRate);
        return errorTotal;
    }
    
    
    private static void applyFunctions(double[][] A, MathFunction[][] functions)
    {
        for(int i = 0; i<A.length; i++)
            for(int j = 0; j<A[0].length; j++)
                A[i][j] = functions[i][j].compute(A[i][j]);
    }
    
    public static double[][] computeFunctions(double[][] A, MathFunction[][] functions)
    {
        double[][] B = new double[A.length][A[0].length];
        for(int i = 0; i<A.length; i++)
            for(int j = 0; j<A[0].length; j++)
                B[i][j] = functions[i][j].compute(A[i][j]);
        return B;
    }
    
    public static double[][] computeFunctions(double[][] A, MathFunction[][] functions, double[][]... extraVariables)
    {
        double[][] B = new double[A.length][A[0].length];
        for(int i = 0; i<A.length; i++)
            for(int j = 0; j<A[0].length; j++){
                for(int e = 0 ; e < extraVariables.length; e++)
                {
                    B[i][j] += functions[i][j].compute(A[i][j], extraVariables[e][i][j]);
                }
            }
        return B;
    }
    
    private static void applyPrimeFunctions(double[][] A, MathFunction[][] functions)
    {
        for(int i = 0; i<A.length; i++)
            for(int j = 0; j<A[0].length; j++)
                A[i][j] = functions[i][j].primeCompute(A[i][j]);
    }
    
    public static double[][] computePrimeFunctions(double[][] A, MathFunction[][] functions)
    {
        double[][] B = new double[A.length][A[0].length];
        for(int i = 0; i<A.length; i++)
            for(int j = 0; j<A[0].length; j++)
                B[i][j] = functions[i][j].primeCompute(A[i][j]);
        return B;
    }
    
    public static double[][] computePrimeFunctions(double[][] A, MathFunction[][] functions, double[][]... extraVariables)
    {
        double[][] B = new double[A.length][A[0].length];
        for(int i = 0; i<A.length; i++)
            for(int j = 0; j<A[0].length; j++){
                for(int e = 0 ; e < extraVariables.length; e++)
                {
                    B[i][j] += functions[i][j].primeCompute(A[i][j], extraVariables[e][i][j]);
                }
            }
        return B;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }
    
    public void saveNNToFile(File f) throws IOException
    {
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        new Gson().toJson(this, fw);
        fw.flush();
        fw.close();
    }
    
    public void saveNNToFile(String fileName) throws IOException
    {
        File f = new File(fileName);
        if(f.exists())
            f.delete();
        f.createNewFile();
        saveNNToFile(f);
    }

    public double[][] getInputNodes() {
        return inputNodes;
    }

    public void setInputNodes(double[][] inputNodes) {
        this.inputNodes = inputNodes;
    }

    public double[][] getWeightsIH() {
        return weightsIH;
    }

    public void setWeightsIH(double[][] weightsIH) {
        this.weightsIH = weightsIH;
    }

    public double[][] getBiasIH() {
        return biasIH;
    }

    public void setBiasIH(double[][] biasIH) {
        this.biasIH = biasIH;
    }

    public double[][] getOutputNodes() {
        return outputNodes;
    }

    public void setOutputNodes(double[][] outputNodes) {
        this.outputNodes = outputNodes;
    }

    public double[][] getWeightsHO() {
        return weightsHO;
    }

    public void setWeightsHO(double[][] weightsHO) {
        this.weightsHO = weightsHO;
    }

    public double[][] getBiasHO() {
        return biasHO;
    }

    public void setBiasHO(double[][] biasHO) {
        this.biasHO = biasHO;
    }

    public MathFunction[][] getOutputActivationFunctions() {
        return outputActivationFunctions;
    }

    public void setOutputActivationFunctions(MathFunction[][] outputActivationFunctions) {
        this.outputActivationFunctions = outputActivationFunctions;
    }

    public LinkedList<double[][]> getHiddenNodes() {
        return hiddenNodes;
    }

    public void setHiddenNodes(LinkedList<double[][]> hiddenNodes) {
        this.hiddenNodes = hiddenNodes;
    }

    public LinkedList<double[][]> getWeightsHH() {
        return weightsHH;
    }

    public void setWeightsHH(LinkedList<double[][]> weightsHH) {
        this.weightsHH = weightsHH;
    }

    public LinkedList<MathFunction[][]> getHiddenActivationFunctions() {
        return hiddenActivationFunctions;
    }

    public void setHiddenActivationFunctions(LinkedList<MathFunction[][]> hiddenActivationFunctions) {
        this.hiddenActivationFunctions = hiddenActivationFunctions;
    }

    public LinkedList<double[][]> getBiasHH() {
        return biasHH;
    }

    public void setBiasHH(LinkedList<double[][]> biasHH) {
        this.biasHH = biasHH;
    }

    public double[][] getPreviousDeltaWeightsIH() {
        return previousDeltaWeightsIH;
    }

    public void setPreviousDeltaWeightsIH(double[][] previousDeltaWeightsIH) {
        this.previousDeltaWeightsIH = previousDeltaWeightsIH;
    }

    public double[][] getPreviousDeltaWeightsHO() {
        return previousDeltaWeightsHO;
    }

    public void setPreviousDeltaWeightsHO(double[][] previousDeltaWeightsHO) {
        this.previousDeltaWeightsHO = previousDeltaWeightsHO;
    }

    public LinkedList<double[][]> getPreviousDeltaWeightsHH() {
        return previousDeltaWeightsHH;
    }

    public void setPreviousDeltaWeightsHH(LinkedList<double[][]> previousDeltaWeightsHH) {
        this.previousDeltaWeightsHH = previousDeltaWeightsHH;
    }

    public double getGradientClipNorm() {
        return gradientClipNorm;
    }

    public void setGradientClipNorm(double gradientClipNorm) {
        this.gradientClipNorm = gradientClipNorm;
    }

    public double[][] getOutputNodesWeightSum() {
        return outputNodesWeightSum;
    }

    public void setOutputNodesWeightSum(double[][] outputNodesWeightSum) {
        this.outputNodesWeightSum = outputNodesWeightSum;
    }

    public LinkedList<double[][]> getHiddenNodesWeightSum() {
        return hiddenNodesWeightSum;
    }

    public void setHiddenNodesWeightSum(LinkedList<double[][]> hiddenNodesWeightSum) {
        this.hiddenNodesWeightSum = hiddenNodesWeightSum;
    }
    
    
}
