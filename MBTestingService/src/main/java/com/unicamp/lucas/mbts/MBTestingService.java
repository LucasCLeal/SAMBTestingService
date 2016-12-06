/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicamp.lucas.mbts;

import com.unicamp.lucas.common.FileHandler;
import com.unicamp.lucas.mbts.TestGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author LucasCLeal Serviço SOAP para model based testing
 */
@WebService(serviceName = "MBTestingService")
public class MBTestingService {

    //iniciando objetos usados apra executar operações
    static TestGenerator tstGen = new TestGenerator();
    static FileHandler flHand = new FileHandler();

    static Path rootPath = Paths.get("/Users/LucasCLeal/Documents/models");

    //extenção de usado nos modelos 
    //static String ext = "graphml";
    static String ext = "json";

    /**
     * Operação de Web service
     *
     * @param fileName
     * @param fileData
     * @return
     * @throws java.io.IOException
     */
    @WebMethod(operationName = "modelUpdate")
    public String modelUpdate(@WebParam(name = "fileName") String fileName, @WebParam(name = "fileData") byte[] fileData) throws IOException {

        //acessando arquivo com modelo//
        //File modelfile = new File("/Users/LucasCLeal/Documents/ex.gw3");
        //File modelfile = new File("/Users/LucasCLeal/Documents/workspace/SAMBTestingService/MBTestingService/src/main/resources/TestModel.json");
        //executando teste usando novo modelo
        //salvando arquivo
        try {
            //salvando arquivo
            Path modelPath = flHand.updateFileAndArchive(fileName, fileData);
            //verificando se GW-rest está operacional e carregando arquivo no serviço
            File modelfile = new File(modelPath.toString());
            tstGen.loadModeltoGW(modelfile);
            //executando teste
            tstGen.exeTest(modelfile);
            return "done!";
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return "fail!";
        }

    }

    @WebMethod(operationName = "executeAllTests")
    public void executeAllTests() {

        //execute testes using all models in the root folder.
        //buscando arquivos na pasta raiz   
        ArrayList<File> modelFiles = flHand.filesAtPathWithExtension(rootPath, ext);

        //exibindo resultado da busca
        System.out.println("models found: ");
        for (File modelFile : modelFiles) {
            System.out.println(modelFile.getName());
        }

        //executando testes para cada arquivo de modelo encontrado
        if (tstGen.isGWRestServiceOnline()) {
            for (File modelFile : modelFiles) {
                try {
                    if (tstGen.loadModeltoGW(modelFile)) {
                        tstGen.exeTest(modelFile);
                    }
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }

        } else {
            System.out.println("please, check if the communication port to Rest Server is right.");
        }
    }

}
