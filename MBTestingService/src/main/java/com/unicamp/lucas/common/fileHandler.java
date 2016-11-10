/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicamp.lucas.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author LucasCLeal
 */
public class FileHandler {
    //todas as operações usand file i/o podem gerar Exceptions, sendo assim todos os metodos devem ser construidos dentro de trys 

    //locais de origem e destino que devem ser mudados de acordo com o sistema
    static Path root = Paths.get("/Users/LucasCLeal/Documents");
    static Path archive = Paths.get("/Users/LucasCLeal/Documents/workspace/models");

    static String separator = System.getProperty("line.separator");
    //

    //buscar por arquivo
    public boolean fileOrfolderExist(Path filePath) throws IOException {
        System.out.println("checking: " + filePath.toString());
        return Files.exists(filePath);
    }

    //gerando path para arquivo
    public Path generateFilePath(Path path, String fileNameWithExt) {
        Path filePath = path.resolve(fileNameWithExt);
        System.out.println(filePath.toString());
        return filePath;
    }

    //obtendo atributos de um arquivo
    public BasicFileAttributes getFileAttributes(Path filePath) throws IOException {
        //dados do arquivo buscado
        BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

        //System.out.println("creationTime: " + attr.creationTime());
        //System.out.println("size: " + attr.size());
        return attr;

    }

    //criar arquivo
    public void createNewFileAtPath(Path path) {

        //Dependendo do sistema operacional e da pasta selecionada o loca do destino dos modelos tem
        //que ser atualizado, ou as permissões escrita na pasta alvo tem que ser atualizadas
        System.out.println("Creating new file...");

        try {
            // Create the empty file with default permissions, etc.
            Files.createFile(path);
            System.out.println("empty file created.");
        } catch (FileAlreadyExistsException x) {
            System.err.format("file named %s"
                    + " already exists%n", path);
        } catch (IOException y) {
            // Some other sort of failure, such as permissions.
            System.err.format("createFile error: %s%n", y);
        }

    }

    public String fileCreationDate(Path file, String separator) throws IOException {
        BasicFileAttributes attr = getFileAttributes(file);
        DateFormat df = new SimpleDateFormat("MM" + separator + "dd" + separator + "yyyy");
        String dateCreated = df.format(attr.creationTime().toMillis());
        return dateCreated;
    }

    public String addCreationDateToFileName(Path file) throws IOException {

        String dateCreated = fileCreationDate(file, "_");
        String fileNameWithOutExt = FilenameUtils.removeExtension(file.getFileName().toString());
        String fileExt = FilenameUtils.getExtension(file.getFileName().toString());
        String newFileName = fileNameWithOutExt + "_" + dateCreated + "." + fileExt;
        System.out.println("backup file name: " + newFileName);
        return newFileName;

    }

    //copiar arquivo para pasta de backup
    public void backupFile(Path originalFile, Path copyDestination) throws IOException {

        //copiar arquivo para nova pasta onde outros backups são guardados
        System.out.println("starting backup");
        //verificando se arquivo existe no local de origem
        try {
            //criando novo nome para backup file, baseado na sua data e hora de modificação
            String newFileName = addCreationDateToFileName(originalFile);
            try {
                //verificando se destino existe, se existir criar backup
                if (fileOrfolderExist(copyDestination)) {
                    System.out.println("folder path exist");
                    Path fileCopyPath = generateFilePath(copyDestination, newFileName);
                    Files.copy(originalFile, fileCopyPath, COPY_ATTRIBUTES);
                    System.out.println("model copied to archive");
                } else {
                    System.out.println("archive file not found, backup aborted.");
                }

            } catch (IOException xin) {
                System.out.println("path to archive folder invalid");
                System.out.println("Exception" + xin);
            }
        } catch (IOException xout) {
            System.out.println("there is no file :" + originalFile.getFileName().toString() + " at root folder");
            System.out.println("Exception" + xout);
        }
    }

    public void deleteFileAtPath(Path filePath) {

        System.out.println("deleteModelFile:" + filePath.toString());

        try {
            Files.delete(filePath);
            System.out.println("done");
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", filePath);
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", filePath);
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }

    }

    public void writeDataInToFilePath(Path filePath, byte[] data) {

        System.out.println("writeDataInToFilePath: " + filePath.toString());

        try {
            Files.write(filePath, data, StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
            System.err.format("IOException: %s%n", e);
        }

        /*
        tentativas de maneiras de escrever em aquivos
        
        try (FileOutputStream fos = new FileOutputStream(filePath.toString())) {
            fos.write(data);
            fos.flush();
        } catch (IOException x) {
            System.err.println(x);
        }

       
        Charset charset = Charset.forName("US-ASCII");
        String s = ...;
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
         */
    }

    public ArrayList<File> filesAtPathWithExtension(Path rootPath, String filesExtention) {

        File folder = new File(rootPath.toString());
        File[] listOfFiles = folder.listFiles();

        /*exibindo arquivos e pastas no diretório
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                System.out.println("File " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Directory " + listOfFile.getName());
            }
        }*/
        //criando lista de arquivos.
        ArrayList<File> modelFiles = new ArrayList<>();

        for (File lisFile : listOfFiles) {
            //caso seja um arquivo e tenha a extenção desejada é adicionado a lista
            if (lisFile.isFile() && FilenameUtils.getExtension(lisFile.getName()).equals(filesExtention)) {
                modelFiles.add(lisFile);
            }
        }

        return modelFiles;

    }

    public Path updateFileAndArchive(String fileName, byte[] fileData) throws IOException {

        Path filePath = generateFilePath(root, fileName);

        //caso o arquivo já exista no WS é necessário salvar ele na pasta de archive, caso não exista executar o download normalmente
        if (fileOrfolderExist(filePath)) {
            System.out.println("File already exists exists");
            try {
                backupFile(filePath, archive);
                deleteFileAtPath(filePath);
                writeDataInToFilePath(filePath, fileData);

            } catch (IOException upfail) {
                System.err.format("IOException: %s%n", upfail);
            }
            System.out.println("old model archived and model updated at root folder");
        } else {
            System.out.println("new model");
            writeDataInToFilePath(filePath, fileData);
        }

        return filePath;
    }

    public String File2String(File modelfile) throws FileNotFoundException, IOException {

        //recieces a model file (the content is in JSON format) and returns a string with the content of the file
        String stringModel = "";
        InputStream modelInputStream = new FileInputStream(modelfile);
        InputStreamReader modelReader = new InputStreamReader(modelInputStream);
        BufferedReader br = new BufferedReader(modelReader);
        String line;
        while ((line = br.readLine()) != null) {
            stringModel = stringModel.concat(line + separator);
        }
        return stringModel;

    }

}
