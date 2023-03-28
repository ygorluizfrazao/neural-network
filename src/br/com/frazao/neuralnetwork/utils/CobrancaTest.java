/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.utils;

import br.com.frazo.neuralnets.utils.IndexOfMaxFunction;
import br.com.frazo.neuralnets.utils.NNFeeder;
import br.com.frazo.neuralnets.mathfunctions.LeakyReLUFunction;
import br.com.frazo.neuralnets.mathfunctions.DistanceToGoalLearningRateAdjuster;
import br.com.frazo.neuralnets.utils.MatrixUtils;
import br.com.frazo.neuralnets.mathfunctions.OneHalfMeanSquaredError;
import br.com.frazo.neuralnets.mathfunctions.SigmoidFunction;
import br.com.frazo.neuralnets.StochasticArtificialNeuralNetwork;
import br.com.frazao.neuralnetwork.CobrancaPOJO;
import br.com.frazao.neuralnetwork.LineChart_AWT;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Ygor
 */
public class CobrancaTest {
    public static void main(String[] args) {
        try {
            StochasticArtificialNeuralNetwork nn = new StochasticArtificialNeuralNetwork(0.1, 0.8, 8, SigmoidFunction.class, 1, LeakyReLUFunction.class, 64,32,16,8,4);
            nn.setGradientClipNorm(3.5);
            System.out.println("Init Feeder");
            NNFeeder nNCobrancaFeeder = new CobrancaFeeder();
            NNFeeder testFeeder = new CobrancaFeeder();
            System.out.println("End Init Feeder");
            
            LineChart_AWT chart = new LineChart_AWT("Neural Network BenchMark","Total MSE", "Accuracy");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            long i = 0;
            int goodMseTimes=0;
            long MSEPlots = 0;
            double MSE;
            double totalMSE =0;
            DistanceToGoalLearningRateAdjuster lrAdjuster = new DistanceToGoalLearningRateAdjuster(0.0001,0.1,0.001,0.000001);
            nn.saveNNToFile("begin.ann");
            while(true){
                //System.out.println("i: "+i);
                nn.feedForward(nNCobrancaFeeder);
                //System.out.print("nnOut: "+ MatrixUtils.toString(nnOut));
                MSE = nn.adjust(nNCobrancaFeeder, new OneHalfMeanSquaredError());
                totalMSE+=MSE;
                nn.setLearningRate(lrAdjuster.compute(MSE));
                //nn.setMomentum(nn.getLearningRate()/10);
                //System.out.println("i: "+i+"/MSE: "+MSE+"/LR: "+nn.getLearningRate()+"/MOM: "+nn.getMomentum());
                if((MSE<=0.0001 && i>=1000))
                {
                    if(goodMseTimes>100){
                        break;
                    }
                    goodMseTimes++;
                }else
                    goodMseTimes = 0;
                if(i>=100000000)
                    break;
                //System.out.println("MSE: "+ MSE);
                if(i%nNCobrancaFeeder.getSize() == 0)
                {
                    double certos =0;
                    double errados = 0;
                    for(int l = 0; l < testFeeder.getSize(); l++)
                    {
                        double[] nextInput = testFeeder.getNextInput();
                        double[][] netReturn = nn.feedForward(nextInput);
                        double[] resp = new double[netReturn.length];
                        for(int k = 0; k<netReturn.length; k++)
                            resp[k] = netReturn[k][0];
                        //int index = (int) new IndexOfMaxFunction().compute(resp);
                        double[] target = testFeeder.getExpectedResult();
                        if(Math.sqrt(Math.pow(target[0]-resp[0],2))<=0.05)
                        {
                            certos++;
                        }else{
                            errados++;
                        }
                    }
                    System.out.println("Certos: "+ certos+"; Errados: "+errados+"Acr: "+certos*100/(certos+errados)+"%");
                    dataset.addValue( totalMSE/(nNCobrancaFeeder.getSize()+1.0D) , "Total MSE" , ""+(MSEPlots));
                    dataset.addValue( (certos+0.0d)/testFeeder.getSize() , "Accuracy" , ""+(MSEPlots));
                    chart.setTitle("Epoch:"+(i+0.0d)/nNCobrancaFeeder.getSize()+"\n/LR:"+nn.getLearningRate()+"\n/TotalMSE:"+totalMSE/(nNCobrancaFeeder.getSize()+1.0D));
                    chart.swapChart(dataset);
                    MSEPlots++;
                    totalMSE=0;
                }
                i++;
            }
            chart.setTitle("FINISHED");
            nn.saveNNToFile("end_TOTAL_PROB.ann");
            RandomAccessFile rf = new RandomAccessFile("Result1653582508153.csv", "rw");
            File fOut = new File("Result"+new Date().getTime()+".csv");
            OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(fOut), "UTF-8");
            rf.seek(0);
            String headerLine = rf.readLine()+";NNOutput_TOT_PROB\n";
            fos.write(headerLine);
            String line;
            while((line = rf.readLine())!=null)
            {

                String fields[] = line.split(";");
                CobrancaPOJO cobrancaPOJO = new CobrancaPOJO();
                cobrancaPOJO.setDocValid(Double.parseDouble(fields[1]));
                cobrancaPOJO.setPerfil(Double.parseDouble(fields[2]));
                cobrancaPOJO.setCategoria(Double.parseDouble(fields[3]));
                cobrancaPOJO.setAbastecimento(Double.parseDouble(fields[4]));
                cobrancaPOJO.setLocalidade(Double.parseDouble(fields[5]));
                cobrancaPOJO.setSitAgua(Double.parseDouble(fields[6]));
                cobrancaPOJO.setSitEsg(Double.parseDouble(fields[7]));
                cobrancaPOJO.setHd(Double.parseDouble(fields[8]));
                cobrancaPOJO.setEnviadas(Double.parseDouble(fields[9]));
                double[][] nnOut = nn.feedForward(nNCobrancaFeeder);
                int index = (int) new IndexOfMaxFunction().compute(MatrixUtils.transposed(nnOut)[0]);
                if(index==0)
                    line +=";"+1+"\n";
                else
                    line +=";"+0+"\n";
                fos.write(line);
            }
            fos.close();
            rf.close();
        } catch (Exception ex) {
            Logger.getLogger(NNTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
