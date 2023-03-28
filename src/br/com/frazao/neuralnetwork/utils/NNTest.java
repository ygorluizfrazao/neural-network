/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.utils;

import br.com.frazo.neuralnets.utils.NNFeeder;
import br.com.frazo.neuralnets.mathfunctions.DistanceToGoalLearningRateAdjuster;
import br.com.frazo.neuralnets.mathfunctions.ELUFunction;
import br.com.frazo.neuralnets.mathfunctions.OneHalfMeanSquaredError;
import br.com.frazo.neuralnets.StochasticArtificialNeuralNetwork;
import br.com.frazao.neuralnetwork.LineChart_AWT;
import br.com.frazo.neuralnets.mathfunctions.LeakyReLUFunction;
import br.com.frazo.neuralnets.mathfunctions.LinearFunction;
import br.com.frazo.neuralnets.mathfunctions.SigmoidFunction;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Ygor
 */
public class NNTest {
    
    
    public static void main(String[] args) {
        try {
            StochasticArtificialNeuralNetwork nn = new StochasticArtificialNeuralNetwork(0.01, 0.5, 1, LinearFunction.class, 1, LeakyReLUFunction.class, 24,12,4);
            nn.setGradientClipNorm(3.5);
            NNFeeder nNFeeder = new NNFeeder() {
                
                private class Pair<X extends Number,Y extends Number> implements Comparable<Pair>{
                    
                    public X x;
                    public Y y;

                    public Pair(X x, Y y) {
                        this.x = x;
                        this.y = y;
                    }

                    @Override
                    public int compareTo(Pair o) {
                        if(this.x.doubleValue()>o.x.doubleValue())
                            return 1;
                        if(this.x.doubleValue()==o.x.doubleValue())
                            return 0;
                        else
                            return -1;
                    }
                }
                
                final ArrayList<Pair<Double,Double>> dataSet = new ArrayList<>(500);
                int currentPosition = -1;
                
                @Override
                public double[] getNextInput() {
                    currentPosition++;
                    if(currentPosition>=500)
                            currentPosition=0;
                    while(dataSet.size()<500)
                    {
                        double x = Math.PI*2*Math.random();
                        dataSet.add(new Pair(x, (Math.sin(x)+1)/2));
                    }
                    Collections.sort(dataSet);
                    return (new double[] {dataSet.get(currentPosition).x});
                }

                @Override
                public double[] getExpectedResult() {
                    return (new double[] {dataSet.get(currentPosition).y});
                }

                @Override
                public int getSize() {
                    return 500;
                }

                @Override
                public int getCurrentTupla() {
                    return currentPosition;
                }
            };
            
            LineChart_AWT chart = new LineChart_AWT("Neural Network BenchMark","Angle", "Sine(Angle)");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            long i = 0;
            int goodMseTimes=0;
            long MSEPlots = 0;
            double MSE = 0.5;
            double totalMSE =0;
            DistanceToGoalLearningRateAdjuster lrAdjuster = new DistanceToGoalLearningRateAdjuster(0.0001,0.01,0.001,0.000001);
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
                    if(goodMseTimes>1000){
                        chart.setTitle("FINISHED");
                        nn.saveNNToFile("end.ann");
                        break;
                    }
                    goodMseTimes++;
                }else
                    goodMseTimes = 0;
                //System.out.println("MSE: "+ MSE);
                if(i%1000 == 0)
                {
                    DecimalFormat df = new DecimalFormat("#,##0.0");
                    ArrayList<Double> xs = new ArrayList<>();
                    for(double s = 0 ; s<=Math.PI*2; s+=Math.PI/360)
                        xs.add(s);
                    Collections.sort(xs);
                    ArrayList<Double> ys = new ArrayList<>();
                    for(int s = 0; s<xs.size(); s++)
                        ys.add((Math.sin(xs.get(s))+1)/2);
                    for(int k = 0; k<xs.size(); k++){
                        dataset.addValue( ys.get(k) , "(sin(angle)+1)/2" , df.format(xs.get(k)));
                        dataset.addValue( nn.feedForward(xs.get(k))[0][0], "NNOut" , df.format(xs.get(k)));
                    }
                    //dataset.addValue( totalMSE/(i+1.0D) , "MSE" , ""+(MSEPlots%100));
                    chart.setTitle("Epoch:"+(i+0.0d)/nNFeeder.getSize()+"\n/LR:"+nn.getLearningRate()+"\n/MSE:"+MSE);
                    chart.swapChart(dataset);
                    MSEPlots++;
                }
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(NNTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
