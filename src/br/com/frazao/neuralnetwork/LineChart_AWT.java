/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class LineChart_AWT extends ApplicationFrame {

    private ChartPanel chartPanel;
    JFreeChart lineChart;
    
   public LineChart_AWT( String applicationTitle, String xLabel, String yLabel) {
        super(applicationTitle);

        lineChart = ChartFactory.createLineChart(
        xLabel+" - "+yLabel,
        xLabel,yLabel,
        null,
        PlotOrientation.VERTICAL,
        true,true,false);
        lineChart.getCategoryPlot().getRangeAxis().setRange(0, 1);
        chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      
      setContentPane( chartPanel );
   }

   public void swapChart( DefaultCategoryDataset dataset)
   {
         lineChart.getCategoryPlot().setDataset(dataset);
   }
   
//   private DefaultCategoryDataset createDataset( ) {
//      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
//      dataset.addValue( 15 , "schools" , "1970" );
//      dataset.addValue( 30 , "schools" , "1980" );
//      dataset.addValue( 60 , "schools" ,  "1990" );
//      dataset.addValue( 120 , "schools" , "2000" );
//      dataset.addValue( 240 , "schools" , "2010" );
//      dataset.addValue( 300 , "schools" , "2014" );
//      return dataset;
//   }
   
//    public static void main( String[ ] args ) {
//      LineChart_AWT chart = new LineChart_AWT(
//         "School Vs Years" ,
//         "Numer of Schools vs years");
//
//      chart.pack( );
//      RefineryUtilities.centerFrameOnScreen( chart );
//      chart.setVisible( true );
//   }
}
