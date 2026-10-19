package main.java;

public class MapFile{
    public String fileName;
    public String extension;
    public byte[] fileBytes;

    public MapFile(){
        
    }

    public MapFile(String fileName, String extension, byte[] fileBytes){
        this.extension=extension;
        this.fileName=fileName;
        this.fileBytes=fileBytes;
    }

    public void setFilename(String fileName){
        this.fileName=fileName;
    }
    public void setExtension(String extension){
        this.extension=extension;
    }
    public void setFileBytes(byte[] fileBytes){
        this.fileBytes=fileBytes;
    }

    public String getFileName(){
        return fileName;
    }

    public String getExtension(){
        return extension;
    }
    public byte[] getFileBytes(){
        return fileBytes;
    }
}