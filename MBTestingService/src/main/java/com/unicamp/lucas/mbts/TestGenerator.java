/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicamp.lucas.mbts;

import com.unicamp.lucas.common.FileHandler;
import com.unicamp.lucas.common.pojo.TestStatistics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.json.JSONObject;

/**
 *
 * @author LucasCLeal Classe criada para lidar com a criação de casos de teste,
 * conecxão do com clintes SAOP para TS, e geração de relatórios com testes.
 */
public class TestGenerator {

    static GraphWalkerRestHandler gwRestHandler = new GraphWalkerRestHandler();
    static FileHandler flHandler = new FileHandler();
    static TestReport tstRep = new TestReport();

    public boolean loadModeltoGW(File modelfile) throws IOException {

        //resetando serviço
        gwRestHandler.resetService();
        //tentando ler arquivo
        String jsModel = flHandler.File2String(modelfile);
        System.out.println(jsModel);
        return gwRestHandler.loadModel(jsModel);

    }

    public boolean isGWRestServiceOnline() {
        //verificando se serviço está online
        try {
            gwRestHandler.isServiceOnline();
            System.out.println("GraphWalker Rest Service online");
            return true;
        } catch (Exception e) {
            System.out.println("GraphWalker Rest Service not online: " + e);
            return false;
        }
    }

    public void setTestGeneratorAndStopCondition() {

    }

    public void getStatistic() {

        //criando JSobject para receber estatisticas
        JSONObject jsObject = new JSONObject(gwRestHandler.getTestStatistics());
        //fazendo o parse do objecto JSON para um objeto da classe java TestStatistics
        TestStatistics tstStats = new TestStatistics(jsObject.getString("edgeCoverage"),
                jsObject.getString("result"),
                jsObject.getString("totalNumberOfEdges"),
                jsObject.getString("totalNumberOfUnvisitedEdges"),
                jsObject.getString("totalNumberOfUnvisitedVertices"),
                jsObject.getString("totalNumberOfVertices"),
                jsObject.getString("totalNumberOfVisitedEdges"),
                jsObject.getString("totalNumberOfVisitedVertices"),
                jsObject.getString("vertexCoverage"));

        System.out.println("result: " + tstStats.getResult());
        System.out.println("edgeCoverage: " + tstStats.getEdgeCoverage());

    }

    public void exeTest(File modelfile) {
        //metodo responsável por fazer o link entre o TS e o graphWalker RestClient
        //testes e resultados são salvos num arquivo de texto.
        System.out.println("Starting test generation...");
        //gwRestHandler.resetService();
        String repHead = tstRep.generateReportHeader(modelfile);
        String repBody = "";
        while (gwRestHandler.hasNextStep()) {
            //filtrar entre vertices e arestas talvez seja necessário
            String currentElement = gwRestHandler.getNextStep();
            //chamada para teste para TESTING SERVICE
            
            //adicionando linha no relatório com 
            repBody = tstRep.fillReportBody(repBody, currentElement, "success");
        }
        
        
        System.out.println(repHead+repBody);
        //relatorio
        tstRep.updateTestReportFile(modelfile.getName(), repHead+repBody);
        
        gwRestHandler.stopService();
        System.out.println("Test finished!");

    }

}
