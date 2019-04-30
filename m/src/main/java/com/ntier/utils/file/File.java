// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   File.java

package com.ntier.utils.file;


public class File
{

    public File()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        int indexExtFile = name.lastIndexOf('.');
        if(indexExtFile > 0)
        {
            String extension = name.substring(indexExtFile + 1);
            if("bmp".equals(extension))
                mime = "image/bmp";
            else
            if("jpg".equals(extension))
                mime = "image/jpeg";
            else
            if("gif".equals(extension))
                mime = "image/gif";
            else
            if("png".equals(extension))
                mime = "image/png";
            else
                mime = "image/unknown";
        }
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte data[])
    {
        this.data = data;
    }

    public String getMime()
    {
        return mime;
    }

    public long getLength()
    {
        return length;
    }

    public void setLength(long length)
    {
        this.length = length;
    }

    private String name;
    private String mime;
    private long length;
    private byte data[];
}
