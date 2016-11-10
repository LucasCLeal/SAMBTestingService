/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicamp.lucas.mbts;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONObject;

/**
 *
 * @author LucasCLeal
 * Class created to execute test over Web Service
 */
public class GraphWalkerRestClient {

        public static void main(String[] args) {

        GraphWalkerRestHandler gwRestHandler = new GraphWalkerRestHandler();
        TestGenerator tstGen = new TestGenerator();

        //resetando serviço e modelos
        gwRestHandler.resetService();

        //acessando arquivo com modelo//
        File modelfile = new File("/Users/LucasCLeal/Documents/ex.gw3");
        //File modelfile = new File("/Users/LucasCLeal/Documents/workspace/SAMBTestingService/MBTestingService/src/main/resources/TestModel.json"); 

        try {
            tstGen.loadModeltoGW(modelfile);
        } catch (Exception x) {
            System.out.println("IOException: " + x);
        }

        tstGen.exeTest(modelfile);
    }

}
