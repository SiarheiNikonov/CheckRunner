package ru.clevertec;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.*;
import java.net.URL;

public class FileLoader extends DefaultTask {
    public String fileUrl;
    public String outDirectory;

    @TaskAction
    public void download() {
        try {
            downloadUsingStream(fileUrl, outDirectory);
        }catch( Exception e) {
            e.printStackTrace();
        }
    }

    @Input
    public String getFileUrl() {
        return fileUrl;
    }
    @OutputDirectory
    public String getOutDirectory() {
        return outDirectory;
    }

    private static void downloadUsingStream(String urlStr, String file) throws IOException{
        URL url = new URL(urlStr);
        String[] strings = url.getPath().split("/");
        String fileName = strings[strings.length-1];
        File f = new File(file, fileName);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(f);
        byte[] buffer = new byte[1024];
        int count;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }
}
