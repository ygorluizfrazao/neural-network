/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.utils;

import br.com.frazo.neuralnets.utils.MatrixUtils;
import br.com.frazo.neuralnets.mathfunctions.SoftMaxFunction;
import br.com.frazao.neuralnetwork.LineChart_AWT;
import br.com.frazao.neuralnetwork.activationfunctions.NeuronActivatorFunction;
import br.com.frazao.neuralnetwork.activationfunctions.ReLUActivator;
import br.com.frazao.neuralnetwork.activationfunctions.SigmoidActivator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Ygor
 */
public class MatrixUtilsTest {
    
    public static void main(String[] args) {
        
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            
//            double A[][] = MatrixUtils.createRandomMatrix(2, 3);
//            System.out.println("A:\n"+ MatrixUtils.toString(A,df));
//            
//            double B[][] = MatrixUtils.createRandomMatrix(3, 2);
//            System.out.println("B:\n"+ MatrixUtils.toString(B,df));
//            
//            double C[][] = MatrixUtils.createMatrix(3, 2,
//                    1,2,
//                    3,4,
//                    5,6);
//            System.out.println("C:\n"+ MatrixUtils.toString(C,df));
//            
            //System.out.println(Stream.generate(() -> Math.random()>=0.8?1:0).limit(10).collect(Collectors.toList()));
            
            
            double[][] d1 = MatrixUtils.createBernoulliDistributionMatrix(10, 11, 0.8);
            
            System.out.println(MatrixUtils.toString(d1));
//            IntStream.range(0, d1.length-1)
//                    .forEach( ii ->
//                        System.out.println("|"+DoubleStream.of(d1[ii])
//                            .mapToObj(d -> Double.toString(d))
//                            .collect(Collectors.joining(","))+"|"));
            
            System.out.println(Arrays.asList(6.,10.0,8.,10.0,10.0).stream().collect(Collectors.summarizingDouble(d->d)));
            double D[][] = MatrixUtils.createMatrix(2, 2,
                    1, 3,
                    2,-1);
            //System.out.println(CobrancaFeeder.QUERY_COBRANCA_PAGANTES);
            System.out.println("D:\n"+ MatrixUtils.toString(D,df));
            System.out.println("D:\n"+ MatrixUtils.vectorNorm(D,2));
            double[][] E = MatrixUtils.scaleToVectorNorm(D, 2, 3.872983346207417);
            System.out.println("E:\n"+ MatrixUtils.toString(E,df));
            System.out.println("E:\n"+ MatrixUtils.vectorNorm(E,2));
            
//            double[] dd = {1, 10, 10};
//            System.out.println(new SoftMaxFunction().compute(dd));
            
            double[][] sigmaO = IntStream.range(1, 8)
                                .mapToObj(a -> 
                                            DoubleStream.generate(() -> Double.valueOf(1))
                                            .limit(1)
                                            .toArray())
                                .toArray(double[][]::new);
            double s1 = 1.8;
            double s2 = 0.9;
            double s3 = 0.68;
            
            System.out.println(new SoftMaxFunction().compute(s1,s2,s3)+","+new SoftMaxFunction().compute(s2,s1,s3)+","+new SoftMaxFunction().compute(s3,s2,s1));
//            System.out.println("D:\n"+ MatrixUtils.toString(D,df));
//            
//            double E[][] = MatrixUtils.diagonal(C);
//            
//            
//            System.out.println("E:\n"+ MatrixUtils.toString(E,df));
//            //System.out.println(MatrixUtils.toString(MatrixUtils.createRandomMatrix(2, 3), new DecimalFormat("0.000")));
            
//            int inputNodes = 1;
//            double[][] activatedValuesI = MatrixUtils.createColumnMatrix(inputNodes);
//            int hiddenNodes = 8;
//            double[][] activatedValuesH = MatrixUtils.createColumnMatrix(hiddenNodes);
//            double[][] weightsIH = MatrixUtils.createRandomMatrix(hiddenNodes, inputNodes);
//            double[][] biasIH = MatrixUtils.createRandomColumnMatrix(hiddenNodes,1);
//            LeakyReLUFunction[][] hiddenActivators = MatrixUtils.createObjectColumnMatrix(LeakyReLUFunction.class,hiddenNodes);
//            int outputNodes = 1;
//            double[][] activatedValuesO = MatrixUtils.createColumnMatrix(outputNodes);
//            double[][] weightsHO = MatrixUtils.createRandomMatrix(outputNodes, hiddenNodes);
//            double[][] biasHO = MatrixUtils.createRandomColumnMatrix(outputNodes,1);
//            SigmoidFunction[][] outputActivators = MatrixUtils.createObjectColumnMatrix(SigmoidFunction.class,outputNodes);
//            
//            System.out.println("Input Neurons:\n"+ MatrixUtils.toString(activatedValuesI,df));
//            System.out.println("Weigths I -> H:\n"+ MatrixUtils.toString(weightsIH,df));
//            System.out.println("Bias I -> H:\n"+ MatrixUtils.toString(biasIH,df));
//            System.out.println("Hidden Neurons:\n"+ MatrixUtils.toString(activatedValuesH,df));
//            System.out.println("Hidden Biases:\n"+ MatrixUtils.toString(biasIH,df));
//            System.out.println("Weigths H -> O:\n"+ MatrixUtils.toString(weightsHO,df));
//            System.out.println("Bias H -> O:\n"+ MatrixUtils.toString(biasHO,df));
//            System.out.println("Output Neurons:\n"+ MatrixUtils.toString(activatedValuesO,df));
//            System.out.println("Output Biases:\n"+ MatrixUtils.toString(biasHO,df));
//            
//            double[][] inputs = MatrixUtils.createRandomMatrix(200, inputNodes, Math.PI*2);
//            inputs = MatrixUtils.sortedColumnMatrix(inputs);
//            
//            LineChart_AWT chart = new LineChart_AWT("Neural Network BenchMark","Angle", "Sine(Angle)");
//            chart.pack( );
//            RefineryUtilities.centerFrameOnScreen( chart );
//            chart.setVisible( true );
//            double previousMSE = 1;
//            double learningRate = 0.1;
//            for(int epoch = 0; epoch<10000; epoch++)
//            {
//                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//                for(int input = 0; input<inputs.length; input++)
//                {
//                    activatedValuesI[0][0]  = inputs[input][0];
//                    System.out.println("Input Neurons:\n"+ MatrixUtils.toString(activatedValuesI,df));
//                    
//                    double[][] errorO;
//                    double[][] sigmaO;
//                    double[][] errorH;
//                    double[][] sigmaH;
//                    double[][] deltaHO;
//                    double[][] deltaIH;
//                    
//                    //feed forward
//                    activatedValuesH = MatrixUtils.add(MatrixUtils.dotProduct(weightsIH, activatedValuesI),biasIH);
//                    //System.out.println("WIH.H:\n"+ MatrixUtils.toString(activatedValuesH,df));
//                    applyFunctions(activatedValuesH, hiddenActivators);
//                    //System.out.println("Hidden Neurons:\n"+ MatrixUtils.toString(activatedValuesH,df));
//                    activatedValuesO = MatrixUtils.add(MatrixUtils.dotProduct(weightsHO, activatedValuesH),biasHO);
//                    //System.out.println("WHO.O:\n"+ MatrixUtils.toString(activatedValuesO,df));
//                    applyFunctions(activatedValuesO, outputActivators);
//                    
//                    System.out.println("Output Neurons:\n"+ MatrixUtils.toString(activatedValuesO,df));
//                    System.out.println("Expected Result = "+((Math.sin(inputs[input][0])+1)/2)+"\nResult = "+activatedValuesO[0][0]);
//                    double MSE = Math.pow(((Math.sin(inputs[input][0])+1)/2)-activatedValuesO[0][0],2)/2;
//                    System.out.println("PMSE = "+previousMSE);
//                    System.out.println("MSE = "+MSE);
////                    if(MSE*5>previousMSE)
////                    {
////                        System.out.println("New Learning Rate("+input+") = "+(learningRate*0.9));
////                        learningRate = learningRate*0.9;
////                    }
//                    System.out.println("New Learning Rate("+epoch+") = "+learningRate);
//                    previousMSE = MSE;
//                    
//                    
//                    //backpropagate
//                    errorO = computePrimeFunctions(MatrixUtils.createColumnMatrix((Math.sin(inputs[input][0])+1)/2)
//                            , MatrixUtils.createObjectColumnMatrix(OneHalfMeanSquaredError.class,hiddenNodes)
//                            , activatedValuesO);
//                    
//                    //System.out.println("errorO:\n"+ MatrixUtils.toString(errorO,df));
//                    
//                    sigmaO = computePrimeFunctions(activatedValuesO, outputActivators);
//                    //System.out.println("SigmaO:\n"+ MatrixUtils.toString(sigmaO,df));
//                    
//                    sigmaO = MatrixUtils.transposed(sigmaO);
//                    sigmaO = MatrixUtils.hadamardProduct(sigmaO, errorO);
//                    //System.out.println("SigmaO:\n"+ MatrixUtils.toString(sigmaO,df));
//                    
////                    System.out.println("weightsHO:\n"+ MatrixUtils.toString(weightsHO,df));
////                    deltaHO = MatrixUtils.dotProduct(sigmaO, weightsHO);
////                    
////                    System.out.println("DeltaHO:\n"+ MatrixUtils.toString(deltaHO,df));
//                    //System.out.println("activatedValuesH:\n"+ MatrixUtils.toString(activatedValuesH,df));
//                    
//                    deltaHO = MatrixUtils.dotProduct(sigmaO, MatrixUtils.transposed(activatedValuesH));
//                    //System.out.println("DeltaHO:\n"+ MatrixUtils.toString(deltaHO,df));
//                    
//                    errorH = MatrixUtils.dotProduct(MatrixUtils.transposed(sigmaO), weightsHO);
//                    //System.out.println("errorH:\n"+ MatrixUtils.toString(errorH,df));
//                    
//                    sigmaH = computePrimeFunctions(activatedValuesH, hiddenActivators);
//                    //System.out.println("SigmaH:\n"+ MatrixUtils.toString(sigmaH,df));
//                    
//                    sigmaH = MatrixUtils.transposed(sigmaH);
//                    sigmaH = MatrixUtils.hadamardProduct(sigmaH, errorH);
//                    //System.out.println("SigmaH:\n"+ MatrixUtils.toString(sigmaH,df));
//                    
////                    System.out.println("weightsHO:\n"+ MatrixUtils.toString(weightsHO,df));
////                    deltaHO = MatrixUtils.dotProduct(sigmaO, weightsHO);
////                    
////                    System.out.println("DeltaHO:\n"+ MatrixUtils.toString(deltaHO,df));
//                    //System.out.println("activatedValuesI:\n"+ MatrixUtils.toString(activatedValuesI,df));
//                    
//                    deltaIH = MatrixUtils.dotProduct(MatrixUtils.transposed(sigmaH), activatedValuesI);
//                    //System.out.println("DeltaIH:\n"+ MatrixUtils.toString(deltaIH,df));
//                    
//                    
//                    //adjust weights
//                    //System.out.println("DeltaHO:\n"+ MatrixUtils.toString(deltaHO,df));
//                    //System.out.println("weightsHO:\n"+ MatrixUtils.toString(weightsHO,df));
//                    weightsHO = MatrixUtils.add(weightsHO, MatrixUtils.scalarProduct(-learningRate, deltaHO));
//                    //System.out.println("weightsHO:\n"+ MatrixUtils.toString(weightsHO,df));
//                    
//                    //System.out.println("DeltaIH:\n"+ MatrixUtils.toString(deltaIH,df));
//                    //System.out.println("weightsIH:\n"+ MatrixUtils.toString(weightsIH,df));
//                    weightsIH = MatrixUtils.add(weightsIH, MatrixUtils.scalarProduct(-learningRate, deltaIH));
//                    //System.out.println("weightsIH:\n"+ MatrixUtils.toString(weightsIH,df));
//                    dataset.addValue( (Math.sin(inputs[input][0])+1)/2 , "Math.sin(angle)" , df.format(inputs[input][0]));
//                    dataset.addValue( activatedValuesO[0][0] , "NNOut" , df.format(inputs[input][0]));
//                }
//                learningRate = learningRate/( 1 + (double)epoch/20000);
//                chart.setTitle("Neural Network BenchMark/"+epoch+"/"+previousMSE);
//                chart.swapChart(dataset);
//            }
//            
//
//            
//            
//            //System.out.println("inputs:\n"+ MatrixUtils.toString(inputs,df));
//            //System.out.println("inputs:\n"+ MatrixUtils.toString(inputs,df));
            
        } catch (Exception ex) {
            Logger.getLogger(MatrixUtilsTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
