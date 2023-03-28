/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.utils;

import br.com.frazo.neuralnets.utils.KFoldCrossValidationNNfeeder;
import br.com.frazo.neuralnets.utils.IndexOfMaxFunction;
import br.com.frazo.neuralnets.mathfunctions.LeakyReLUFunction;
import br.com.frazo.neuralnets.mathfunctions.DistanceToGoalLearningRateAdjuster;
import br.com.frazo.neuralnets.utils.MatrixUtils;
import br.com.frazo.neuralnets.mathfunctions.OneHalfMeanSquaredError;
import br.com.frazo.neuralnets.mathfunctions.SigmoidFunction;
import br.com.frazo.neuralnets.StochasticArtificialNeuralNetwork;
import br.com.frazao.neuralnetwork.LineChart_AWT;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Ygor
 */
public class IrisTest {
    
    
 public static void main(String[] args) {
        try {
            StochasticArtificialNeuralNetwork nn = new StochasticArtificialNeuralNetwork(0.1, 0.9, 4, SigmoidFunction.class, 3, LeakyReLUFunction.class, 8);
            nn.setGradientClipNorm(1);
            KFoldCrossValidationNNfeeder nNFeeder = new IrisDataSetFeeder("iris_2.csv",5);
            
            LineChart_AWT chart = new LineChart_AWT("Neural Network BenchMark","Iris dataset, Total MSE", "Accuracy");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            
            
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            long i = 0;
            int goodMseTimes=0;
            long MSEPlots = 0;
            double MSE = 0.5;
            double totalMSE =0;
            DistanceToGoalLearningRateAdjuster lrAdjuster = new DistanceToGoalLearningRateAdjuster(0.0005,0.1,0.001,0.000001);
            nn.saveNNToFile("begin.ann");
            while(true){
                //System.out.println("i: "+i);
                nn.feedForward(nNFeeder);
                //System.out.print("nnOut: "+ MatrixUtils.toString(nnOut));
                MSE = nn.adjust(nNFeeder, new OneHalfMeanSquaredError());
                totalMSE+=MSE;
                nn.setLearningRate(lrAdjuster.compute(MSE));
                //nn.setMomentum(nn.getLearningRate()/10);
                if(MSE<=0.0001 && i>=1000)
                {
                    if(goodMseTimes>100){
                        chart.setTitle("FINISHED");
                        nn.saveNNToFile("end.ann");
                        break;
                    }
                    goodMseTimes++;
                }else
                    goodMseTimes = 0;
                if(i%(nNFeeder.getSize()-nNFeeder.getTestSetSize()) == 0)
                {
                    int certos = 0;
                    int errados = 0;
                    ArrayList<Double> testMSE = new ArrayList<>();
                    for(int l = 0; l < nNFeeder.getTestSetSize(); l++)
                    {
                        double[] nextInput = nNFeeder.nextTestInput();
                        double[][] netReturn = nn.feedForward(nextInput);
                        int index = (int) new IndexOfMaxFunction().compute(MatrixUtils.transposed(netReturn)[0]);
                        double[] resp = new double[netReturn.length];
                        resp[index] = 1;
                        double mmmse = 0;
                        for(int e = 0 ; e < netReturn.length; e++)
                            mmmse+=(new OneHalfMeanSquaredError().compute(netReturn[e][0],resp[e]));
                        testMSE.add(mmmse/netReturn.length);
                        if(Arrays.equals(resp, nNFeeder.getTestExpectedResult()))
                            certos++;
                        else
                            errados++;
                    }
                    System.out.println("I> "+i+"; CERTOS: "+certos+"; ERRADOS: "+errados+"; RATIO: "+ (certos*100.0d)/nNFeeder.getTestSetSize()+"/k: "+nNFeeder.getCurrentKFold());
                    System.out.println(testMSE.stream().collect(Collectors.summarizingDouble(d -> d)));
                    dataset.addValue( totalMSE/(nNFeeder.getSize()+1.0D) , "Total MSE" , ""+(MSEPlots));
                    dataset.addValue( testMSE.stream().collect(Collectors.averagingDouble(d -> d)) , "Test MSE" , ""+(MSEPlots));
                    dataset.addValue( (certos+0.0d)/nNFeeder.getTestSetSize() , "Accuracy" , ""+(MSEPlots));
                    chart.setTitle("Epoch:"+(i+0.0d)/(nNFeeder.getSize()-nNFeeder.getTestSetSize())+"\n/LR:"+nn.getLearningRate()+"\n/TotalMSE:"+totalMSE/((nNFeeder.getSize()-nNFeeder.getTestSetSize())+1.0D));
                    chart.swapChart(dataset);
                    MSEPlots++;
                    totalMSE = 0;
                    if(errados==0 && i>=((nNFeeder.getSize()-nNFeeder.getTestSetSize())*1000))
                    {
                        chart.setTitle("Finished - "+"Epoch:"+(i+0.0d)/(nNFeeder.getSize()-nNFeeder.getTestSetSize()));
                        return;
                    }
                    nNFeeder.nextKFoldTestSet();
                }
                //System.out.println("MSE: "+ MSE);
                if(i%10000 == 0)
                {
                    //DecimalFormat df = new DecimalFormat("#,##0.0");
//                    ArrayList<Double> xs = new ArrayList<>();
//                    for(double s = 0 ; s<=Math.PI*2; s+=Math.PI/360)
//                        xs.add(s);
//                    Collections.sort(xs);
//                    ArrayList<Double> ys = new ArrayList<>();
//                    for(int s = 0; s<xs.size(); s++)
//                        ys.add((Math.sin(xs.get(s))+1)/2);
//                    for(int k = 0; k<xs.size(); k++){
//                        dataset.addValue( ys.get(k) , "Math.sin(angle)" , df.format(xs.get(k)));
//                        dataset.addValue( nn.feedForward(xs.get(k))[0][0], "NNOut" , df.format(xs.get(k)));
//                    }
                }
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(NNTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
