package com.unicamp.lucas.rest;

import com.unicamp.lucas.common.FileHandler;
import com.unicamp.lucas.mbts.TestGenerator;
import java.awt.PageAttributes;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author LucasCLeal
 */

@Path("/exefulltest")
public class ExeFullTest {
    
    //locais de origem e destino que devem ser mudados de acordo com o sistema
    static java.nio.file.Path root = Paths.get("/Users/LucasCLeal/Documents/models/");
    //
   
    //iniciando objetos usados apra executar operações
    static TestGenerator tstGen = new TestGenerator();
    static FileHandler flHand = new FileHandler();

    //extenção de usado nos modelos 
    //static String ext = "graphml";
    static String ext = "json";
    
    
    @GET
    @Produces (MediaType.TEXT_PLAIN)
    public String ExeFullApplicationTest(){
        
        //execute testes using all models in the root folder.
        //buscando arquivos na pasta raiz   
        ArrayList<File> modelFiles = flHand.filesAtPathWithExtension(root, ext);
        String outPut = "";

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
                        outPut = outPut.concat(tstGen.exeTestReturnResult(modelFile));
                    }
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            }

        } else {
            System.out.println("please, check if the communication port to Rest Server is right.");
        }
        
        return outPut;
        
    }
    
}