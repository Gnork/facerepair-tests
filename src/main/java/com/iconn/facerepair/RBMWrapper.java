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
    public float[][][] apply(float[][] broken_data, boolean[][] inpaint_positions) {
        float[][][] result = new float[rbms.size()][][];
        
        for(int i = 0; i < broken_data.length; i++) {
            // Calculating mean
            int sum = 0;
            float sumR = 0;
            float sumG = 0;
            float sumB = 0;
            for(int j = 0; j < broken_data[0].length; j+=3) {
                if(!inpaint_positions[i][j/3]) {
                    sum++;
                    sumR+=broken_data[i][j+0];
                    sumG+=broken_data[i][j+1];
                    sumB+=broken_data[i][j+2];
                }
            }
            float avgR = sumR/sum;
            float avgG = sumG/sum;
            float avgB = sumB/sum;
            
            // Inserting mean into inpaint_positions
            for(int j = 0; j < broken_data[0].length; j+=3) {
                if(inpaint_positions[i][j/3]) {
                    broken_data[i][j+0] = avgR;
                    broken_data[i][j+1] = avgG;
                    broken_data[i][j+2] = avgB;
                }
            }
        }
        
        float[][] visible = broken_data;
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
