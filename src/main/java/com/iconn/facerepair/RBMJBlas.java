/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iconn.facerepair;


import org.jblas.FloatMatrix;
import org.jblas.MatrixFunctions;

/**
 *
 * @author christoph
 */
public class RBMJBlas{

    private FloatMatrix weights;

    public RBMJBlas(float[][] weights) {
        this.weights = new FloatMatrix(weights);    
    }
    
    public float[][] getHidden(float[][] data, boolean binarizeHidden) {

        FloatMatrix dataMatrix = new FloatMatrix(data);

        // Insert bias units of 1 into the first column of data.
        final FloatMatrix oneVector = FloatMatrix.ones(dataMatrix.getRows(), 1);
        final FloatMatrix dataWithBias = FloatMatrix.concatHorizontally(oneVector, dataMatrix);

        // Calculate the activations of the hidden units.
        final FloatMatrix hiddenActivations = dataWithBias.mmul(this.weights);

        // Calculate the probabilities of turning the hidden units on.
        FloatMatrix hiddenNodes = logistic(hiddenActivations);

        // Ignore the bias units.
        final FloatMatrix hiddenNodesWithoutBias = hiddenNodes.getRange(0, hiddenNodes.getRows(), 1, hiddenNodes.getColumns());
        
        return hiddenNodesWithoutBias.toArray2();
    }

    public float[][] getVisible(float[][] data, boolean binarizeVisible) {

        FloatMatrix dataMatrix = new FloatMatrix(data);

        // Insert bias units of 1 into the first column of data.
        final FloatMatrix oneVector = FloatMatrix.ones(dataMatrix.getRows(), 1);
        final FloatMatrix dataWithBias = FloatMatrix.concatHorizontally(oneVector, dataMatrix);

        // Calculate the activations of the visible units.
        final FloatMatrix visibleActivations = dataWithBias.mmul(weights.transpose());

        // Calculate the probabilities of turning the visible units on.
        FloatMatrix visibleNodes = logistic(visibleActivations);

        // Ignore bias
        final FloatMatrix visibleNodesWithoutBias = visibleNodes.getRange(0, visibleNodes.getRows(), 1, visibleNodes.getColumns());

        return visibleNodesWithoutBias.toArray2();

    }
    
    public FloatMatrix logistic(FloatMatrix m) {
		
        final FloatMatrix negM = m.neg();
        final FloatMatrix negExpM = MatrixFunctions.exp(negM);
        final FloatMatrix negExpPlus1M = negExpM.add(1.0f);
        final FloatMatrix OneDivideNegExpPlusOneM = MatrixFunctions.pow(negExpPlus1M, -1.0f); 		 
        return OneDivideNegExpPlusOneM;
    }
}
