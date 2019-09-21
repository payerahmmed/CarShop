/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exam.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;



/**
 *
 * @author Instructor
 */
public class Upload {

    public static String uploadFile(String path, UploadedFile uploadedFile) {
        try {
            Path folder = Paths.get(path);
            String filename = FilenameUtils.getBaseName(uploadedFile.getFileName());
            String extension = FilenameUtils.getExtension(uploadedFile.getFileName());
            Path file = Files.createTempFile(folder, filename + "-", "." + extension);
            InputStream input = uploadedFile.getInputstream();
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
            String copyFile = file.getFileName().toString();
            return copyFile;
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
