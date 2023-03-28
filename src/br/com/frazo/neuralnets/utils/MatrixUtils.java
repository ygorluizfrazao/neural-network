/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.frazo.neuralnets.utils;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 *
 * @author Ygor
 */
public class MatrixUtils {
    
    public static double[][] dotProduct(double[][] A, double[][] B) throws Exception
    {
        
        if(A==null || B==null)
            throw new Exception("A and B params must be non null");
        if(A[0].length!=B.length)
            throw new Exception("For matrix multiplication, the number of columns in the first matrix must be equal to the number of rows in the second matrix.\n"
                    +"A:"+ toString(A)+" . B:"+toString(B));
        
        double C[][] = new double[A.length][B[0].length];
        
        for(int ai = 0 ; ai < A.length; ai++){
            for(int bj = 0; bj<B[0].length; bj++)
                for( int aj = 0; aj<A[0].length; aj++){
                    C[ai][bj] += A[ai][aj]*B[aj][bj];
                }
        }
        return C;
    }
    
    public static void setValues(double[][] A, double[][] B) throws Exception
    {
        if(A==null || B==null)
            throw new Exception("A and B params must be non null");
        if(A.length!=B.length || A[0].length!=B[0].length)
            throw new Exception("A and B must have the same number of rows and columns");
        
        for(int bi = 0; bi < B.length; bi++)
            for(int bj = 0; bj < B[0].length; bj++){
                A[bi][bj] = B[bi][bj];
            }
    }
    
    public static void setValue(double[][] A, int i, int j, double value) throws Exception
    {
        if(A==null)
            throw new Exception("A param must be non null");
        if(!(i>=0 && i<A.length))
            throw new Exception("i must be equal or greater than 0 and less than the number of rows in matrix A.");
        if(!(j>=0 && j<A[0].length))
            throw new Exception("j must be equal or greater than 0 and less than the number of columns in matrix A.");
        
        A[i][j] = value;
    }
    
    public static double[][] kroneckerProduct(double[][] A, double[][] B) throws Exception
    {
        
        if(A==null || B==null)
            throw new Exception("A and B params must be non null");
        
        double C[][] = new double[A.length*B.length][A[0].length*B[0].length];
        
        for(int ai = 0 ; ai < A.length; ai++){
            for( int aj = 0; aj<A[0].length; aj++){
                for (int bi = 0; bi<B.length; bi++){
                    for(int bj = 0; bj<B[0].length; bj++){
                        C[(ai*B.length)+bi][(aj*B[0].length)+bj]= A[ai][aj]*B[bi][bj];
                    }
                }
            }
        }
        return C;
    }
    
    public static double[][] scalarProduct(double n, double [][] A) throws Exception{
        if(A==null)
            throw new Exception("A param must be non null");
        
        double C[][] = new double[A.length][A[0].length];
        
        for(int ai = 0 ; ai < A.length; ai++){
                for( int aj = 0; aj<A[0].length; aj++){
                    C[ai][aj] += A[ai][aj]*n;
                }
        }
        return C;
    }
    
    public static double[][] hadamardProduct(double[][] A, double [][] B) throws Exception{
        if(A==null || B==null)
            throw new Exception("A and B param must be non null");
        if(A.length!= B.length || A[0].length!=B[0].length)
            throw new Exception("In Hadamard Product operands must have the same number of rows and columns, see: https://en.wikipedia.org/wiki/Hadamard_product_(matrices).");
        
        double C[][] = new double[A.length][A[0].length];
        
        for(int ai = 0 ; ai < A.length; ai++){
                for( int aj = 0; aj<A[0].length; aj++){
                    C[ai][aj] += A[ai][aj]*B[ai][aj];
                }
        }
        return C;
    }
    
    public static double[][] transposed(double[][] A) throws Exception
    {
        if(A==null)
            throw new Exception("A param must be non null");
        
        double[][] At = new double[A[0].length][A.length];
        
        for(int aj = 0 ; aj < A[0].length; aj++)
            for(int ai =0 ; ai < A.length; ai++)
            {
                At[aj][ai] = A[ai][aj];
            }
        return At;
    }
    
    public static double[][] createIdentity(int i)
    {
        double[][] I = new double[i][i];
        
        for(int ii=0; ii < i; i++)
            I[ii][ii]=1;
        
        return I;
    }
    
    public static double[][] diagonal(double[][] A) throws Exception
    {
        if(A==null)
            throw new Exception("A param must be non null");
        
        double[][] Da = new double[A.length][A[0].length];
        
        int diagonalItens = Math.min(A.length, A[0].length);
        
        for(int d = 0 ; d < diagonalItens; d++){
                Da[d][d] = A[d][d];
            }
        
        return Da;
    }
    
    public static double[][] add(double[][] A, double[][] B) throws Exception
    {
        if(A==null || B==null)
            throw new Exception("A and B params must be non null");
        if((A.length != B.length) || A[0].length!=B[0].length)
            throw new Exception("A and B params must have the same amount of rows and columns");
        double C[][] = new double[A.length][A[0].length];
        
        for(int ai = 0 ; ai < A.length; ai++){
                for( int aj = 0; aj<A[0].length; aj++){
                    C[ai][aj] += A[ai][aj]+B[ai][aj];
                }
        }
        return C;
    }
    
    public static double[][] scaleToVectorNorm(double[][] A, Integer normLevel, double normSize)
    {
        double norm = vectorNorm(A, normLevel);
        double[][] B = new double[A.length][A[0].length];
        
        for(int i = 0 ; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++){
                B[i][j] = A[i][j]/norm*normSize;
            }
        }
        return B;
    }
    
    public static double[][] scaleToVectorNorm(double[][] A, Integer normLevel)
    {
        return scaleToVectorNorm(A, normLevel, 1);
    }
    
    public static double vectorNorm(double[][] A, Integer level)
    {
        double lineNorm = 0.0;
        for(int i = 0 ; i < A.length; i++){
            for(int j = 0; j < A[0].length; j++){
                lineNorm+= Math.pow(Math.abs(A[i][j]), level);
            }
        }
        return Math.pow(lineNorm, (1.0D/level));
    }
    
    
    public static double[][] directSum(double[][] A, double[][] B) throws Exception
    {
        if(A==null || B==null)
            throw new Exception("A and B params must be non null");
        
        double C[][] = new double[A.length+B.length][A[0].length+B[0].length];
        
        for(int ai = 0 ; ai < A.length; ai++){
                for( int aj = 0; aj<A[0].length; aj++){
                    C[ai][aj] = A[ai][aj];
                }
        }
        
        for(int bi = 0 ; bi < B.length; bi++){
                for( int bj = 0; bj<B[0].length; bj++){
                    C[A.length+bi][A[0].length+bj] = B[bi][bj];
                }
        }
        return C;
    }
    
    public static double[][] createRandomMatrix(int i, int j)
    {
        return createRandomMatrix(i, j, 1);
    }
    
    public static double[][] createRandomMatrix(int i, int j, double randomModifier)
    {
        double[][] A = new double[i][j];
        
        for(int ai = 0 ; ai < A.length; ai++)
            for( int aj = 0; aj<A[ai].length; aj++)
                A[ai][aj] = Math.random()*randomModifier;     
        return A;
    }
    
    public static double[][] createColumnMatrix(double... values) throws Exception
    {
        if(values==null)
            throw new Exception("Array of values must be non null");
        
        double[][] A = new double[values.length][1];
        
        for( int aj = 0; aj<A.length; aj++)
            A[aj][0] = values[aj];     
        return A;
    }
    
    public static double[][] createColumnMatrix(int i) throws Exception
    {
        
        double[][] A = new double[i][1];
        return A;
    }
    
    public static double[][] createRowMatrix(int j) throws Exception
    {
        
        double[][] A = new double[1][j];
        return A;
    }
    
    public static double[][] createRandomColumnMatrix(int i,double randomModifier) throws Exception
    {   
        double[] values = new double[i];
        
        for(int j = 0; j<values.length; j++)
            values[j] = Math.random()*randomModifier;
        return createColumnMatrix(values);
    }
    
    public static double[][] sortedColumnMatrix(double[][] A) throws Exception
    {
        double[][] B = A;
        B = transposed(A);
        Arrays.sort(B[0]);
        return transposed(B);
    }
    
    public static double[][] createMatrix(int i, int j, double... values) throws Exception
    {
        if(values==null)
            throw new Exception("Array of values must be non null");
        if(values.length%i !=0 && values.length%j != 0)
            throw new Exception("Wrong amount of entries in array values. The lenght of the array must be divisible by param i and param j.");
        
        double[][] A = new double[i][j];
        
        int iValue=0;
        for(int ai = 0 ; ai < A.length; ai++)
            for( int aj = 0; aj<A[ai].length; aj++){
                A[ai][aj] = values[iValue];
                iValue++;
            }
        return A;
    }
    
    public static <T extends Object> T[][] createObjectColumnMatrix(T... values) throws Exception
    {
        if(values==null)
            throw new Exception("Array of values must be non null");
        T[][] A = (T[][]) Array.newInstance(values[0].getClass(), values.length,1);
        for(int i = 0; i<A.length; i++)
            A[i][0] = values[i];
        return A;
    }
    
    public static <T extends Object> T[][] createObjectColumnMatrix(Class<T> clazz, int i) throws Exception
    {
        T[] columns = (T[]) Array.newInstance(clazz, i);
        
        for(int j = 0; j < i; j++)
            columns[j] = clazz.getDeclaredConstructor().newInstance();
        
        return createObjectColumnMatrix(columns);
    }
    
    public static double[][] createBernoulliDistributionMatrix(int i, int j, double p)
    {
        return IntStream.range(1, i).
                    mapToObj(a -> new Random()
                                    .doubles(j, 0, 1)
                                    .map(d -> d>=p?1d:0d)
                                    .toArray())
                    .toArray(double[][]::new);
    }
    
    public static String toString(double[][] A) throws Exception
    {
        return toString(A, null);
    }
    
    public static String toString(double[][] A, NumberFormat formatter) throws Exception
    {
//        if(A==null)
//            throw new Exception("Param A must be non null.");
//        String s="";
//        for (double[] A1 : A) {
//            s+="|";
//            for (int aj = 0; aj < A1.length; aj++) {
//                if (formatter!=null) {
//                    s += formatter.format(A1[aj]) + "\t";
//                } else {
//                    s += A1[aj] + "\t";
//                }
//            }
//            s = s.substring(0, s.length()-1);
//            s+="|\n";
//        }
        return IntStream.range(0, A.length)
                    .mapToObj(ii ->
                        DoubleStream.of(A[ii])
                            .mapToObj(d -> formatter==null?Double.toString(d):formatter.format(d))
                            .collect(Collectors.joining(",")))
                .map(s -> "|"+s+"|")
                .collect(Collectors.joining("\n"));
    }
    
    
    //credit:: https://en.wikipedia.org/wiki/LU_decomposition
    public double[] SolveUsingLU(double[][] matrix, double[] rightPart, int n)
    {
        // decomposition of matrix
        double[][] lu = new double[n][n];
        double sum = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = i; j < n; j++)
            {
                sum = 0;
                for (int k = 0; k < i; k++)
                    sum += lu[i][k] * lu[k][j];
                lu[i][j] = matrix[i][j] - sum;
            }
            for (int j = i + 1; j < n; j++)
            {
                sum = 0;
                for (int k = 0; k < i; k++)
                    sum += lu[j][k] * lu[k][i];
                lu[j][i] = (1 / lu[i][i]) * (matrix[j][i] - sum);
            }
        }

        // lu = L+U-I
        // find solution of Ly = b
        double[] y = new double[n];
        for (int i = 0; i < n; i++)
        {
            sum = 0;
            for (int k = 0; k < i; k++)
                sum += lu[i][k] * y[k];
            y[i] = rightPart[i] - sum;
        }
        // find solution of Ux = y
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--)
        {
            sum = 0;
            for (int k = i + 1; k < n; k++)
                sum += lu[i][k] * x[k];
            x[i] = (1 / lu[i][i]) * (y[i] - sum);
        }
        return x;
    }
    
    
    //credit:: https://www.geeksforgeeks.org/java-program-to-find-the-determinant-of-a-matrix/
    static double determinantOfMatrix(double[][] A) throws Exception
    {
        if(A==null)
            throw new Exception("Param A cannot be null.");
        if(A.length!=A[0].length)
            throw new Exception("Param A must be a square matrix");
        
        int n =  A.length;
        double num1, num2, det = 1, total = 1;
        int index = 1; // Initialize result
 
        // temporary array for storing row
        double[] temp = new double[n + 1];
 
        // loop for traversing the diagonal elements
        for (int i = 0; i < n; i++) {
            index = i; // initialize the index
 
            // finding the index which has non zero value
            while (A[index][i] == 0 && index < n) {
                index++;
            }
            if (index == n) // if there is non zero element
            {
                // the determinant of matrix as zero
                continue;
            }
            if (index != i) {
                // loop for swaping the diagonal element row
                // and index row
                for (int j = 0; j < n; j++) {
                    swap(A, index, j, i, j);
                }
                // determinant sign changes when we shift
                // rows go through determinant properties
                det = (int)(det * Math.pow(-1, index - i));
            }
 
            // storing the values of diagonal row elements
            for (int j = 0; j < n; j++) {
                temp[j] = A[i][j];
            }
 
            // traversing every row below the diagonal
            // element
            for (int j = i + 1; j < n; j++) {
                num1 = temp[i]; // value of diagonal element
                num2 = A[j]
                          [i]; // value of next row element
 
                // traversing every column of row
                // and multiplying to every row
                for (int k = 0; k < n; k++) {
                    // multiplying to make the diagonal
                    // element and next row element equal
                    A[j][k] = (num1 * A[j][k])
                                - (num2 * temp[k]);
                }
                total = total * num1; // Det(kA)=kDet(A);
            }
        }
 
        // multiplying the diagonal elements to get
        // determinant
        for (int i = 0; i < n; i++) {
            det = det * A[i][i];
        }
        return (det / total); // Det(kA)/k=Det(A);
    }
 
    static double[][] swap(double[][] arr, int i1, int j1, int i2,
                        int j2)
    {
        double temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
        return arr;
    }
    
}
