/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ygor
 */
public class CobrancaTrainingSetManager{
    
    
    private List<List<CobrancaPOJO>> bufferTrainingSets;
    private int bufferSize = 4;
    private static CobrancaTrainingSetManager instance;
    
    private CobrancaTrainingSetManager()
    {
        bufferTrainingSets = Collections.synchronizedList(new ArrayList<>());
    }
    
    public static CobrancaTrainingSetManager getInstance()
    {
        if(instance==null)
            instance = new CobrancaTrainingSetManager();
        return instance;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public List<CobrancaPOJO> asyncLoad(CobrancaTrainingSetLoadedListener cobrancaTrainingSetLoadedListener) throws SQLException
    {
        List<CobrancaPOJO> currTrainingSet=null;
        if(bufferTrainingSets.size()>=1){
            currTrainingSet = bufferTrainingSets.remove(0);
        }else{
            currTrainingSet = new Controle().nextCobrancaDataSet();
        }
        int missingSets = bufferSize - bufferTrainingSets.size();
        while(missingSets>0)
        {
            Thread t = new Thread(() -> {
                try {
                    List<CobrancaPOJO> buff = new Controle().nextCobrancaDataSet();
                    bufferTrainingSets.add(buff);
                    if(cobrancaTrainingSetLoadedListener!=null)
                        cobrancaTrainingSetLoadedListener.newTrainingSet(buff);
                } catch (SQLException ex) {
                    Logger.getLogger(CobrancaTrainingSetManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            t.start();
        }
        return currTrainingSet;
    }
    
    public List<CobrancaPOJO> syncLoad() throws SQLException
    {
        return new Controle().nextCobrancaDataSet();
    }
    
    
}
