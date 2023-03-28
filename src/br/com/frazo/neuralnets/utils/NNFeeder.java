/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.utils;

/**
 *
 * @author Ygor
 */
public interface NNFeeder {
    
    public double[] getNextInput();
    public double[] getExpectedResult();
    public int getSize();
    public int getCurrentTupla();
    
}
