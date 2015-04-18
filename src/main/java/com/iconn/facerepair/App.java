package com.iconn.facerepair;

import java.io.IOException;

public class App 
{
    
    public static void main( String[] args ) throws IOException
    {
        // FakeReconstruct fake = new FakeReconstruct();
        
        
        String w1000 = Settings.rbmWeights + "WildFaces_64x64_rgb_1kh_58380it_TE13,203_CVE14,907.dat";
        String w1500 = Settings.rbmWeights + "output_104000it_TE10,128_CVE12,156.dat";
        String w2000 = Settings.rbmWeights + "WildFaces_64x64_rgb_2kh_10440it.dat";
        
        Test test = new Test();
        
        RBMWrapper rbm1000 = new RBMWrapper(new String[]{w1000});
        test.run(rbm1000, "1000");
        
        RBMWrapper rbm1500 = new RBMWrapper(new String[]{w1500});
        test.run(rbm1500, "1500");
        
        RBMWrapper rbm2000 = new RBMWrapper(new String[]{w2000});
        test.run(rbm2000, "2000");
        
        RBMWrapper rbm100015002000 = new RBMWrapper(new String[]{w1000,w1500,w2000});
        test.run(rbm100015002000, "1000-1500-2000");
    }
}
