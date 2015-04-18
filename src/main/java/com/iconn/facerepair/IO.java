/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iconn.facerepair;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author christoph
 */
public class IO {
    public static float[][] loadTestData(String testData){
        return null;
    }
    
    public static float[][] loadWeights(String path){
        float[][] weights = null;
        
        ObjectInputStream ois = null;
        try {
            File file = new File(path);
            ois = new ObjectInputStream(Files.newInputStream(file.toPath()));
            weights = (float[][]) ois.readObject();
            ois.close(); 
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return weights;
    }
}
