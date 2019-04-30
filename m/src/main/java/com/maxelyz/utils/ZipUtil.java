/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author apichatt
 */
public class ZipUtil {

    private static Log log = LogFactory.getLog(ZipUtil.class);

    public static byte[] readFile(String f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte b[] = new byte[fis.available()];
        fis.read(b);
        fis.close();
        return b;
    }

    public static void writeFile(String f, byte[] b) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(b);
        fos.close();
    }

    public static int zipFolder(String sourcePath, String zipName, String outPath) {

        File dir = new File(sourcePath);
        if (!dir.exists()) {
            System.out.println("zipFolder > path not found > " + sourcePath);
            return 0;
        }

        String[] children = dir.list();

        FileOutputStream fo = null;
        ZipOutputStream zo = null;
        try {
            fo = new FileOutputStream(outPath + File.separator + zipName);
            zo = new ZipOutputStream(fo);

            for (int i = 0; i < children.length; i++) {
                System.out.println(sourcePath + children[i]);
                byte b[] = readFile(sourcePath + children[i]);
                zo.putNextEntry(new ZipEntry(children[i]));
                zo.write(b);
                zo.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (zo != null) {
                    zo.close();
                }
            } catch (Exception e) {
                return 0;
            }
            try {
                if (fo != null) {
                    fo.close();
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 1;
    }
}
