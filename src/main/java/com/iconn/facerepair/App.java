package com.iconn.facerepair;

public class App 
{
    public static void main( String[] args )
    {
        String[] weights = {
            Settings.rbmWeights + "WildFaces_64x64_rgb_1kh_28380it.dat", 
            Settings.rbmWeights + "WildFaces_64x64_rgb_1500_104000it_TE10,128_CVE12,156.dat", 
            Settings.rbmWeights + "WildFaces_64x64_rgb_2kh_TE11,745_CVE13,817.dat"
        };
        
        RBMWrapper rbms = new RBMWrapper(weights);
    }
}
