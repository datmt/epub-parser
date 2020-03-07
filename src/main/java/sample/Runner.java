package sample;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Runner {


    public static void main(String[] args) {


        File epubFolder = new File("/Users/myn/Desktop/epub");

        File[] books = epubFolder.listFiles();

        for (File f : books)
        {
            if (f.getName().endsWith(".epub"))
            {
                String text = EpubHelper.readEpub(f.getAbsolutePath());
                updateWC(text);
            }
        }




    }

    public static void updateWC(String text)
    {
        HashMap<String, Integer> wordsMap;

        try {
            wordsMap = DB.getAllWords();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return;
        }

        ArrayList<String> words = WordHelpers.textToWords(text);

        for (String x : words)
        {

            try {
                System.out.println("putting: " + x);
                Integer wordCount = wordsMap.get(x);
                if (wordCount == null)
                {
                    wordsMap.put(x, 1);
                } else
                {
                    wordsMap.put(x, wordCount + 1);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            DB.massInsert(wordsMap);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
