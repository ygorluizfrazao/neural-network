/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.frazao.neuralnetwork;

import br.com.frazao.neuralnetwork.activationfunctions.HiperbolicTanActivator;
import java.util.ArrayList;
import br.com.frazao.neuralnetwork.activationfunctions.NeuronActivatorFunction;
import br.com.frazao.neuralnetwork.activationfunctions.SigmoidActivator;
import br.com.frazao.neuralnetwork.errorfunctions.ErrorFunction;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

/**
 *
 * @author ygorl
 */
public class NeuralNetwork implements Serializable{
 
    private ArrayList<NeuralLayer> layers;
    private double bias;
    private ErrorFunction errorFunction;
    

    public NeuralNetwork(ArrayList<NeuralLayer> layers, double bias) {
        this.layers = layers;
        this.bias = bias;
    }

    public NeuralNetwork(double bias) {
        layers = new ArrayList<>();
        this.bias = bias;

    }

    public ErrorFunction getErrorFunction() {
        return errorFunction;
    }

    public void setErrorFunction(ErrorFunction errorFunction) {
        this.errorFunction = errorFunction;
    }
    
    public void addLayer(NeuralLayer nl)
    {
        layers.add(nl);
    }

    public ArrayList<NeuralLayer> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<NeuralLayer> layers) {
        this.layers = layers;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
    
    public void createNetwork(ArrayList<ArrayList<Neuron>> neurons,ArrayList<ArrayList<Double>> weights, NeuronActivatorFunction neuronActivator, double bias)
    {
        this.bias = bias;
        for (ArrayList<Neuron> neuronLayer : neurons) {
            layers.add(new NeuralLayer(null, null, neuronLayer));
        }
        layers.get(0).setIncomingLayer(null);
        layers.get(0).setOutgoingLayer(layers.get(1));
        for (int i=1;i<layers.size()-1;i++) {
            NeuralLayer currentLayer = layers.get(i);
            currentLayer.setIncomingLayer(layers.get(i-1));
            currentLayer.setOutgoingLayer(layers.get(i+1));
        }
        layers.get(layers.size()-1).setIncomingLayer(layers.get(layers.size()-2));
        layers.get(layers.size()-1).setOutgoingLayer(null);
        
        for (int i=1;i<layers.size();i++) {
            NeuralLayer layer = layers.get(i);
            NeuralLayer incomingLayer = layer.getIncomingLayer();
            for (int j=0; j<incomingLayer.getNeurons().size();j++) {
                Neuron leftNeuron =incomingLayer.getNeurons().get(j);
                for (int k=0; k<layer.getNeurons().size();k++) {
                    Neuron rightNeuron = layer.getNeurons().get(k);
                    Synapse s = new Synapse(leftNeuron, rightNeuron, weights.get(i-1).get(j*layer.getNeurons().size()+k));
                    leftNeuron.connectOutgoingNeuron(rightNeuron, s);
                    rightNeuron.connectIncomingNeuron(leftNeuron, s);
                }
            }
        }
    }

    public void createNetwork(NeuronActivatorFunction neuronActivator, double bias,int[] sizes,double[] weights)
    {
        this.bias = bias;
        for (int i = 0; i< sizes.length; i++) {
            ArrayList<Neuron> neuronLayer = new ArrayList<>();
            for(int j = 0; j< sizes[i]; j++)
            {
                neuronLayer.add(new Neuron(1, neuronActivator));
            }
            layers.add(new NeuralLayer(null, null, neuronLayer));
        }
        layers.get(0).setIncomingLayer(null);
        layers.get(0).setOutgoingLayer(layers.get(1));
        for (int i=1;i<layers.size()-1;i++) {
            NeuralLayer currentLayer = layers.get(i);
            currentLayer.setIncomingLayer(layers.get(i-1));
            currentLayer.setOutgoingLayer(layers.get(i+1));
        }
        layers.get(layers.size()-1).setIncomingLayer(layers.get(layers.size()-2));
        layers.get(layers.size()-1).setOutgoingLayer(null);
        
        for (int i=1;i<layers.size();i++) {
            NeuralLayer layer = layers.get(i);
            NeuralLayer incomingLayer = layer.getIncomingLayer();
            for (int j=0; j<incomingLayer.getNeurons().size();j++) {
                Neuron leftNeuron =incomingLayer.getNeurons().get(j);
                for (int k=0; k<layer.getNeurons().size();k++) {
                    Neuron rightNeuron = layer.getNeurons().get(k);
                    Synapse s = new Synapse(leftNeuron, rightNeuron, weights[((i-1)*(layer.getNeurons().size()*incomingLayer.getNeurons().size()))+j*layer.getNeurons().size()+k]);
                    leftNeuron.connectOutgoingNeuron(rightNeuron, s);
                    rightNeuron.connectIncomingNeuron(leftNeuron, s);
                }
            }
        }
    }
    
    public void createNetwork(NeuronActivatorFunction neuronActivator,int[] sizes)
    {
        for (int i = 0; i< sizes.length; i++) {
            ArrayList<Neuron> neuronLayer = new ArrayList<>();
            for(int j = 0; j< sizes[i]; j++)
            {
                neuronLayer.add(new Neuron(1, neuronActivator));
            }
            layers.add(new NeuralLayer(null, null, neuronLayer));
        }
        layers.get(0).setIncomingLayer(null);
        layers.get(0).setOutgoingLayer(layers.get(1));
        for (int i=1;i<layers.size()-1;i++) {
            NeuralLayer currentLayer = layers.get(i);
            currentLayer.setIncomingLayer(layers.get(i-1));
            currentLayer.setOutgoingLayer(layers.get(i+1));
        }
        layers.get(layers.size()-1).setIncomingLayer(layers.get(layers.size()-2));
        layers.get(layers.size()-1).setOutgoingLayer(null);
        
        for (int i=1;i<layers.size();i++) {
            NeuralLayer layer = layers.get(i);
            NeuralLayer incomingLayer = layer.getIncomingLayer();
            for (int j=0; j<incomingLayer.getNeurons().size();j++) {
                Neuron leftNeuron =incomingLayer.getNeurons().get(j);
                for (int k=0; k<layer.getNeurons().size();k++) {
                    Neuron rightNeuron = layer.getNeurons().get(k);
                    Synapse s = new Synapse(leftNeuron, rightNeuron, Math.random()-0.5);
                    leftNeuron.connectOutgoingNeuron(rightNeuron, s);
                    rightNeuron.connectIncomingNeuron(leftNeuron, s);
                }
            }
        }
    }
    
    public void createNetwork(NeuronActivatorFunction[] neuronActivator,int[] sizes)
    {
        int n = 0;
        for (int i = 0; i< sizes.length; i++) {
            ArrayList<Neuron> neuronLayer = new ArrayList<>();
            for(int j = 0; j< sizes[i]; j++)
            {
                neuronLayer.add(new Neuron(1, neuronActivator[n]));
                n++;
            }
            layers.add(new NeuralLayer(null, null, neuronLayer));
        }
        layers.get(0).setIncomingLayer(null);
        layers.get(0).setOutgoingLayer(layers.get(1));
        for (int i=1;i<layers.size()-1;i++) {
            NeuralLayer currentLayer = layers.get(i);
            currentLayer.setIncomingLayer(layers.get(i-1));
            currentLayer.setOutgoingLayer(layers.get(i+1));
        }
        layers.get(layers.size()-1).setIncomingLayer(layers.get(layers.size()-2));
        layers.get(layers.size()-1).setOutgoingLayer(null);
        
        for (int i=1;i<layers.size();i++) {
            NeuralLayer layer = layers.get(i);
            NeuralLayer incomingLayer = layer.getIncomingLayer();
            for (int j=0; j<incomingLayer.getNeurons().size();j++) {
                Neuron leftNeuron =incomingLayer.getNeurons().get(j);
                for (int k=0; k<layer.getNeurons().size();k++) {
                    Neuron rightNeuron = layer.getNeurons().get(k);
                    Synapse s = new Synapse(leftNeuron, rightNeuron, Math.random()-0.5);
                    leftNeuron.connectOutgoingNeuron(rightNeuron, s);
                    rightNeuron.connectIncomingNeuron(leftNeuron, s);
                }
            }
        }
    }
    
    public void createNetwork(NNJson nnJSON)
    {
        this.bias = nnJSON.getBias();
        NeuronActivatorFunction neuronActivatorFunction;
        if(nnJSON.getNeuronActivatorFunction()==NNJson.HYPERBOLIC_TANGENT)
            neuronActivatorFunction = new HiperbolicTanActivator();
        else
            neuronActivatorFunction = new SigmoidActivator();
        for (int i = 0; i< nnJSON.getSizes().length; i++) {
            ArrayList<Neuron> neuronLayer = new ArrayList<>();
            for(int j = 0; j< nnJSON.getSizes()[i]; j++)
            {
                neuronLayer.add(new Neuron(1, neuronActivatorFunction));
            }
            layers.add(new NeuralLayer(null, null, neuronLayer));
        }
        layers.get(0).setIncomingLayer(null);
        layers.get(0).setOutgoingLayer(layers.get(1));
        for (int i=1;i<layers.size()-1;i++) {
            NeuralLayer currentLayer = layers.get(i);
            currentLayer.setIncomingLayer(layers.get(i-1));
            currentLayer.setOutgoingLayer(layers.get(i+1));
        }
        layers.get(layers.size()-1).setIncomingLayer(layers.get(layers.size()-2));
        layers.get(layers.size()-1).setOutgoingLayer(null);
        
        int weightIndex = 0;
        double[] weights = nnJSON.getWeigths();
        for (int i=1;i<layers.size();i++) {
            NeuralLayer layer = layers.get(i);
            NeuralLayer incomingLayer = layer.getIncomingLayer();
            for (int j=0; j<incomingLayer.getNeurons().size();j++) {
                Neuron leftNeuron =incomingLayer.getNeurons().get(j);
                for (int k=0; k<layer.getNeurons().size();k++) {
                    Neuron rightNeuron = layer.getNeurons().get(k);
                    Synapse s = new Synapse(leftNeuron, rightNeuron, weights[weightIndex]);
                    leftNeuron.connectOutgoingNeuron(rightNeuron, s);
                    rightNeuron.connectIncomingNeuron(leftNeuron, s);
                    weightIndex++;
                }
            }
        }
    }
    
    public void createNetwork(File f) throws FileNotFoundException, IOException
    {
        InputStreamReader fr = new InputStreamReader(new FileInputStream(f), "UTF-8");
        NNJson nnJSON = new Gson().fromJson(fr, NNJson.class);
        fr.close();
        createNetwork(nnJSON);
    }
    
    public void saveNNToFile(File f) throws IOException
    {
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        new Gson().toJson(nnJsonizer(), fw);
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
    
    public NNJson nnJsonizer()
    {
        NNJson nNJson = new NNJson();
        
        int[] sizes = new int[layers.size()];
        double[] weights;
        
        for(int i = 0; i<layers.size(); i++)
        {
            NeuralLayer currentLayer = layers.get(i);
            sizes[i]=currentLayer.getNeurons().size();
        }
        
        int synapsesCount = 0;
        
        for(int i = 1; i<sizes.length; i++)
        {
            synapsesCount+=sizes[i]*sizes[i-1];
        }
        weights = new double[synapsesCount];

        //layers
        int weigthIndex = 0;
        for(int i = 0; i<layers.size()-1; i++)
        {
            NeuralLayer currentLayer = layers.get(i);
            for (int j =0 ; j<currentLayer.getNeurons().size(); j++) {
                Neuron currentNeuron = currentLayer.getNeurons().get(j);
                for(int k = 0; k<currentNeuron.getOutgoingConections().size(); k++)
                {
                    weights[weigthIndex]= currentNeuron.getOutgoingConections().get(k).getWeight();
                    weigthIndex++;
                }
            }
        }
        
        nNJson.setSizes(sizes);
        nNJson.setWeigths(weights);
        nNJson.setBias(bias);
        
//        if(activator instanceof SigmoidActivator)
//            nNJson.setNeuronActivatorFunction(NNJson.SIGMOID);
//        else if(activator instanceof HiperbolicTanActivator)
//            nNJson.setNeuronActivatorFunction(NNJson.HYPERBOLIC_TANGENT);
        nNJson.setNeuronActivatorFunction(NNJson.SIGMOID);
        return nNJson;
    }
    
    public ArrayList<Double> feedForward(double... inputs)
    {
        //input layer
        NeuralLayer inputLayer = layers.get(0);
        for (int i =0 ; i<inputLayer.getNeurons().size(); i++) {
            Neuron inputNeuron = inputLayer.getNeurons().get(i);
            inputNeuron.setActivatedValue(inputs[i]);
            inputNeuron.forwardSignal();
        }
        //hidden layers
        for(int i = 1; i<layers.size()-1; i++)
        {
            NeuralLayer currentHiddenLayer = layers.get(i);
            for (int j =0 ; j<currentHiddenLayer.getNeurons().size(); j++) {
                Neuron hiddenNeuron = currentHiddenLayer.getNeurons().get(j);
                hiddenNeuron.sumIncommingSignals(bias);
                hiddenNeuron.activate();
                hiddenNeuron.forwardSignal();
            }
        }
        
        //output layer
        NeuralLayer outputLayer = layers.get(layers.size()-1);
        for (int i =0 ; i<outputLayer.getNeurons().size(); i++) {
            Neuron outputNeuron = outputLayer.getNeurons().get(i);
            outputNeuron.sumIncommingSignals(bias);
            outputNeuron.activate();
        }
        return getOutputs();
    }
    
    public ArrayList<Double> getOutputs()
    {
        ArrayList<Double> outputs=new ArrayList<>();
        NeuralLayer outputLayer = layers.get(layers.size()-1);
        for (int i =0 ; i<outputLayer.getNeurons().size(); i++) {
            Neuron outputNeuron = outputLayer.getNeurons().get(i);
            outputs.add(outputNeuron.getActivatedValue());
        }   
        return outputs;
    }
    
//    private void backPropagateError(double... outputErrors)
//    {
//        //output layer
//        NeuralLayer outputLayer = layers.get(layers.size()-1);
//        for (int i =0 ; i<outputLayer.getNeurons().size(); i++) {
//            Neuron outputNeuron = outputLayer.getNeurons().get(i);
//            outputNeuron.setErrorInOutput(outputErrors[i]);
//            outputNeuron.setSigma(activator.primeFunction(outputNeuron)*outputErrors[i]);
//            outputNeuron.setLastBiasGradient(outputNeuron.getBiasGradient());
//            outputNeuron.setBiasGradient(bias*outputNeuron.getSigma());
//            outputNeuron.setBiasDelta(outputNeuron.getBiasDelta()-learningRate*outputNeuron.getBiasGradient());
//            for (Synapse synapse : outputNeuron.getIncomingConections().keySet()) {
//                synapse.setLastGradient(synapse.getGradient());
//                synapse.setGradient(synapse.getFromNeuron().getActivatedValue()*outputNeuron.getSigma());
//                synapse.setDeltaWeight(synapse.getDeltaWeight()-learningRate*synapse.getFromNeuron().getActivatedValue()*outputNeuron.getSigma());
//            }
//        }
//        //hidden layers
//        for(int i = layers.size()-2; i>0; i--)
//        {
//            NeuralLayer currentHiddenLayer = layers.get(i);
//            for (int j =0 ; j<currentHiddenLayer.getNeurons().size(); j++) {
//                Neuron hiddenNeuron = currentHiddenLayer.getNeurons().get(j);
//                double neuronError = 0;
//                for (Synapse synapse : hiddenNeuron.getOutgoingConections().keySet()) {
//                    neuronError+=synapse.getWeight()*synapse.getToNeuron().getSigma();
//                }
//                hiddenNeuron.setErrorInOutput(neuronError);
//                hiddenNeuron.setSigma(neuronError*activator.primeFunction(hiddenNeuron));
//                hiddenNeuron.setLastBiasGradient(hiddenNeuron.getBiasGradient());
//                hiddenNeuron.setBiasGradient(bias*hiddenNeuron.getSigma());
//                hiddenNeuron.setBiasDelta(hiddenNeuron.getBiasDelta()-learningRate*hiddenNeuron.getBiasGradient());
//                for (Synapse synapse : hiddenNeuron.getIncomingConections().keySet()) {
//                    synapse.setLastGradient(synapse.getGradient());
//                    synapse.setGradient(synapse.getFromNeuron().getActivatedValue()*hiddenNeuron.getSigma());
//                    hiddenNeuron.setBiasGradient(bias*hiddenNeuron.getSigma());
//                    synapse.setDeltaWeight(synapse.getDeltaWeight()-learningRate*synapse.getFromNeuron().getActivatedValue()*hiddenNeuron.getSigma());
//                }
//            }
//        }
//    }
    
//    private void resilientBackPropagateError(double minLearningRate, double maxLearningRate, double deltaMax, double deltaMin,double... outputErrors)
//    {
//        NeuralLayer outputLayer = layers.get(layers.size()-1);
//        for (int i =0 ; i<outputLayer.getNeurons().size(); i++) {
//            Neuron outputNeuron = outputLayer.getNeurons().get(i);
//            outputNeuron.setErrorInOutput(outputErrors[i]);
//            outputNeuron.setSigma(activator.primeFunction(outputNeuron)*outputErrors[i]);
//            outputNeuron.setBiasGradient(outputNeuron.getBiasGradient()+bias*outputNeuron.getSigma());
//            //doRPROPupdatesForBias(outputNeuron, minLearningRate, maxLearningRate, deltaMax, deltaMin);
//            for (Synapse synapse : outputNeuron.getIncomingConections()) {
//                synapse.setGradient(synapse.getGradient()+synapse.getFromNeuron().getActivatedValue()*outputNeuron.getSigma());
//                //doRPROPupdatesForSynapse(synapse, minLearningRate, maxLearningRate, deltaMax, deltaMin);
//            }
//        }
//        //hidden layers
//        for(int i = layers.size()-2; i>0; i--)
//        {
//            NeuralLayer currentHiddenLayer = layers.get(i);
//            for (int j =0 ; j<currentHiddenLayer.getNeurons().size(); j++) {
//                Neuron hiddenNeuron = currentHiddenLayer.getNeurons().get(j);
//                double neuronError = 0;
//                for (Synapse synapse : hiddenNeuron.getOutgoingConections()) {
//                    neuronError+=synapse.getWeight()*synapse.getToNeuron().getSigma();
//                }
//                hiddenNeuron.setErrorInOutput(neuronError);
//                hiddenNeuron.setSigma(neuronError*activator.primeFunction(hiddenNeuron));
//                hiddenNeuron.setBiasGradient(hiddenNeuron.getBiasGradient()+bias*hiddenNeuron.getSigma());
//                //doRPROPupdatesForBias(hiddenNeuron, minLearningRate, maxLearningRate, deltaMax, deltaMin);
//                for (Synapse synapse : hiddenNeuron.getIncomingConections()) {
//                    synapse.setGradient(synapse.getGradient()+synapse.getFromNeuron().getActivatedValue()*hiddenNeuron.getSigma());
//                    //doRPROPupdatesForSynapse(synapse, minLearningRate, maxLearningRate, deltaMax, deltaMin);
//                }
//            }
//        }
//    }
    
    public void calculateGradients(double... outputErrors)
    {
        NeuralLayer outputLayer = layers.get(layers.size()-1);
        for (int i =0 ; i<outputLayer.getNeurons().size(); i++) {
            Neuron outputNeuron = outputLayer.getNeurons().get(i);
            outputNeuron.setErrorInOutput(outputErrors[i]);
            outputNeuron.calculateSigma(outputErrors[i]);
            //outputNeuron.setBiasGradient(outputNeuron.getBiasGradient()+bias*outputNeuron.getSigma());
            //doRPROPupdatesForBias(outputNeuron, minLearningRate, maxLearningRate, deltaMax, deltaMin);
            for (Synapse synapse : outputNeuron.getIncomingConections()) {
                synapse.setGradient(synapse.getGradient()+synapse.getFromNeuron().getActivatedValue()*outputNeuron.getSigma());
                //doRPROPupdatesForSynapse(synapse, minLearningRate, maxLearningRate, deltaMax, deltaMin);
            }
        }
        //hidden layers
        for(int i = layers.size()-2; i>0; i--)
        {
            NeuralLayer currentHiddenLayer = layers.get(i);
            for (int j =0 ; j<currentHiddenLayer.getNeurons().size(); j++) {
                Neuron hiddenNeuron = currentHiddenLayer.getNeurons().get(j);
                double neuronError = 0;
                for (Synapse synapse : hiddenNeuron.getOutgoingConections()) {
                    neuronError+=synapse.getWeight()*synapse.getToNeuron().getSigma();
                }
                hiddenNeuron.setErrorInOutput(neuronError);
                hiddenNeuron.calculateSigma(neuronError);
                hiddenNeuron.setBiasGradient(hiddenNeuron.getBiasGradient()+bias*hiddenNeuron.getSigma());
                //doRPROPupdatesForBias(hiddenNeuron, minLearningRate, maxLearningRate, deltaMax, deltaMin);
                for (Synapse synapse : hiddenNeuron.getIncomingConections()) {
                    synapse.setGradient(synapse.getGradient()+synapse.getFromNeuron().getActivatedValue()*hiddenNeuron.getSigma());
                    //doRPROPupdatesForSynapse(synapse, minLearningRate, maxLearningRate, deltaMax, deltaMin);
                }
            }
        }
    }
    
    public void calculateGradientsWithErrorFuncion()
    {
        NeuralLayer outputLayer = layers.get(layers.size()-1);
        for (int i =0 ; i<outputLayer.getNeurons().size(); i++) {
            Neuron outputNeuron = outputLayer.getNeurons().get(i);
            outputNeuron.setErrorInOutput(errorFunction.primeErrorFunction(i));
            outputNeuron.calculateSigma(outputNeuron.getErrorInOutput());
            //outputNeuron.setBiasGradient(outputNeuron.getBiasGradient()+bias*outputNeuron.getSigma());
            //doRPROPupdatesForBias(outputNeuron, minLearningRate, maxLearningRate, deltaMax, deltaMin);
            for (Synapse synapse : outputNeuron.getIncomingConections()) {
                synapse.setGradient(synapse.getGradient()+synapse.getFromNeuron().getActivatedValue()*outputNeuron.getSigma());
                //doRPROPupdatesForSynapse(synapse, minLearningRate, maxLearningRate, deltaMax, deltaMin);
            }
        }
        //hidden layers
        for(int i = layers.size()-2; i>0; i--)
        {
            NeuralLayer currentHiddenLayer = layers.get(i);
            for (int j =0 ; j<currentHiddenLayer.getNeurons().size(); j++) {
                Neuron hiddenNeuron = currentHiddenLayer.getNeurons().get(j);
                double neuronError = 0;
                for (Synapse synapse : hiddenNeuron.getOutgoingConections()) {
                    neuronError+=synapse.getWeight()*synapse.getToNeuron().getSigma();
                }
                hiddenNeuron.setErrorInOutput(neuronError);
                hiddenNeuron.calculateSigma(neuronError);
                //hiddenNeuron.setBiasGradient(hiddenNeuron.getBiasGradient()+bias*hiddenNeuron.getSigma());
                //doRPROPupdatesForBias(hiddenNeuron, minLearningRate, maxLearningRate, deltaMax, deltaMin);
                for (Synapse synapse : hiddenNeuron.getIncomingConections()) {
                    synapse.setGradient(synapse.getGradient()+synapse.getFromNeuron().getActivatedValue()*hiddenNeuron.getSigma());
                    //doRPROPupdatesForSynapse(synapse, minLearningRate, maxLearningRate, deltaMax, deltaMin);
                }
            }
        }
        errorFunction.reset();
    }
    
    private void doBackPropagateUpdatesForSynapse(Synapse s ,double learningRate, double momentum)
    {
        s.setDeltaWeight(-learningRate*s.getGradient()+momentum*s.getLastDeltaWeight());
        s.setGradient(0);
    }
    
    private void doBackPropagateUpdatesForBias(Neuron n ,double learningRate, double momentum)
    {
        n.setBiasDelta(-learningRate*n.getBiasGradient()+momentum*n.getLastBiasDelta());
        n.setBiasGradient(0);
    }
    
    private void doRPROPupdatesForSynapse(Synapse s ,double minLearningRate, double maxLearningRate, double deltaMax, double deltaMin)
    {
        if(s.getLastGradient()*s.getGradient()>0)
        {
            s.setRPROPDelta(Math.min(s.getLastRPROPDelta()*maxLearningRate, deltaMax));
            s.setLastRPROPDelta(s.getRPROPDelta());
            s.setDeltaWeight((-Math.signum(s.getGradient())*s.getRPROPDelta()));
            s.setLastGradient(s.getGradient());
            s.setGradient(0);
        }else if(s.getLastGradient()*s.getGradient()<0)
        {
            s.setRPROPDelta(Math.max(s.getLastRPROPDelta()*minLearningRate, deltaMin));
            s.setLastRPROPDelta(s.getRPROPDelta());
            s.setLastGradient(0);
            s.setGradient(0);
        }else
        {
            s.setDeltaWeight((-Math.signum(s.getGradient())*s.getRPROPDelta()));
            s.setLastRPROPDelta(s.getRPROPDelta());
            s.setLastGradient(s.getGradient());
            s.setGradient(0);
        }
    }
    
    private void doRPROPupdatesForBias(Neuron n ,double minLearningRate, double maxLearningRate, double deltaMax, double deltaMin)
    {
        if(n.getLastBiasGradient()*n.getBiasGradient()>0)
        {
            n.setBiasRPROPDelta(Math.min(n.getLastBiasRPROPDelta()*maxLearningRate, deltaMax));
            n.setLastBiasRPROPDelta(n.getBiasRPROPDelta());
            n.setBiasDelta((-Math.signum(n.getBiasGradient())*n.getBiasRPROPDelta()));
            n.setLastBiasGradient(n.getBiasGradient());
            n.setBiasGradient(0);
        }else if(n.getLastBiasGradient()*n.getBiasGradient()<0)
        {
            n.setBiasRPROPDelta(Math.max(n.getLastBiasRPROPDelta()*minLearningRate, deltaMin));
            n.setLastBiasRPROPDelta(n.getBiasRPROPDelta());
            n.setLastBiasGradient(0);
            n.setBiasGradient(0);
        }else
        {
            n.setBiasDelta((-Math.signum(n.getBiasGradient())*n.getBiasRPROPDelta()));
            n.setLastBiasRPROPDelta(n.getBiasRPROPDelta());
            n.setLastBiasGradient(n.getBiasGradient());
            n.setBiasGradient(0);
        }
    }
    
    public void updateBackPropagationWeights(double learningRate, double momentum)
    {
        for (NeuralLayer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                //doBackPropagateUpdatesForBias(neuron, learningRate, momentum);
                //neuron.updateBiasWeight();
                for (Synapse synapse : neuron.getOutgoingConections()) {
                    doBackPropagateUpdatesForSynapse(synapse, learningRate, momentum);
                    synapse.updateWeight();
                }
            }
        }
    }
    
    public void updateRPROPWeights(double negativeLearningRate, double positiveLearningRate, double deltaMax, double deltaMin)
    {
        for (NeuralLayer layer : layers) {
            for (Neuron neuron : layer.getNeurons()) {
                doRPROPupdatesForBias(neuron, negativeLearningRate, positiveLearningRate, deltaMax, deltaMin);
                neuron.updateBiasWeight();
                for (Synapse synapse : neuron.getOutgoingConections()) {
                    doRPROPupdatesForSynapse(synapse, negativeLearningRate, positiveLearningRate, deltaMax, deltaMin);
                    synapse.updateWeight();
                }
            }
        }
    }
    
    public ArrayList<Neuron> getOutputNeurons()
    {
        return layers.get(layers.size()-1).getNeurons();
    }
    
    @Override
    public String toString() {
        String s="";
        for (int i=0;i<layers.size();i++) {
            NeuralLayer layer = layers.get(i);
            s+="Layer["+i+"]: \nIncomming:"+ layer.getIncomingLayer()+"\nOutgoing:"+layer.getOutgoingLayer()+"\n";
            for (int j=0; j<layer.getNeurons().size();j++) {
                Neuron n = layer.getNeurons().get(j);
                s+="Incomming:";
                if(!n.getIncomingConections().isEmpty())
                    for (Synapse synapse : n.getIncomingConections()) {
                        s+="["+synapse.getWeight()+"]\n";
                    }
                s+="Outgoing:";
                if(!n.getOutgoingConections().isEmpty())
                    for (Synapse synapse : n.getOutgoingConections()) {
                        s+="["+synapse.getWeight()+"]\n";
                    }
                s+="\n";
            }
        }
        return s;
    }

    public NeuralNetwork() {
        layers = new ArrayList<>();
    }
    
    
}
