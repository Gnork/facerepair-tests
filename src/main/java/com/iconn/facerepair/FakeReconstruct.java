/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iconn.facerepair;

/**
 *
 * @author christoph
 */
public class FakeReconstruct implements IReconstruct {

    @Override
    public float[][][] apply(float[][] broken_data, boolean[][] inpaint_positions) {
        return new float[][][]{broken_data};
    }
    
}
