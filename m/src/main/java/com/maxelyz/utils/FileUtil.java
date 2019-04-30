package com.maxelyz.utils;

import java.io.File;
import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

/**
 * Created by apichatt on 7/9/2559.
 */
public class FileUtil {

    /* only java7 */
//    public static int createFolder(String folderName) {
//        Path path = Paths.get(folderName);
//        //if directory exists?
//        if (!Files.exists(path)) {
//            try {
//                Files.createDirectories(path);
//                return 1;
//            } catch (IOException e) {
//                //fail to create directory
//                e.printStackTrace();
//                return -1;
//            }
//        }
//        return 0;
//    }

    public static void createFolder(String folderName) {
        File file = new File(folderName);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created! : " + folderName);
            } else {
                System.out.println("Failed to create directory! : " + folderName);
            }
        }
    }

    /* only java7 */
//    public static void delete(File f) throws IOException {
//        try {
//            if (f.isDirectory()) {
//                for (File c : f.listFiles())
//                    delete(c);
//            }
//            f.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void delete(File file) throws IOException{

        if(file.isDirectory()){

            //directory is empty, then delete it
            if(file.list().length==0){

                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());

            }else{

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

//    public static int deleteFolder(String folderName) {
//        Path path = Paths.get(folderName);
//        //if directory exists?
//        if (Files.exists(path)) {
//            try {
//                Files.deleteIfExists(path);
//                return 1;
//
//            } catch (IOException e) {
//                //fail to create directory
//                e.printStackTrace();
//                return -1;
//            }
//        }
//        return 0;
//    }

    public static boolean deleteRecursive(File path) {
        boolean ret = true;
        if (path.exists()) {
            if (path.isDirectory()) {
                for (File f : path.listFiles()) {
                    ret = ret && deleteRecursive(f);
                }
            }
            return ret && path.delete();
        }
        return ret && path.delete();
    }

}
