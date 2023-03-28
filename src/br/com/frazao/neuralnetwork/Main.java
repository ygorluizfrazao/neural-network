/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import br.com.frazao.neuralnetwork.activationfunctions.SigmoidActivator;
import br.com.frazao.neuralnetwork.errorfunctions.OneHalfMeanSquaredError;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author ygorl
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MainFrame.main(args);
        
        
//        try{
//            new Controle().evaluateCobranca(new File("evaluate.csv"), new File("NNF1652047103197.json"));
//        }catch(IOException ex)
//        {
//            ex.printStackTrace();
//        }
        
        
        LineChart_AWT chart = new LineChart_AWT("Neural Network BenchMark","Angle", "Sine(Angle)");

        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        //Controle c = new Controle();
        
        NeuralNetwork bpnn = new NeuralNetwork(0.02);
        int sizes[]={1,20,1};
        bpnn.createNetwork(new SigmoidActivator(), sizes);

        NeuralNetwork rpnn = new NeuralNetwork(0.2);
        rpnn.createNetwork(new SigmoidActivator(), sizes);
        
        int iterations=0;
        OneHalfMeanSquaredError omsebp = new OneHalfMeanSquaredError();
        bpnn.setErrorFunction(new OneHalfMeanSquaredError());
        OneHalfMeanSquaredError omserp = new OneHalfMeanSquaredError();
        ArrayList<Point2D.Double> trainingSet = new ArrayList<>();
        for(double i = 0; i<=Math.PI*2; i+=Math.PI/180)
        {
            trainingSet.add(new Point2D.Double(i, (Math.sin(i)+1)/2));
        }
        Collections.sort(trainingSet, new Comparator<Point2D.Double>() {
            @Override
            public int compare(Point2D.Double o1, Point2D.Double o2) {
                if(o1.x>o2.x)
                    return 1;
                else if(o1.x<o2.x)
                    return -1;
                else
                    return 0;
            }
        });
        //Collections.shuffle(trainingSet, new Random(17111987));
        while(true)
        {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            iterations++;
            double outputBP=0.0;
            double outputRP=0.0;
            omsebp.reset();
            omserp.reset();
            for (Point2D.Double set : trainingSet) {
                outputBP = bpnn.feedForward(set.x).get(0);
                omsebp.acumulateError(set.y, outputBP);
                bpnn.getErrorFunction().acumulateError(set.y, outputBP);
                bpnn.calculateGradientsWithErrorFuncion();
                bpnn.updateBackPropagationWeights(0.01, 0.001);
                outputRP = rpnn.feedForward(set.x).get(0);
                omserp.acumulateError(set.y, outputRP);
                rpnn.calculateGradients(outputRP-set.y);
                dataset.addValue( set.y , "Math.sin(angle)" , df.format(set.x));
                dataset.addValue( outputBP , "BPNNout(angle)" , df.format(set.x));
                dataset.addValue( outputRP , "RPNNout(angle)" , df.format(set.x));
            }
            rpnn.updateRPROPWeights(0.5, 1.2, 0.01, 0.00001);
            if(iterations==1 || iterations%5==0)
            {
                System.out.println("EPOCH:"+iterations);
                System.out.println("RMS(BP): "+omsebp.getMeanError());
                System.out.println("RMS(RP): "+omserp.getMeanError());
//                for (Point2D.Double set : trainingSet) {
//                    output = nn.feedForward(set.x).get(0);
//                    System.out.println("sin("+set.x*180/Math.PI+"): "+df.format(Math.sin(set.x))+"->NNout: "+df.format(output));
//                }
                chart.setTitle("Neural Network BenchMark (Epoch:"+iterations+")");
                chart.swapChart(dataset);
            }
        }
//        while(true)
//        {
//            ArrayList<ForexHistoryEntry> trainingSet= new ArrayList<>();
//            while(trainingSet.size()!=11)
//                trainingSet = c.getDataSet(con);
//            int expectedRresult;
//            if(trainingSet.get(0).getBidOpen()-trainingSet.get(1).getBidOpen()<=0)
//                expectedRresult = 0;
//            else
//                expectedRresult = 1;
//            trainingSet.remove(0);
//            double[] set = new double[10];
//            for (int i =0;i<trainingSet.size();i++) {
//                set[i] = trainingSet.get(i).getBidOpen();
//            }
//            iterations++;
//            double result=nn.feedForward(set).get(0);
//            double error1 = expectedRresult-result;
//            double error2;
//            if(result>=0.5 && expectedRresult==1)
//            {
//                correctnes++;
//                error2=0;
//            }
//            else if(result<0.5 && expectedRresult==0)
//            {
//                correctnes++;
//                error2 =0;
//            }else
//            {
//                error2= expectedRresult-result;
//            }
//            nn.backPropagateError(error1);
//            rms +=error2*error2;
//            if((correctnes/iterations*100)>90) exit = true;
//            if(exit)
//            {
//                rms=rms/((iterations/1000-Math.ceil(iterations/1000))*1000);
//                System.out.println("EPOCH:"+iterations);
//                System.out.println("RMS: "+rms);
//                System.out.println("TAXA DE SUCESSO: "+df.format(correctnes/1000*100)+"%");
//                System.out.println("LAST RESULT: "+ result+" -> ESPERADO: "+expectedRresult);
//                correctnes = 0;
//                rms = 0;
//                break;
//            }
//            if(iterations>0 && iterations%1000==0)
//            {
//                try {
//                    con.close();
//                    con=null;
//                } catch (SQLException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                con = c.conectarDB(Controle.DB_DRIVER, Controle.DB_URL, Controle.DB_USER, Controle.DB_PASS);
//                rms=rms/1000;
//                System.out.println("EPOCH:"+iterations);
//                System.out.println("RMS: "+rms);
//                System.out.println("TAXA DE SUCESSO: "+df.format(correctnes/1000*100)+"%");
//                System.out.println("LAST RESULT: "+ result+" -> ESPERADO: "+expectedRresult);
//                correctnes = 0;
//                rms = 0;
//            }
//        }
    }
    
}
