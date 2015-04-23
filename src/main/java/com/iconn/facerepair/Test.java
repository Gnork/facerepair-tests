/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iconn.facerepair;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;

/**
 *
 * @author christoph
 */
public class Test {
    
    private float[][] data;
    private static final Color brokenColor = Color.BLACK;
    
    public Test() throws IOException{
        this.data = IO.loadTestData();
    }
    
    public float[] run(IReconstruct recon, String testName) throws IOException{
        System.out.println("Test: " + testName);
        // generate test cases
        Rectangle[] inpaintRects = new Rectangle[7];
        inpaintRects[0] = new Rectangle(0,0,32,64);
        inpaintRects[1] = new Rectangle(0,0,64,32);
        inpaintRects[2] = new Rectangle(0,32,64,32);
        
        inpaintRects[3] = new Rectangle(10,10,20,44);
        inpaintRects[4] = new Rectangle(22,10,20,44);
        inpaintRects[5] = new Rectangle(10,5,44,20);
        inpaintRects[6] = new Rectangle(10,34,44,20);
        
        boolean[][] inpaintMaps = new boolean[inpaintRects.length][];
        for(int i = 0; i < inpaintMaps.length; ++i){
            inpaintMaps[i] = createInpaintPositionsFromRect(inpaintRects[i]);
        }
        
        float[][] errorsPerTestCase = new float[inpaintMaps.length][2];
        
        // iterate through test cases
        for(int testCase = 0; testCase < inpaintMaps.length; ++testCase){
            System.out.println("Case: " + testCase);
            float[][] brokenImages = breakImages(this.data, inpaintMaps[testCase]);
            
            float[][][] testResults = recon.apply(brokenImages, inpaintMaps[testCase]);
            
            // store result images for test case
            for(int imageIndex = 0; imageIndex < brokenImages.length; ++ imageIndex){
                float[][] imageResults = new float[testResults.length][];
                for (int resultIndex = 0; resultIndex < testResults.length; ++resultIndex){
                    imageResults[resultIndex] = testResults[resultIndex][imageIndex];
                }
                IO.writeResultsForImage(testName, testCase, imageIndex, this.data[imageIndex], brokenImages[imageIndex], imageResults);
            }
            
            // calculate errors for test case
            float[][] errorsPerImage = new float[brokenImages.length][2];
            for(int i = 0; i < brokenImages.length; ++i){
                // errors[i][0] is error for RBM reconstruction
                errorsPerImage[i][0] = meanErrorPerImage(this.data[i], testResults[testResults.length - 1][i], inpaintMaps[testCase]);
                // errors[i][1] is error for inserting mean color
                errorsPerImage[i][1] = meanErrorPerImage(this.data[i], testResults[0][i], inpaintMaps[testCase]);
                
                errorsPerTestCase[testCase][0] += errorsPerImage[i][0];
                errorsPerTestCase[testCase][1] += errorsPerImage[i][1];
            }
            errorsPerTestCase[testCase][0] = errorsPerTestCase[testCase][0] / brokenImages.length;
            errorsPerTestCase[testCase][1] = errorsPerTestCase[testCase][1] / brokenImages.length;         
            
            IO.writeErrorsPerImage(testName, testCase, errorsPerImage);
        }
        IO.writeErrorsPerTestCase(testName, errorsPerTestCase);
        
        float[] meanErrorForCompleteTest = new float[]{0f, 0f};
        for(int i = 0; i < errorsPerTestCase.length; ++i){
            meanErrorForCompleteTest[0] += errorsPerTestCase[i][0];
            meanErrorForCompleteTest[1] += errorsPerTestCase[i][1];
        }
        meanErrorForCompleteTest[0] /= errorsPerTestCase.length;
        meanErrorForCompleteTest[1] /= errorsPerTestCase.length;
        System.out.println("RMSE: " + meanErrorForCompleteTest[0]);
        System.out.println("Baseline RMSE: " + meanErrorForCompleteTest[1]);
        return meanErrorForCompleteTest;
    }
    
    public float meanErrorPerImage(float[] originalImage, float[] reconstructedImage, boolean[] inpaintMap){
        int sum = 0;
        int error = 0;
        for(int i = 0; i < inpaintMap.length; ++i){
            int pos = i * 3;
            if(inpaintMap[i]){
                int deltaR = (int)(originalImage[pos]*255) - (int)(reconstructedImage[pos]*255);
                int deltaG = (int)(originalImage[pos + 1]*255) - (int)(reconstructedImage[pos + 1]*255);
                int deltaB = (int)(originalImage[pos + 2]*255) - (int)(reconstructedImage[pos + 2]*255);
                error += deltaR * deltaR;
                error += deltaG * deltaG;
                error += deltaB * deltaB;
                sum += 3;
            }
        }
        float meanError = (float)error / (float)sum;
        return (float)Math.sqrt(meanError);
    }
    
    public boolean[] createInpaintPositionsFromRect(Rectangle rect){
        boolean[] result = new boolean[64 * 64];
        for(int y = 0; y < 64; ++y){
            for(int x = 0; x < 64; ++x){
                int pos = y * 64 + x;
                result[pos] = x >= rect.x && x < rect.width + rect.x && y >= rect.y && y < rect.height + rect.y;
            }
        }
        return result;
    }
    
    public float[][] breakImages(float[][] originalImages, boolean[] inpaintMap){
        float[][] results = new float[originalImages.length][];
        for(int i = 0; i < originalImages.length; ++i){
            results[i] = breakImage(originalImages[i], inpaintMap);
        }
        return results;
    }
    
    public float[] breakImage(float[] originalImage, boolean[] inpaintMap){
        float brokenColorR = brokenColor.getRed() / 255f;
        float brokenColorG = brokenColor.getGreen() / 255f;
        float brokenColorB = brokenColor.getBlue() / 255f;
        
        float[] result = new float[originalImage.length];
        for(int i = 0; i < inpaintMap.length; ++i){
            int pos = i * 3;
            if (inpaintMap[i]){
                result[pos] = brokenColorR;
                result[pos + 1] = brokenColorG;
                result[pos + 2] = brokenColorB;
            }else{
                result[pos] = originalImage[pos];
                result[pos + 1] = originalImage[pos + 1];
                result[pos + 2] = originalImage[pos + 2];
            }
        }
        return result;
    }
}
