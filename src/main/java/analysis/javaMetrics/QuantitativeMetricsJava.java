package analysis.javaMetrics;

import analysis.interfaces.JavaMetric;
import module.ConfigurationModuleClass;
import module.ConfigurationModuleImpl;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuantitativeMetricsJava  implements JavaMetric  {

    static public String getBlankStrCount(String filePathName) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        int blankStrCount=0;

        for (String str:lines)
        {
            if (StringUtils.isBlank(str))
            {
                blankStrCount++;
            }
        }
        return "Number of blank lines:"+blankStrCount;
    }

    static public String getCommentsCount(String filePathName) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        int commentsCount=0;
        boolean multilineCommentClosed=true;
        Pattern pattern1 = Pattern.compile("^.*\".*//.*\".*$");
        Pattern pattern2 = Pattern.compile("^.*\".*/\\*.*\".*$");
        for (String str:lines)
        {
            if (multilineCommentClosed)
            {
                if (str.contains("/*"))
                {
                    if ( !pattern2.matcher(str).matches())
                    {
                        multilineCommentClosed=false;
                        commentsCount++;
                    }
                }
                else
                if (str.contains("//"))
                {
                    if ( !pattern1.matcher(str).matches())
                    {
                        commentsCount++;
                    }
                }
            }
            else
            if (!multilineCommentClosed)
            {
                if (str.contains("*/"))
                {
                    multilineCommentClosed=true;
                }
            }
        }
        return "Number of comments:"+commentsCount;
    }
    
    static public String getRatioOfComments(String filePathName) throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        int commentsRowCount=0;
        boolean multilineCommentClosed=true;
        Pattern pattern1 = Pattern.compile("^.*\".*//.*\".*$");
        Pattern pattern2 = Pattern.compile("^.*\".*/\\*.*\".*$");
        for (String str:lines)
        {
            if (multilineCommentClosed)
            {
                if (str.contains("/*"))
                {
                    if ( !pattern2.matcher(str).matches())
                    {
                        multilineCommentClosed=false;
                        commentsRowCount++;
                    }
                }
                else
                if (str.contains("//"))
                {
                    if ( !pattern1.matcher(str).matches())
                    {
                        commentsRowCount++;
                    }
                }
            }
            else
            if (!multilineCommentClosed)
            {
                if (str.contains("*/"))
                {
                    multilineCommentClosed=true;
                }
                commentsRowCount++;
            }
        }

        return "The comments in the code make up"+commentsRowCount*100/lines.size()+"%";
    }
}