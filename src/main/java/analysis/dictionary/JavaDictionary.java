package analysis.dictionary;

import java.util.HashSet;

public class JavaDictionary
{
    static public HashSet<String> getOperatorDictionary()
    {
        HashSet<String> hashSet=new HashSet<String>();
        hashSet.add("instanceof");
        hashSet.add(">>>");
        hashSet.add("--");
        hashSet.add("++");
        hashSet.add(">>");
        hashSet.add("<<");
        hashSet.add(">=");
        hashSet.add("<=");
        hashSet.add("==");
        hashSet.add("!=");
        hashSet.add("&&");
        hashSet.add("||");
        hashSet.add("?:");
        hashSet.add("+=");
        hashSet.add("-=");
        hashSet.add("*=");
        hashSet.add("/=");
        hashSet.add("%=");
        hashSet.add("=");
        hashSet.add(">");
        hashSet.add("&");
        hashSet.add("^");
        hashSet.add("|");
        hashSet.add("<");
        hashSet.add("·");
        hashSet.add("!");
        hashSet.add("~");
        hashSet.add("*");
        hashSet.add("/");
        hashSet.add("+");
        hashSet.add("-");
        hashSet.add("%");
        hashSet.add("(");
        hashSet.add("[");
        return hashSet;
    }
}
