/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iconn.facerepair;

import java.util.ArrayList;
import java.util.Arrays;

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
                    // copying original pixel data
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
        
        float[][][] result = new float[rbms.size()*2 + 1][][];
        result[0] = deepCopy(imagesWithMean);
        
        float[][] visible = imagesWithMean;
        for(int i = 0; i < rbms.size(); i++) {
              RBMJBlas rbm = rbms.get(i);
            
              float[][] hidden = rbm.getHidden(visible, false);
              visible = rbm.getVisible(hidden, false);             
              result[i * 2 + 1] = deepCopy(visible);
              
              insertOriginalPixelsInReconstruction(visible, brokenImages, inpaintMap);
              result[i * 2 + 2] = deepCopy(visible);
        }
        
        return result;
    }
    
    public static void insertOriginalPixelsInReconstruction(float[][] reconstructions, float[][] brokenImages, boolean[] inpaintMap){
        for(int i = 0; i < reconstructions.length; ++i){
            for(int k = 0; k < inpaintMap.length; ++k){
                int pos = k * 3;
                if(!inpaintMap[k]){
                    reconstructions[i][pos] = brokenImages[i][pos];
                    reconstructions[i][pos + 1] = brokenImages[i][pos + 1];
                    reconstructions[i][pos + 2] = brokenImages[i][pos + 2];
                }
            }
        }
    }
    
    // code from http://stackoverflow.com/a/1564856/1793836
    public static float[][] deepCopy(float[][] original) {
        if (original == null) {
            return null;
        }

        final float[][] result = new float[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }
    
}
