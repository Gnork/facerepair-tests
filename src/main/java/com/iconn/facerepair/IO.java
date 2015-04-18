/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iconn.facerepair;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author christoph
 */
public class IO {
    public static float[][] loadTestData(String testData) throws IOException{
        BufferedImage[] images = openImageFiles(testData);
        return imagesToFloat(images);
    }
    
    public static BufferedImage[] openImageFiles(String testData) throws IOException{
        ArrayList<BufferedImage> result = new ArrayList<>();
        File dir = new File(testData);
        for (final File f : dir.listFiles()) {
            BufferedImage img = ImageIO.read(f);
            result.add(img);
        }
        
        BufferedImage[] resultArray = new BufferedImage[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }
    
    public static float[][] imagesToFloat(BufferedImage[] images){
        float[][] result = new float[images.length][];
        for(int i = 0; i < images.length; ++i){
            result[i] = imageToFloat(images[i]);
        }
        return result;
    }
    
    public static float[] imageToFloat(BufferedImage image){
        int[] rgbData = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        float[] result = new float[rgbData.length * 3];
        for(int i = 0; i < rgbData.length; ++i){
            int pos = i * 3;
            Color c = new Color(rgbData[i]);
            result[pos] = c.getRed() / 255f;
            result[pos + 1] = c.getGreen() / 255f;
            result[pos + 2] = c.getBlue() / 255f;
        }
        return result;
    }
    
    public static void writeResultsForImage(int testCase, int imageIndex, float[] originalImage, float[] brokenImage, float[][] resultImages){
        
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
