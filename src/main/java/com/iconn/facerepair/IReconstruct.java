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
public interface IReconstruct {
    public float[][][] apply(float[][] broken_data, boolean[][] inpaint_positions);
}
