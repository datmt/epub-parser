package sample;

import java.lang.reflect.Array;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class WordHelpers {

    private static final String[] ABBREVIATIONS = {
            "Dr." , "Prof." , "Mr." , "Mrs." , "Ms." , "Jr." , "Ph.D."
    };

    private static boolean hasAbbreviation(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return false;
        }
        for (String w : ABBREVIATIONS) {
            if (sentence.contains(w)) {
                return true;
            }
        }
        return false;
    }

    public static String[] textToParagraphs(String text)
    {
        String[] paraps = text.split("\n");

        return (String[])Arrays.stream(paraps).filter(x -> x.length() > 30).toArray();
    }

    public static ArrayList<String> textToSentences(String text)
    {
        ArrayList<String> sentenceList = new ArrayList<>();
        BreakIterator bi = BreakIterator.getSentenceInstance(Locale.US);
        bi.setText(text);
        int start = bi.first();
        int end = bi.next();
        int tempStart = start;
        while (end != BreakIterator.DONE) {
            String sentence = text.substring(start, end);
            if (! hasAbbreviation(sentence)) {
                sentence = text.substring(tempStart, end);
                tempStart = end;
                sentenceList.add(sentence);
            }
            start = end;
            end = bi.next();
        }
        return sentenceList;


    }

    public static ArrayList<String> textToWords(String text)
    {
        String[] result =  text.trim().split("\\P{L}+");

        return ( ArrayList<String>) Arrays.stream(result).map(String::toLowerCase).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(textToWords("The world's third-largest economy shrank 1.6% in the fourth quarter of 2019 "));


    }

}