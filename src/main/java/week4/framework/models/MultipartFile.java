package week4.framework.models;

public class MultipartFile {
    private final String fileName;
    private final String contentType;
    private final byte[] data;

    public MultipartFile(String fileName, String contentType, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }

    public String getFileName() { return fileName; }
    public String getContentType() { return contentType; }
    public byte[] getData() { return data; }
}