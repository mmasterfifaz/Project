// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileUpload.java

package com.ntier.utils.file;

import java.util.ArrayList;
//import org.richfaces.event.UploadEvent;
//import org.richfaces.model.UploadItem;

// Referenced classes of package com.ntier.utils.file:
//            File

public class FileUpload
{

    public FileUpload()
    {
        files = new ArrayList();
    }

//    public void listener(UploadEvent event)
//        throws Exception
//    {
//        UploadItem item = event.getUploadItem();
//        File file = new File();
//        file.setLength(item.getData().length);
//        file.setName(item.getFileName());
//        file.setData(item.getData());
//        files.add(file);
//    }

    private ArrayList files;
}
