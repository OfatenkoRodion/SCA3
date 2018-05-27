package analysis.javaMetrics;

import analysis.dictionary.JavaDictionary;
import module.ConfigurationModuleClass;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HalsteadMetricsJava {

    public static String getUniqueOperators(String filePathName) throws IOException {

        HashSet<String> uniqueOperators =  new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        HashSet<String> operatorDictionary=JavaDictionary.getOperatorDictionary();

        lines.forEach(str->{

            operatorDictionary.forEach(operator->
            {
                if(str.contains(operator))
                {
                    uniqueOperators.add(operator);

                }
            });
        });

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Discovered ").append(uniqueOperators.size()).append(" unique operators ");
        uniqueOperators.forEach(str-> stringBuilder.append("[").append(str).append("]"));

        return stringBuilder.toString();
    }

    public static String getNumberOfOperators(String filePathName) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        HashSet<String> operatorDictionary=JavaDictionary.getOperatorDictionary();
        AtomicInteger numberOfOperators= new AtomicInteger();

        lines.forEach(str->{


            operatorDictionary.forEach(operator->
            {
                if(str.contains(operator))
                {

                    numberOfOperators.addAndGet(StringUtils.countMatches(str, operator));
                }
            });
        });

        return "Total detected "+numberOfOperators+" operators";
    }

    public static String getUniqueOperands(String filePathName) throws IOException {

        HashSet<String> uniqueOperands = new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);

        lines.forEach(str-> {

            uniqueOperands.addAll(uniqueOperands(str));

        });

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Discovered ").append(uniqueOperands.size()).append(" unique operands");
        uniqueOperands.forEach(str-> stringBuilder.append("[").append(str).append("]"));

        return stringBuilder.toString();
    }

    public static String getNumberOfOperands(String filePathName) throws IOException {

        AtomicInteger numberOfOperands= new AtomicInteger();

        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);

        lines.forEach(str->{

            numberOfOperands.addAndGet(countOperands(str));

        });

        return "Total detected "+numberOfOperands+" operands";
    }

    public static String getProgramVocabulary(String filePathName) throws IOException {

        HashSet<String> uniqueOperators =  new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        HashSet<String> operatorDictionary=JavaDictionary.getOperatorDictionary();

        lines.forEach(str->{

            operatorDictionary.forEach(operator->
            {
                if(str.contains(operator))
                {
                    uniqueOperators.add(operator);

                }
            });
        });

        HashSet<String> uniqueOperands = new HashSet<>();
        lines.forEach(str-> {

            uniqueOperands.addAll(uniqueOperands(str));

        });

        return "Dictionary of the program: "+(uniqueOperators.size()+uniqueOperands.size());
    }

    public static String getProgramLength(String filePathName) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        HashSet<String> operatorDictionary=JavaDictionary.getOperatorDictionary();
        AtomicInteger numberOfOperators= new AtomicInteger();

        lines.forEach(str->{


            operatorDictionary.forEach(operator->
            {
                if(str.contains(operator))
                {

                    numberOfOperators.addAndGet(StringUtils.countMatches(str, operator));
                }
            });
        });

        AtomicInteger numberOfOperands= new AtomicInteger();

        lines.forEach(str->{

            numberOfOperands.addAndGet(countOperands(str));

        });
        return "Length of the program: "+(numberOfOperands.get()+numberOfOperators.get());
    }

    public static String  getDifficulty(String filePathName) throws IOException {

        HashSet<String> uniqueOperators =  new HashSet<>();
        List<String> lines = Files.readAllLines(Paths.get( ConfigurationModuleClass.folderUrl()+filePathName), StandardCharsets.UTF_8);
        HashSet<String> operatorDictionary=JavaDictionary.getOperatorDictionary();
        AtomicInteger numberOfOperands= new AtomicInteger();

        lines.forEach(str->{

            operatorDictionary.forEach(operator->
            {
                if(str.contains(operator))
                {
                    uniqueOperators.add(operator);

                }
            });
        });

        HashSet<String> uniqueOperands = new HashSet<>();
        lines.forEach(str-> {

            uniqueOperands.addAll(uniqueOperands(str));

        });

        lines.forEach(str->{

            numberOfOperands.addAndGet(countOperands(str));

        });


        return "Complexity of the program: "+((uniqueOperators.size()/2)*(numberOfOperands.get()/uniqueOperands.size()));
    }



    private static int countOperands(String str)
    {
        int numberOfOperands=0;

        final char[] chars = str.toCharArray();

        int a=-1;
        StringBuilder tempSt= new StringBuilder();


        for (int i=0;i<chars.length;i++)
        {
            if (chars[i]=='(')
            {
                if (a>=0)
                {
                    tempSt.append(chars[i]);
                }
                a++;
            }
            else
            {
                if (chars[i]==')')
                {
                    a--;
                    if ( a==-1)
                    {
                        numberOfOperands++;
                    }
                    if (a>=0)
                    {
                        tempSt.append(chars[i]);
                    }
                }
                else
                if ( a==0)
                {
                    if(chars[i]==',')
                    {
                        numberOfOperands++;
                        tempSt.delete(0,tempSt.length());
                    }
                    else
                    {
                        tempSt.append(chars[i]);
                    }
                }
            }
        }

        return numberOfOperands;
    }

    private static HashSet<String> uniqueOperands(String str)
    {
        HashSet<String> uniqueOperands = new HashSet<>();


        final char[] chars = str.toCharArray();
        int a=-1;
        StringBuilder tempSt= new StringBuilder();


        for (int i=0;i<chars.length;i++)
        {
            if (chars[i]=='(')
            {
                if (a>=0)
                {
                    tempSt.append(chars[i]);
                }
                a++;
            }
            else
            {
                if (chars[i]==')')
                {
                    a--;
                    if ( a==-1)
                    {
                        uniqueOperands.add(StringUtils.trim(tempSt.toString()));

                    }
                    if (a>=0)
                    {
                        tempSt.append(chars[i]);
                    }
                }
                else
                if ( a==0)
                {
                    if(chars[i]==',')
                    {
                        uniqueOperands.add(StringUtils.trim(tempSt.toString()));
                        tempSt.delete(0,tempSt.length());
                    }
                    else
                    {
                        tempSt.append(chars[i]);
                    }
                }
            }
        }

        return uniqueOperands;
    }




}
