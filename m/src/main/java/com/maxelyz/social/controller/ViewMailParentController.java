/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.maxelyz.social.controller;

import com.maxelyz.social.model.dao.SoMessageDAO;
import com.maxelyz.social.model.entity.SoMessage;
import com.maxelyz.utils.JSFUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

/**
 *
 * @author ukritj
 */
@ManagedBean
@ViewScoped
public class ViewMailParentController {
    
    public static boolean showStructure = true;
    public static boolean saveAttachments = true;
    public static int attnum = 1;
    
    private Integer soMessageId;
    private SoMessage soMessage;
    private List<String> bodyListParent;
    private List<String[]> contentIdList;
    
    private String emailPath = JSFUtil.getApplication().getEmailFilePath();
    private String separate = JSFUtil.getApplication().getSeparatePath();
    
    @ManagedProperty(value = "#{soMessageDAO}")
    private SoMessageDAO soMessageDAO;
    
    @PostConstruct
    public void initialize() {
        Integer soAccountId = 0;
        Integer soMessageId = 0;
        contentIdList = new ArrayList<String[]>();
        
        if(JSFUtil.getRequestParameterMap("soMessageId") != null && !JSFUtil.getRequestParameterMap("soMessageId").equals("")){
            soMessageId = Integer.parseInt(JSFUtil.getRequestParameterMap("soMessageId"));
        }
        if(soMessageId != null && soMessageId != 0){
            soMessage = soMessageDAO.findSoMessage(soMessageId);
            if(soMessage != null){ 
                if(soMessage.getSoAccount() != null){
                    soAccountId = soMessage.getSoAccount().getId();
                }
            }
        }
        if(soAccountId != 0 && soMessageId != 0){
            try{
                displayEmailParent(new File(emailPath + soAccountId + separate + soMessageId + separate + soMessageId + ".eml"), soAccountId, soMessageId);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private void displayEmailParent(File emlFile, Integer soAccountId, Integer soMessageId) throws Exception{
        InputStream source = new FileInputStream(emlFile);
        MimeMessage msgParent = new MimeMessage(null, source);

        bodyListParent = new ArrayList<String>();
        
        dumpPart(msgParent, bodyListParent, soAccountId, soMessageId);
        
        List<String> sta = new ArrayList<String>();
        for(String body : bodyListParent){
            for(String[] stra : contentIdList){
                if(body.contains(stra[0])){
                    body = body.replace("cid:"+stra[0], (JSFUtil.getRequestContextPath() + "/emailfiles/" + soAccountId + "/" + soMessageId + "/" + stra[1]));
                }
            }
            sta.add(body);
        }
        bodyListParent = sta;
        
        source.close();
    }
    
    private String getFileMap(String strFile){
        String s = "";
        if(soMessage.getSoEmailMessage().getAttachment() != null){
            try{
                String str = soMessage.getSoEmailMessage().getAttachment();
                String strMap = soMessage.getSoEmailMessage().getAttachmentMap();
                if(str.indexOf(",") != -1 && strMap.indexOf(",") != -1){
                    StringTokenizer stk = new StringTokenizer(str, ",");
                    StringTokenizer stkMap = new StringTokenizer(strMap, ",");
                    while (stk.hasMoreTokens() && stkMap.hasMoreTokens()) {
                        String fName = (String) stk.nextToken();
                        String fNameMap = (String) stkMap.nextToken();
                        if(strFile.equals(fName)){
                            s = fNameMap;
                        }
                    }
                }else if(str.indexOf(",") == -1 && strMap.indexOf(",") == -1){
                    if(strFile.equals(str)){
                        s = strMap;
                    }
                }
            }catch(Exception e){
                
            }
        }
        return s;
    }
    
    private String replaceNewLine(String text) {

        if (text == null) {
            return null;
        }

        StringBuilder html = new StringBuilder();

        StringTokenizer st = new StringTokenizer(text, "\r\n", true);

        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (token.equals("\n")) {
                html.append("<br/>");
            } else if (token.equals("\r")) {    
                // Do nothing    
            } else {    
                html.append(token);    
            }
        }

        return html.toString();

    }
    
    private String convertContentId(String cId){
        String str = "";
        if(cId.indexOf("<") != -1 && cId.indexOf(">") != -1){
            str = cId.substring(cId.indexOf("<") + 1, cId.indexOf(">"));
        }
        return str;
    }
    
    public void dumpPart(Part p, List<String> stra, Integer accountId, Integer soMessageId) throws Exception {
	//if (p instanceof Message)
	//    dumpEnvelope((Message)p);

	/** Dump input stream .. 

	InputStream is = p.getInputStream();
	// If "is" is not already buffered, wrap a BufferedInputStream
	// around it.
	if (!(is instanceof BufferedInputStream))
	    is = new BufferedInputStream(is);
	int c;
	while ((c = is.read()) != -1)
	    System.out.write(c);

	**/
        
        String[] sa = p.getHeader("Content-ID");
        if(sa != null){
            String contentId = convertContentId(sa[0]);
            String fileName = p.getFileName() != null ? MimeUtility.decodeText(p.getFileName()) : null;//convertContentIdToFileName(contentId);
            String[] a = new String[2];
            a[0] = contentId;
            a[1] = fileName;
            contentIdList.add(a);
        }
        
	String ct = p.getContentType();
	try {
	    pr("CONTENT-TYPE: " + (new ContentType(ct)).toString());
	} catch (ParseException pex) {
	    pr("BAD CONTENT-TYPE: " + ct);
	}
	String filename = p.getFileName() != null ? MimeUtility.decodeText(p.getFileName()) : null;
	if (filename != null)
	    pr("FILENAME: " + filename);

	/*
	 * Using isMimeType to determine the content type avoids
	 * fetching the actual content data until we need it.
	 */
	if (p.isMimeType("text/plain") && filename == null) {
	    pr("This is plain text");
	    pr("---------------------------");
	    if (showStructure){
		//System.out.println((String)p.getContent());
                stra.add(this.replaceNewLine((String)p.getContent()));
            }
	}else if (p.isMimeType("multipart/*")) {
            if (p.isMimeType("multipart/alternative")) {
                pr("This is a Multipart/Alternative");
                pr("---------------------------");
                Multipart mp = (Multipart) p.getContent();
                level++;
                int count = mp.getCount();
                boolean havePlain = false;
                boolean haveHtml = false;
                boolean haveRelated = false;
                for (int i = 0; i < count; i++) {
                    //System.out.println(mp.getBodyPart(i).getContentType());
                    if (mp.getBodyPart(i).isMimeType("text/plain")) {
                        havePlain = true;
                    }
                    if (mp.getBodyPart(i).isMimeType("text/html")) {
                        haveHtml = true;
                    }
                    if (mp.getBodyPart(i).isMimeType("multipart/related")){
                        haveRelated = true;
                    }
                }
                /*for (int i = 0; i < count; i++) {
                    if (haveHtml && mp.getBodyPart(i).isMimeType("text/html")) {
                        dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                        break;
                    } else if (!haveHtml && havePlain && mp.getBodyPart(i).isMimeType("text/plain")) {
                        dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                        break;
                    }
                }*/
                
                if(haveRelated){
                    for (int i = 0; i < count; i++) {
                        if (mp.getBodyPart(i).isMimeType("multipart/related")) {
                            dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                            break;
                        }
                    }
                } else if(haveHtml){
                    for (int i = 0; i < count; i++) {
                        if (mp.getBodyPart(i).isMimeType("text/html")) {
                            dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                            break;
                        }
                    }
                } else if(havePlain){
                    for (int i = 0; i < count; i++) {
                        if (mp.getBodyPart(i).isMimeType("text/plain")) {
                            dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                            break;
                        }
                    }
                }
                level--;
            } else {
                pr("This is a Multipart");
                pr("---------------------------");
                Multipart mp = (Multipart) p.getContent();
                level++;
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    dumpPart(mp.getBodyPart(i), stra, accountId, soMessageId);
                }
                level--;
            }
	} else if (p.isMimeType("message/rfc822")) {
	    pr("This is a Nested Message");
	    pr("---------------------------");
	    level++;
	    dumpPart((Part)p.getContent(), stra, accountId, soMessageId);
	    level--;
	} else {
            //System.out.println(p.getContentType());
	    if (showStructure) {
		/*
		 * If we actually want to see the data, and it's not a
		 * MIME type we know, fetch it and check its Java type.
		 */
		Object o = p.getContent();
		if ((o instanceof String) && filename == null) {
		    pr("This is a string");
		    pr("---------------------------");
		    //System.out.println((String)o);
                    stra.add((String)o);
		} else if (o instanceof InputStream) {
		    pr("This is just an input stream");
		    pr("---------------------------");
		    InputStream is = (InputStream)o;
		    int c;
		    while ((c = is.read()) != -1)
			System.out.write(c);
		} else {
		    pr("This is an unknown type");
		    pr("---------------------------");
		    pr(o.toString());
                    //stra.add((String)o);
		}
	    } else {
		// just a separator
		pr("---------------------------");
	    }
	}

	/*
	 * If we're saving attachments, write out anything that
	 * looks like an attachment into an appropriately named
	 * file.  Don't overwrite existing files to prevent
	 * mistakes.
	 */
        
	if (saveAttachments && p instanceof MimeBodyPart) {
	    String disp = p.getDisposition();
	    // many mailers don't include a Content-Disposition
            if (disp != null || filename != null){
                //if (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE)) {
                    if (filename == null)
                        filename = "Attachment" + attnum++;
                    pr("Saving attachment to file " + filename);
                    try {
                        //File f = new File(emailPath + accountId + "/" + soMessageId + "/" + filename);
                        if(filename.indexOf(",") != -1){
                            filename = filename.replace(",", "");
                        }
                        File f = new File(emailPath + accountId + separate + soMessageId + separate + filename);
                        if (f.exists())
                            // XXX - could try a series of names
                            throw new IOException("file exists");
                        ((MimeBodyPart)p).saveFile(f);
                    } catch (IOException ex) {
                        pr("Failed to save attachment: " + ex);
                    }
                    pr("---------------------------");
                //}
            }
	}
    }

    public static void dumpEnvelope(Message m) throws Exception {
	pr("This is the message envelope");
	pr("---------------------------");
	Address[] a;
	// FROM 
	if ((a = m.getFrom()) != null) {
	    for (int j = 0; j < a.length; j++)
		pr("FROM: " + a[j].toString());
	}

	// REPLY TO
	if ((a = m.getReplyTo()) != null) {
	    for (int j = 0; j < a.length; j++)
		pr("REPLY TO: " + a[j].toString());
	}

	// TO
	if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
	    for (int j = 0; j < a.length; j++) {
		pr("TO: " + a[j].toString());
		InternetAddress ia = (InternetAddress)a[j];
		if (ia.isGroup()) {
		    InternetAddress[] aa = ia.getGroup(false);
		    for (int k = 0; k < aa.length; k++)
			pr("  GROUP: " + aa[k].toString());
		}
	    }
	}

	// SUBJECT
	pr("SUBJECT: " + m.getSubject());

	// DATE
	Date d = m.getSentDate();
	pr("SendDate: " +
	    (d != null ? d.toString() : "UNKNOWN"));

	// FLAGS
	Flags flags = m.getFlags();
	StringBuffer sb = new StringBuffer();
	Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

	boolean first = true;
	for (int i = 0; i < sf.length; i++) {
	    String s;
	    Flags.Flag f = sf[i];
	    if (f == Flags.Flag.ANSWERED)
		s = "\\Answered";
	    else if (f == Flags.Flag.DELETED)
		s = "\\Deleted";
	    else if (f == Flags.Flag.DRAFT)
		s = "\\Draft";
	    else if (f == Flags.Flag.FLAGGED)
		s = "\\Flagged";
	    else if (f == Flags.Flag.RECENT)
		s = "\\Recent";
	    else if (f == Flags.Flag.SEEN)
		s = "\\Seen";
	    else
		continue;	// skip it
	    if (first)
		first = false;
	    else
		sb.append(' ');
	    sb.append(s);
	}

	String[] uf = flags.getUserFlags(); // get the user flag strings
	for (int i = 0; i < uf.length; i++) {
	    if (first)
		first = false;
	    else
		sb.append(' ');
	    sb.append(uf[i]);
	}
	pr("FLAGS: " + sb.toString());

	// X-MAILER
	String[] hdrs = m.getHeader("X-Mailer");
	if (hdrs != null)
	    pr("X-Mailer: " + hdrs[0]);
	else
	    pr("X-Mailer NOT available");
    }

    static String indentStr = "                                               ";
    static int level = 0;

    /**
     * Print a, possibly indented, string.
     */
    public static void pr(String s) {
	//if (showStructure)
	    //System.out.print(indentStr.substring(0, level * 2));
	//System.out.println(s);
    }

    public Integer getSoMessageId() {
        return soMessageId;
    }

    public void setSoMessageId(Integer soMessageId) {
        this.soMessageId = soMessageId;
    }

    public List<String> getBodyListParent() {
        return bodyListParent;
    }

    public void setBodyListParent(List<String> bodyListParent) {
        this.bodyListParent = bodyListParent;
    }

    public SoMessageDAO getSoMessageDAO() {
        return soMessageDAO;
    }

    public void setSoMessageDAO(SoMessageDAO soMessageDAO) {
        this.soMessageDAO = soMessageDAO;
    }

    public List<String[]> getContentIdList() {
        return contentIdList;
    }

    public void setContentIdList(List<String[]> contentIdList) {
        this.contentIdList = contentIdList;
    }

    public SoMessage getSoMessage() {
        return soMessage;
    }

    public void setSoMessage(SoMessage soMessage) {
        this.soMessage = soMessage;
    }
}
