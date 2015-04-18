/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iconn.facerepair;

import java.awt.Rectangle;
import javafx.scene.paint.Color;

/**
 *
 * @author christoph
 */
public class Test {
    private String testData;
    private String testOutput;
    
    private float[][] data;
    private static final Color brokenColor = Color.BLACK;
    
    public void Test(String testData, String testOutput){
        this.testData = testData;
        this.testOutput = testOutput;
        
        this.data = IO.loadTestData(testData);
    }
    
    public void run(IReconstruct recon){
        Rectangle[] inpaintPositionsRect = new Rectangle[2];
        inpaintPositionsRect[0] = new Rectangle(0,0,32,64);
        inpaintPositionsRect[1] = new Rectangle(22,0,20,64);
        
        boolean[][] inpaintPositions = new boolean[inpaintPositionsRect.length][];
        for(int i = 0; i < inpaintPositions.length; ++i){
            inpaintPositions[i] = createInpaintPositionsFromRect(inpaintPositionsRect[i]);
        }
        
        for(int i = 0; i < inpaintPositions.length; ++i){
            float[][] brokenImage = breakImage(inpaintPositions[i]);
        }
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
    
    public float[][] breakImage(boolean[] inpaintPositions){
        // TODO
        return null;
    }
}
