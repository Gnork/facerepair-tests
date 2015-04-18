package com.iconn.facerepair;

import java.io.FileWriter;
import java.io.IOException;

public class App 
{
    
    public static void main( String[] args ) throws IOException
    {
        // FakeReconstruct fake = new FakeReconstruct();
        String outputFile = Settings.testOutput + "/errors.csv";
        FileWriter writer = new FileWriter(outputFile);
 
        writer.write("rbmConfig;reconstructionError\n");
        
        String w1000 = Settings.rbmWeights + "WildFaces_64x64_rgb_1kh_58380it_TE13,203_CVE14,907.dat";
        String w1500 = Settings.rbmWeights + "output_104000it_TE10,128_CVE12,156.dat";
        String w2000 = Settings.rbmWeights + "WildFaces_64x64_rgb_2kh_10440it.dat";
        
        Test test = new Test();
        float error;
        
        error = test.run(new RBMWrapper(new String[]{w1000}), "1000");
        writer.write("1000;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1500}), "1500");
        writer.write("1500;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w2000}), "2000");
        writer.write("2000;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1000,w1000}), "1000-1000");
        writer.write("1000-1000;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1500,w1500}), "1500-1500");
        writer.write("1500-1500;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w2000,w2000}), "2000-2000");
        writer.write("2000-2000;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1000,w1500}), "1000-1500");
        writer.write("1000-1500;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1500,w2000}), "1500-2000");
        writer.write("1500-2000;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1000,w2000}), "1000-2000");
        writer.write("1000-2000;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1000,w1500,w1500}), "1000-1500-1500");
        writer.write("1000-1500-1500;" + error + "\n");
        
        error = test.run(new RBMWrapper(new String[]{w1000,w1500,w2000}), "1000-1500-2000");
        writer.write("1000-1500-2000;" + error + "\n");
        
        writer.close();
    }
}
