package com.maxelyz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * Created by apichatt on 7/9/2559.
 */
public class Zip4jUtil {

    public static int zipFolder(String sourcePath, String zipName, String outPath, String password) {
        try {
            //This is name and path of zip file to be created
            ZipFile zipFile = new ZipFile(outPath+zipName);

            //Add files to be archived into zip file
            ArrayList<File> filesToAdd = new ArrayList<File>();
            //filesToAdd.add(new File("C:/tmp/test1.txt"));
            //filesToAdd.add(new File("C:/tmp/test2.txt"));

            File dir = new File(sourcePath);
            if (!dir.exists()) {
                System.out.println("zipFolder > path not found > " + sourcePath);
                return 0;
            }
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                System.out.println(sourcePath + children[i]);
                filesToAdd.add(new File(sourcePath + children[i]));
            }

            //Initiate Zip Parameters which define various properties
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression

            //DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
            //DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
            //DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
            //DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
            //DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            //Set the encryption flag to true
            parameters.setEncryptFiles(true);

            //Set the encryption method to AES Zip Encryption
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

            //AES_STRENGTH_128 - For both encryption and decryption
            //AES_STRENGTH_192 - For decryption only
            //AES_STRENGTH_256 - For both encryption and decryption
            //Key strength 192 cannot be used for encryption. But if a zip file already has a
            //file encrypted with key strength of 192, then Zip4j can decrypt this file
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

            //Set password
            parameters.setPassword(password);

            //Now add files to the zip file
            zipFile.addFiles(filesToAdd, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int zipFolderStream(ZipOutputStream outputStream, String sourcePath, String password) {

        InputStream inputStream = null;

        try {
            //This is name and path of zip file to be created

            //Add files to be archived into zip file
            ArrayList<File> filesToAdd = new ArrayList<File>();
            //filesToAdd.add(new File("C:/tmp/test1.txt"));
            //filesToAdd.add(new File("C:/tmp/test2.txt"));

            File dir = new File(sourcePath);
            if (!dir.exists()) {
                System.out.println("zipFolder > path not found > " + sourcePath);
                return 0;
            }
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                System.out.println("zip file : " + sourcePath + children[i]);
                filesToAdd.add(new File(sourcePath + children[i]));
            }

            //Initiate Zip Parameters which define various properties
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression

            //DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
            //DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
            //DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
            //DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
            //DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            //Set the encryption flag to true
            parameters.setEncryptFiles(true);

            //Set the encryption method to AES Zip Encryption
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

            //AES_STRENGTH_128 - For both encryption and decryption
            //AES_STRENGTH_192 - For decryption only
            //AES_STRENGTH_256 - For both encryption and decryption
            //Key strength 192 cannot be used for encryption. But if a zip file already has a
            //file encrypted with key strength of 192, then Zip4j can decrypt this file
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);

            //Set password
            parameters.setPassword(password);

            //Now add files to the zip file
            for (int i = 0; i < filesToAdd.size(); i++) {
                File file = (File)filesToAdd.get(i);

                //This will initiate ZipOutputStream to include the file
                //with the input parameters
                outputStream.putNextEntry(file,parameters);

                //If this file is a directory, then no further processing is required
                //and we close the entry (Please note that we do not close the outputstream yet)
                if (file.isDirectory()) {
                    outputStream.closeEntry();
                    continue;
                }

                //Initialize inputstream
                inputStream = new FileInputStream(file);
                byte[] readBuff = new byte[4096];
                int readLen = -1;

                //Read the file content and write it to the OutputStream
                while ((readLen = inputStream.read(readBuff)) != -1) {
                    outputStream.write(readBuff, 0, readLen);
                }

                //Once the content of the file is copied, this entry to the zip file
                //needs to be closed. ZipOutputStream updates necessary header information
                //for this file in this step
                outputStream.closeEntry();

                inputStream.close();
            }

            //ZipOutputStream now writes zip header information to the zip file
            outputStream.finish();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

}
