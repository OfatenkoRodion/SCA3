package analysis;

import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public  class DownloadFile
{
    public static String start(String downloadURL, String path, String newFileName) throws IOException
    {
        URL website = new URL(downloadURL);
        String fileName = newFileName+"."+FilenameUtils.getExtension(downloadURL);

        try (InputStream inputStream = website.openStream())
        {
            Files.copy(inputStream, Paths.get(path+fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        List<String> lines = Files.readAllLines(Paths.get(path+fileName), StandardCharsets.UTF_8);

        return fileName;
    }
}