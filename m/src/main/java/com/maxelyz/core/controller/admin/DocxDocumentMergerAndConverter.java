/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.core.controller.admin;

/**
 *
 * @author kamalast
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
 
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
 
/**
 * @author Dhananjay Jha
 *
 */
public class DocxDocumentMergerAndConverter {
	/**
	 * Takes file path as input and returns the stream opened on it
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public InputStream loadDocumentAsStream(String filePath) throws IOException{
		//URL url =new File(filePath).toURL();
		URL url =new File(filePath).toURI().toURL();
		InputStream documentTemplateAsStream=null;
		documentTemplateAsStream= url.openStream();
		return documentTemplateAsStream;
	}
	/**
	 * Loads the docx report 
	 * @param documentTemplateAsStream
	 * @param freemarkerOrVelocityTemplateKind
	 * @return
	 * @throws IOException
	 * @throws XDocReportException
	 */
	public IXDocReport loadDocumentAsIDocxReport(InputStream documentTemplateAsStream, TemplateEngineKind freemarkerOrVelocityTemplateKind) throws IOException, XDocReportException{
		IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(documentTemplateAsStream, freemarkerOrVelocityTemplateKind);
		return xdocReport;
	}
	/**
	 * Takes the IXDocReport instance, creates IContext instance out of it and puts variables in the context 
	 * @param report
	 * @param variablesToBeReplaced
	 * @return
	 * @throws XDocReportException
	 */
	public IContext replaceVariabalesInTemplateOtherThanImages(IXDocReport report, Map<String, Object> variablesToBeReplaced) throws XDocReportException{
		IContext context = report.createContext();
		for(Map.Entry<String, Object> variable: variablesToBeReplaced.entrySet()){
			context.put(variable.getKey(), variable.getValue());
		}
		return context;
	}
	/**
	 * Takes Map of image variable name and filepath of the image to be replaced. Creates IImageprovides and adds the variable in context
	 * @param report
	 * @param variablesToBeReplaced
	 * @param context
	 */
	public void replaceImagesVariabalesInTemplate(IXDocReport report, Map<String, String> variablesToBeReplaced, IContext context){
 
		 FieldsMetadata metadata = new FieldsMetadata();
         for(Map.Entry<String, String> variable: variablesToBeReplaced.entrySet()){
                 metadata.addFieldAsImage(variable.getKey());
                 context.put(variable.getKey(), new FileImageProvider(new File(variable.getValue()),true));
         }
         report.setFieldsMetadata(metadata);
 
	}
	/**
	 * Generates byte array as output from merged template
	 * @param report
	 * @param context
	 * @return
	 * @throws XDocReportException
	 * @throws IOException
	 */
	public byte[] generateMergedOutput(IXDocReport report,IContext context ) throws XDocReportException, IOException{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		report.process(context, outputStream);
		return outputStream.toByteArray();
	}
	/**
	 * Takes inputs and returns merged output as byte[]
	 * @param templatePath
	 * @param templateEngineKind
	 * @param nonImageVariableMap
	 * @param imageVariablesWithPathMap
	 * @return
	 * @throws IOException
	 * @throws XDocReportException
	 */
	public byte[] mergeAndGenerateOutput(String templatePath, TemplateEngineKind templateEngineKind, Map<String, Object> nonImageVariableMap,Map<String, String> imageVariablesWithPathMap ) throws IOException, XDocReportException{
		InputStream inputStream = loadDocumentAsStream(templatePath);
		IXDocReport xdocReport = loadDocumentAsIDocxReport(inputStream,templateEngineKind);
		IContext context = replaceVariabalesInTemplateOtherThanImages(xdocReport,nonImageVariableMap);
		replaceImagesVariabalesInTemplate(xdocReport, imageVariablesWithPathMap, context);
		byte[] mergedOutput = generateMergedOutput(xdocReport, context); 
		return mergedOutput;
	}
}