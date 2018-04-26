package analysis.javaMetrics;

import analysis.dictionary.JavaDictionary;
import analysis.interfaces.JavaMetric;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TheHalsteadMetricsJava implements JavaMetric
{
    private List<String> listFileLines;
    private HashSet<String> operatorDictionary;
    private HashSet<String> uniqueOperators;
    private int numberOfOperators=0;
    private HashSet<String> uniqueOperands;
    private int numberOfOperands=0;

    public void setListFileLines(List<String> lines)
    {
        this.listFileLines = lines;
        runAnalysis();
    }

    public TheHalsteadMetricsJava()
    {
        operatorDictionary= JavaDictionary.getOperatorDictionary();
        uniqueOperators= new HashSet<>();
        uniqueOperands= new HashSet<>();
    }

    public TheHalsteadMetricsJava(String path, String fileName) throws IOException
    {
        this();
        listFileLines = Files.readAllLines(Paths.get(path+fileName), StandardCharsets.UTF_8);
        runAnalysis();
    }
    private void runAnalysis()
    {
        uniqueOperators.clear();

        listFileLines.forEach(str->{

            countOperands(str);

            operatorDictionary.forEach(operator->
            {
                if(str.contains(operator))
                {
                    uniqueOperators.add(operator);

                    numberOfOperators+=StringUtils.countMatches(str, operator);
                }
            });
        });
    }
    private void countOperands(String str)
    {
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
                        uniqueOperands.add(StringUtils.trim(tempSt.toString()));
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
    }
    public String getResultOfAnalysisUniqueOperators()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Discovered ").append(uniqueOperators.size()).append(" unique operators ");

        uniqueOperators.forEach(str-> stringBuilder.append("[").append(str).append("]"));

        return stringBuilder.toString();
    }
    public String getResultOfAnalysisNumberOfOperators()
    {
        return "Total detected "+numberOfOperators+" operators";
    }
    public String getResultOfAnalysisUniqueOperands()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Discovered ").append(uniqueOperands.size()).append(" unique operands");

        uniqueOperands.forEach(str-> stringBuilder.append("[").append(str).append("]"));

        return stringBuilder.toString();
    }
    public String getResultOfAnalysisNumberOfOperands()
    {
        return "Total detected "+numberOfOperands+" operands";
    }
    public String getProgramVocabulary()
    {
        return "Dictionary of the program: "+(uniqueOperators.size()+uniqueOperands.size());
    }
    public String getProgramLength()
    {
        return "Length of the program: "+(numberOfOperands+numberOfOperators);
    }
    public String  getDifficulty()
    {
        return "Complexity of the program: "+((uniqueOperators.size()/2)*(numberOfOperands/uniqueOperands.size()));
    }
}