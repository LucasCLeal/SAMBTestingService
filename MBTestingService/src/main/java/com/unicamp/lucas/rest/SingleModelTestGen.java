/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicamp.lucas.rest;

import com.unicamp.lucas.common.FileHandler;
import com.unicamp.lucas.common.pojo.ModelsInfo;
import com.unicamp.lucas.mbts.TestGenerator;
import static com.unicamp.lucas.rest.GetModelList.flHand;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.POST;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author LucasCLeal
 */
@Path("/singleMTG")
public class SingleModelTestGen {
    
    
    //locais de origem e destino que devem ser mudados de acordo com o sistema
    static java.nio.file.Path root = Paths.get("/Users/LucasCLeal/Documents/models/");
    //
   
    //iniciando objetos usados apra executar operações
    static TestGenerator tstGen = new TestGenerator();
    static FileHandler flHand = new FileHandler();

    //extenção de usado nos modelos 
    //static String ext = "graphml";
    static String ext = "json";

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String ExecuteSingleTest(String model) {

        String data = "/singleMTG: " + model;
        System.out.println(data);

        //executando teste usando nome de modelo recebido
        
        try {
            //buscando arquivo
            File modelfile = new File(flHand.fileAtPathWithName(root, model, ext).toString());
            //verificando se GW-rest está operacional e carregando arquivo no serviço
            tstGen.loadModeltoGW(modelfile);
            //executando teste
            return tstGen.exeTestReturnResult(modelfile);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return "fail!";
        }

        
    }

}
