package sample;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.jsoup.Jsoup;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

public class EpubHelper {

    public static String readEpub(String epubPath)
    {
        StringBuilder textBuilder = new StringBuilder();
        try {
            EpubReader reader = new EpubReader();

            Book book = reader.readEpub(new FileInputStream(epubPath));


            for (Resource r: book.getContents())
            {
                String chapText = (new String(r.getData(), StandardCharsets.UTF_8));

                textBuilder.append(Jsoup.parse(chapText).text());
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return textBuilder.toString();

    }
}
