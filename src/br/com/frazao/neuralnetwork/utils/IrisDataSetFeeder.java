/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazao.neuralnetwork.utils;

import br.com.frazo.neuralnets.utils.KFoldCrossValidationNNfeeder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ygor
 */
public class IrisDataSetFeeder extends KFoldCrossValidationNNfeeder{

    private ArrayList<IrisFlowerPOJO> dataArrayList;
    private final Map<Integer, Integer> kFoldPositionMap;
    private int currentPosition = -1;
    private int currentTestPosition = -1;
    
    public IrisDataSetFeeder(String filename, int kFolds) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        RandomAccessFile rf = new RandomAccessFile(filename, "rw");
        rf.seek(0);
        rf.readLine();
        String line;
        dataArrayList = new ArrayList<>();
        while((line = rf.readLine())!=null)
        {
            String fields[] = line.split(";");
            IrisFlowerPOJO flower = new IrisFlowerPOJO();
            flower.setId(Integer.parseInt(fields[0]));
            flower.setSepalLen(Double.parseDouble(fields[1]));
            flower.setSepalWid(Double.parseDouble(fields[2]));
            flower.setPetalLen(Double.parseDouble(fields[3]));
            flower.setPetalWid(Double.parseDouble(fields[4]));
            flower.setSpecies(fields[5]);
            flower.setSpeciesClass(Double.parseDouble(fields[6]));
            dataArrayList.add(flower);
        }
        rf.close();
        Collections.shuffle(dataArrayList);
        
        this.kFolds = kFolds;
        this.kFoldPositionMap = new HashMap<>();
        if(this.kFolds>0)
        {
            this.testSetSize = dataArrayList.size()/this.kFolds;
            for( int i = 0 ; i < kFolds; i++)
                this.kFoldPositionMap.put(i, i*this.testSetSize);
            currentKFold++;
        }
    }
    
    @Override
    public double[] getNextInput() {
        currentPosition++;
        if(currentPosition>=dataArrayList.size())
            currentPosition=0;
        if(currentPosition>= kFoldPositionMap.getOrDefault(currentKFold, -1) && 
                currentPosition<kFoldPositionMap.getOrDefault(currentKFold+1, -1))
            currentPosition = kFoldPositionMap.getOrDefault(currentKFold+1, -1);
        IrisFlowerPOJO flower = dataArrayList.get(currentPosition);
        double[] ret = new double[4];
        ret[0] = flower.getSepalLen();
        ret[1] = flower.getSepalWid();
        ret[2] = flower.getPetalLen();
        ret[3] = flower.getPetalWid();
        return ret;
    }

    @Override
    public double[] getExpectedResult() {
        double[] ret = new double[3];
        double sClass = dataArrayList.get(currentPosition).getSpeciesClass();
        if(sClass == 0.3){
            ret[0] = 1;
            ret[1] = 0;
            ret[2] = 0;
        }else if( sClass == 0.6){
            ret[0] = 0;
            ret[1] = 1;
            ret[2] = 0;
        }else if ( sClass == 0.9){
            ret[0] = 0;
            ret[1] = 0;
            ret[2] = 1;
        }
        return ret;
    }

    @Override
    public int getSize() {
        return dataArrayList.size();
    }

    @Override
    public int getCurrentTupla() {
        return currentPosition;
    }

    public ArrayList<IrisFlowerPOJO> getDataArrayList() {
        return dataArrayList;
    }

    public void setDataArrayList(ArrayList<IrisFlowerPOJO> dataArrayList) {
        this.dataArrayList = dataArrayList;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    public double[] nextTestInput() {
        currentTestPosition++;
        if(currentTestPosition<kFoldPositionMap.getOrDefault(currentKFold, -1)
                || currentTestPosition>=kFoldPositionMap.getOrDefault(currentKFold+1, -1))
            currentTestPosition = kFoldPositionMap.getOrDefault(currentKFold, -1);
        IrisFlowerPOJO flower = dataArrayList.get(currentTestPosition);
        double[] ret = new double[4];
        ret[0] = flower.getSepalLen();
        ret[1] = flower.getSepalWid();
        ret[2] = flower.getPetalLen();
        ret[3] = flower.getPetalWid();
        return ret;
    }

    @Override
    public double[] getTestExpectedResult() {
        double[] ret = new double[3];
        double sClass = dataArrayList.get(currentTestPosition).getSpeciesClass();
        if(sClass == 0.3){
            ret[0] = 1;
            ret[1] = 0;
            ret[2] = 0;
        }else if( sClass == 0.6){
            ret[0] = 0;
            ret[1] = 1;
            ret[2] = 0;
        }else if ( sClass == 0.9){
            ret[0] = 0;
            ret[1] = 0;
            ret[2] = 1;
        }
        return ret;
    }

    @Override
    public int getTestCurrentTupla() {
        return currentTestPosition;
    }
    
}


