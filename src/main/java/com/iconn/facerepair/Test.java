/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iconn.facerepair;

import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author christoph
 */
public class Test {
    private String testData;
    private String testOutput;
    
    private float[][] data;
    private static Color brokenColor = Color.BLACK;
    
    public void Test(String testData, String testOutput){
        this.testData = testData;
        this.testOutput = testOutput;
        
        this.data = IO.loadTestData(testData);
    }
    
    public void run(IReconstruct recon){
        
        // generate test cases
        Rectangle[] inpaintRects = new Rectangle[2];
        inpaintRects[0] = new Rectangle(0,0,32,64);
        inpaintRects[1] = new Rectangle(22,0,20,64);
        
        boolean[][] inpaintMaps = new boolean[inpaintRects.length][];
        for(int i = 0; i < inpaintMaps.length; ++i){
            inpaintMaps[i] = createInpaintPositionsFromRect(inpaintRects[i]);
        }
        
        // iterate through test cases
        for(int testCase = 0; testCase < inpaintMaps.length; ++testCase){
            float[][] brokenImages = breakImages(this.data, inpaintMaps[testCase]);
            float[][][] testResults = recon.apply(brokenImages, inpaintMaps);
            
            // store result images for test case
            for(int imageIndex = 0; imageIndex < brokenImages.length; ++ imageIndex){
                float[][] imageResults = new float[testResults.length][];
                for (int resultIndex = 0; resultIndex < testResults.length; ++resultIndex){
                    imageResults[resultIndex] = testResults[resultIndex][imageIndex];
                }
                IO.writeResultsForImage(testCase, imageIndex, this.data[imageIndex], brokenImages[imageIndex], imageResults);
            }
            
            // calculate errors for test case
            float[] errors = new float[brokenImages.length];
        }
    }
    
    
    
    public boolean[] createInpaintPositionsFromRect(Rectangle rect){
        boolean[] result = new boolean[64 * 64];
        for(int y = 0; y < 64; ++y){
            for(int x = 0; x < 64; ++x){
                int pos = y * 64 + x;
                if(x >= rect.x && x < rect.width + rect.x && y >= rect.y && y < rect.height + rect.y){
                    result[pos] = true;
                }else{
                    result[pos] = false;
                }
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
