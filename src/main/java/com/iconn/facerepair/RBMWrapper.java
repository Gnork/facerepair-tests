/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iconn.facerepair;

import java.util.ArrayList;

/**
 *
 * @author Radek
 */
public class RBMWrapper implements IReconstruct {
    
    ArrayList<RBMJBlas> rbms = new ArrayList<>();
    
    public RBMWrapper(String[] fileNames){
        for (String fileName : fileNames) {
            float[][] weights = IO.loadWeights(fileName);
            RBMJBlas rbm = new RBMJBlas(weights);
            rbms.add(rbm);
        }
    };

    @Override
    public float[][][] apply(float[][] brokenImages, boolean[] inpaintMap) {
        float[][][] result = new float[rbms.size()][][];
        float[][] imagesWithMean = new float[brokenImages.length][];
        
        for(int i = 0; i < brokenImages.length; i++) {
            float[] imageWithMean = new float[brokenImages[i].length];
            // Calculating mean
            int sum = 0;
            float sumR = 0;
            float sumG = 0;
            float sumB = 0;
            for(int k = 0; k < inpaintMap.length; ++k){
                int pos = k * 3;
                if(!inpaintMap[k]){
                    sum++;
                    sumR += brokenImages[i][pos];
                    sumG += brokenImages[i][pos + 1];
                    sumB += brokenImages[i][pos + 2];
                }
            }
            float meanR = sumR/sum;
            float meanG = sumG/sum;
            float meanB = sumB/sum;
            
            for(int k = 0; k < inpaintMap.length; ++k){
                int pos = k * 3;
                if(!inpaintMap[k]){
                    // copying original pixel daten
                    imageWithMean[pos] = brokenImages[i][pos];
                    imageWithMean[pos + 1] = brokenImages[i][pos + 1];
                    imageWithMean[pos + 2] = brokenImages[i][pos + 2];
                }else{
                    // inserting mean at inpaint positions
                    imageWithMean[pos] = meanR;
                    imageWithMean[pos + 1] = meanG;
                    imageWithMean[pos + 2] = meanB;
                }
            }
            imagesWithMean[i] = imageWithMean;
        }
        
        float[][] visible = imagesWithMean;
        for(int i = 0; i < rbms.size(); i++) {
              RBMJBlas rbm = rbms.get(i);
            
              float[][] hidden = rbm.getHidden(visible, false);
              visible = rbm.getVisible(hidden, false);
              
              float[][] visibleRBMI = new float[visible.length][visible[0].length];
              System.arraycopy(visible, 0, visibleRBMI, 0, visible.length);
              result[i] = visibleRBMI;
        }
        
        return result;
    }
    
}
