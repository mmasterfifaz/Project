/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maxelyz.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author User
 */
public class MergePDF {
    
    public static void mergeFiles(List<InputStream> list, OutputStream outputStream)
            throws DocumentException, IOException {
        Document document = new Document();
        //PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        PdfCopy copy = new PdfCopy(document, outputStream);
        copy.setMergeFields();
        document.open();
         List<PdfReader> readers = new ArrayList<PdfReader>();
         for (InputStream in : list) {
             PdfReader reader = new PdfReader(in);
        readers.add(reader);
        copy.addDocument(reader);
         }
         document.close();
    for (PdfReader reader : readers) {
        reader.close();
    }
        //PdfContentByte cb = writer.getDirectContent();
//        PdfContentByte cb = copy.getDirectContent();
//        
//        for (InputStream in : list) {
//            PdfReader reader = new PdfReader(in);
//            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//                document.newPage();
//                PdfImportedPage page = copy.getImportedPage(reader, i);
//                cb.addTemplate(page, 0, 0);
//            }
//        }
        
//        outputStream.flush();
//        try{
//        document.close();
//        }
//        catch (Exception ex)
//        {
//            System.out.println(ex);
//        }
//        outputStream.close();
    }
}
