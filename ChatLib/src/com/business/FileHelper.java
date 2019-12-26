/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.business;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author leanh
 */
public class FileHelper {

    private String directory;
    private final byte[] BUFFER = new byte[100 * 1024];

    public FileHelper() {
    }

    /**
     *
     * @param imageDir directory to save file
     */
    public FileHelper(String imageDir) {
        this.directory = imageDir;
    }

    /**
     *
     * @param file to check
     * @return true if file is an image
     * @throws java.io.IOException if not able to read
     */
    public boolean isSupportImage(File file) throws IOException {
        return ImageIO.read(file) != null;
    }

    /**
     *
     * @param os socket.getOuputStream
     * @param file file file object to save
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void streamFileToOuput(OutputStream os, File file) throws
        FileNotFoundException, IOException {
        try (BufferedInputStream bfis = new BufferedInputStream(
            new FileInputStream(file.getAbsolutePath()))) {
            int count;

            while ((count = bfis.read(BUFFER)) != -1) {
                os.write(BUFFER, 0, count);
            }
            os.flush();
            bfis.close();
        }

    }

    /**
     *
     * @param is socket.getInputStream
     * @param file file object to save
     * @param imageAbsolutePath
     * @return true if save completely
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean saveStreamFile(InputStream is, File file, String imageAbsolutePath)
        throws FileNotFoundException, IOException {
        creatDirIfNotExisted();
        try (BufferedOutputStream bos
            = new BufferedOutputStream(new FileOutputStream(imageAbsolutePath))) {
            int byteReads;
            long fileLength = file.length();

            while ((byteReads = is.read(BUFFER)) != -1) {
                bos.write(BUFFER, 0, byteReads);
                fileLength -= byteReads;
                if (fileLength <= 0) {
                    break;
                }
            }
            bos.close();

        }

        return true;

    }

    /**
     *
     * @return true if success make directory
     * @throws IOException
     */
    public boolean creatDirIfNotExisted() throws IOException {
        return new File(directory).mkdirs();
    }

    /**
     *
     * @return this directory of this object
     */
    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public boolean isExistFileInDir(String path) throws IOException {
        File file = new File(path);

        if (file.isDirectory()) {
            return file.list().length > 0;
        } else {
            creatDirIfNotExisted();
            return false;
        }
    }

    public boolean isExistFileInDir() throws IOException {
        return isExistFileInDir(directory);
    }

    public void removeExistFile(String path) {
        File[] files = new File(path).listFiles();
        for (File file : files) {
            file.delete();
        }
    }

    public void removeExistFile() {
        removeExistFile(directory);
    }

    /**
     *
     * @return this return file's name of the first file in the current directory
     */
    public String getFile() {
        return directory + File.separatorChar + new File(directory).list()[0];
    }

    /**
     *
     * @param name is name of the file
     * @return file extension
     */
    public String getFileExtension(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

}
