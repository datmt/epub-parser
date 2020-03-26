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
import java.rmi.server.ExportException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Runner {


    public static void main(String[] args) {


        File epubFolder = new File("C:\\books");

        File[] books = epubFolder.listFiles();

        System.out.println("Got " + books.length + " books");

        for (File f : books)
        {
            if (f.getName().endsWith(".epub"))
            {

                try {


                    Book book = EpubHelper.getBook(f.getAbsolutePath());

                    String text = EpubHelper.readBookToString(book);

                    String source = book.getTitle() + " - " + book.getMetadata().getAuthors().toString();

                    for (String p : WordHelpers.textToParagraphs(text))
                    {

                        if (p.length() > 125)
                        {
                            System.out.println("inserting paragraph: " + p);
                            try {
                                DB.insertParagraph(p, source);
                            } catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }

                        }
                    }

                    for (String s : WordHelpers.textToSentences(text))
                    {

                        if (s.length() > 25)
                        {
                            System.out.println("inserting sentence: " + s);
                            try {
                                DB.insertSentence(s, source);
                            } catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }

                    }

                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }

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
