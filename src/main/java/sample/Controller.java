package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.HashMap;

public class Controller {

    @FXML
    TextArea wordInputTA;

    public void getWords()
    {
        String text = wordInputTA.getText();
        String[] words = text.split("\\s+");

        try
        {
            HashMap<String, Integer> currentWords = DB.getAllWords();

            for (String w : words)
            {
                String rawWord = w.replaceAll("[^\\w]", "").toLowerCase();
                if(currentWords.containsKey(rawWord))
                {
                    int currentWordFrequency = currentWords.get(rawWord);
                    DB.updateWordFrequency(rawWord, currentWordFrequency);
                    currentWords.put(rawWord, currentWordFrequency + 1);
                } else
                {
                    DB.insertWord(rawWord);
                    currentWords.put(rawWord, 0);
                }
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
