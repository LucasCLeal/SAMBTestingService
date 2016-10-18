/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.unicamp.lucas.libs;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author LucasCLeal
 */
public class fileHandler {
    //todas as operações usand file i/o podem gerar Exceptions, sendo assim todos os metodos devem ser construidos dentro de trys 

    //buscar por arquivo
    public boolean graphmlModelFileExist(Path filePath) throws IOException {
        System.out.println("filehandler.graphmlModelFileExist()");
        return Files.exists(filePath);
    }

    //gerando path para arquivo
    /**
     *
     * @param path
     * @param fileName
     * @return
     */
    public Path filePathForModel(Path path, String fileName) {
        System.out.println(path.toString());
        System.out.println(fileName);

        Path filePath = path.resolve(fileName + ".graphml");
        return filePath;
    }

    //obtendo atributos de um arquivo
    public BasicFileAttributes getFileAttributes(Path filePath) throws IOException {
        //dados do arquivo buscado
        BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

        System.out.println("creationTime: " + attr.creationTime());
        System.out.println("lastAccessTime: " + attr.lastAccessTime());
        System.out.println("lastModifiedTime: " + attr.lastModifiedTime());

        System.out.println("isDirectory: " + attr.isDirectory());
        System.out.println("isOther: " + attr.isOther());
        System.out.println("isRegularFile: " + attr.isRegularFile());
        System.out.println("isSymbolicLink: " + attr.isSymbolicLink());
        System.out.println("size: " + attr.size());

        return attr;

    }

    //criar arquivo
    public boolean createNewModelFile(Path path, String fileName) {

        //Dependendo do sistema operacional e da pasta selecionada o loca do destino dos modelos tem
        //que ser atualizado, ou as permissões escrita na pasta alvo tem que ser atualizadas
        System.out.println("Criando novo arquivo de modelo");
        boolean output;

        Path filePath = filePathForModel(path, fileName);
        try {
            // Create the empty file with default permissions, etc.
            Files.createFile(filePath);
            output = true;
        } catch (FileAlreadyExistsException x) {
            System.err.format("file named %s"
                    + " already exists%n", filePath);
            output = false;
        } catch (IOException y) {
            // Some other sort of failure, such as permissions.
            System.err.format("createFile error: %s%n", y);
            output = false;
        }
        return output;

    }

    //copiar modelo para pasta de backup
    public void backupModelFile(Path originalFile, Path copyDestination, String fileName) throws IOException {

        //copiar arquivo para nova pasta onde outros backups são guardados
        System.out.println("executando backup de modelo");
        //verificando se arquivo existe no local de origem
        try {
            if (graphmlModelFileExist(originalFile)) {
                //criando novo nome para backup file, baseado na sua data e hora de modificação
                BasicFileAttributes attr = getFileAttributes(originalFile);
                DateFormat df = new SimpleDateFormat("MM_dd_yyyy");
                String dateCreated = df.format(attr.creationTime().toMillis());
                String newFileName = fileName + "_" + dateCreated;

                try {
                    //verificando se destino existe, se existir criar backup
                    if (graphmlModelFileExist(copyDestination)) {
                        Path fileCopyPath = filePathForModel(copyDestination, newFileName);
                        Files.copy(originalFile, fileCopyPath, COPY_ATTRIBUTES);
                    }

                } catch (IOException xin) {
                    System.out.println("Pasta de destino de backup inexistente");
                    System.out.println("Exception" + xin);
                }
            }
        } catch (IOException xout) {
            System.out.println("Não existe nenhum modelo de nome:" + fileName + " na pasta de origem");
            System.out.println("Exception" + xout);
        }
    }

    public boolean deleteModelFile(Path filePath) {

        System.out.println("deletando documendo:" + filePath.toString());

        boolean output;
        try {
            Files.delete(filePath);
            output = true;
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", filePath);
            output = false;
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", filePath);
            output = false;
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
            output = false;
        }

        return output;
    }

    public boolean writingOnFile(Path filePath) {

        // codificação do arquivo com modelo charset=us-ascii
        Charset charset = Charset.forName("US-ASCII");
        String s = ...;
try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        return true;
    }

}
