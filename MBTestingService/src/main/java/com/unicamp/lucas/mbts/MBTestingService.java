/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicamp.lucas.mbts;

import com.unicamp.lucas.common.fileHandler;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author LucasCLeal
 */
@WebService(serviceName = "MBTestingService")
public class MBTestingService {

    /**
     * Operação de Web service
     *
     * @param fileName
     * @param fileData
     * @return
     * @throws java.io.IOException
     */
    @WebMethod(operationName = "uploadFile")
    public Boolean uploadFile(@WebParam(name = "fileName") String fileName, @WebParam(name = "fileData") byte[] fileData) throws IOException {

        //iniciando file handler
        fileHandler fi = new fileHandler();

        boolean result;

        //locais de origem e destino que devem ser mudados de acordo com o sistema
        Path root = Paths.get("/Users/LucasCLeal/Documents");
        Path archive = Paths.get("/Users/LucasCLeal/Documents/workspace/models");
        //

        Path filePath = fi.generateFilePath(root, fileName);
        System.out.println("file location: " + filePath.toString());

        //caso o arquivo já exista no WS é necessário salvar ele na pasta de archive, caso não exista executar o download normalmente
        if (fi.fileOrfolderExist(filePath)) {
            System.out.println("File exists");
            try {
                fi.backupFile(filePath, archive);
                fi.deleteFileAtPath(filePath);
                fi.writeDataInToFilePath(filePath, fileData);
                result = true;
            } catch (IOException upfail) {
                System.err.format("IOException: %s%n", upfail);
                result = false;
            }
            System.out.println("old model archived and model updated at root folder");
        } else {
            System.out.println("File does not exist yet");
            fi.writeDataInToFilePath(filePath, fileData);
            result = true;
        }

        return result;
    }
}
