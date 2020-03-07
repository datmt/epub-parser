package sample;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DB {

    private static final String DB_URL = "jdbc:sqlite:/Users/myn/Documents/lab/words.db";

    private static Connection connection;

    public static Connection getConnection() throws SQLException
    {
        if (connection == null || connection.isClosed())
            connection =  DriverManager.getConnection(DB_URL);

        return connection;
    }


    public static boolean checkWordExist(String word) throws SQLException
    {
        Connection connection = getConnection();

        //First check if words exists. If it does, get word count and increase by one
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM words_frequency WHERE word = ?");
        statement.setString(1, word);

        ResultSet set = statement.executeQuery();

        boolean exists = set.next();

        statement.close();
        connection.close();

        return exists;

    }


    public static void massInsert(HashMap<String, Integer> wordsMap)
    {
        for (Map.Entry<String, Integer> entry : wordsMap.entrySet())
        {
            try {

                DB.updateWordFrequency(entry.getKey(), entry.getValue());
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }

    public static HashMap<String, Integer> getAllWords() throws SQLException
    {
        Connection connection = getConnection();
        ResultSet set = connection.createStatement().executeQuery("SELECT * FROM words_frequency");

        HashMap<String, Integer> words = new HashMap<String, Integer>();
        while(set.next())
        {
            words.put(set.getString("word"), set.getInt("frequency"));
        }

        set.close();
        connection.close();

        return words;
    }


    public static void updateWordFrequency(String word, int newCount) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement updateStatement = connection.prepareStatement("UPDATE words_frequency SET frequency = ? WHERE word = ?");
        updateStatement.setInt(1, newCount);
        updateStatement.setString(2, word);
        updateStatement.execute();
        updateStatement.close();
        connection.close();
    }

    public static int getWordFrequency(String word) throws SQLException
    {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT frequency FROM words_frequency WHERE word = ?");
        statement.setString(1, word);
        ResultSet set = statement.executeQuery();
        int result = 0;
        if (set.next())
            result = set.getInt("frequency");
        statement.close();
        connection.close();

        return result;
    }

    public static void insertWord(String word) throws SQLException
    {
        Connection connection = getConnection();

        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO  words_frequency(word) VALUES(?)");
        insertStatement.setString(1, word);
        insertStatement.execute();
        insertStatement.close();

    }

    public static void insertSentence(String sentence, String source) throws SQLException
    {
        Connection connection = getConnection();

        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO  sentences(sentence, source) VALUES(?, ?)");
        insertStatement.setString(1, sentence);
        insertStatement.setString(2, source);
        insertStatement.execute();
        insertStatement.close();
        connection.close();
    }

    public static void insertParagraph(String paragraph, String source) throws SQLException
    {
        Connection connection = getConnection();

        PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO  paragraphs(paragraph, source) VALUES(?, ?)");
        insertStatement.setString(1, paragraph);
        insertStatement.setString(2, source);
        insertStatement.execute();
        insertStatement.close();

        connection.close();
    }

    public static void createTables() throws SQLException
    {
        String createWordTable = "CREATE TABLE IF NOT EXISTS words_frequency(id integer primary key autoincrement, word text not null, frequency integer not null default 1)";
        String createSentencesTable = "CREATE TABLE IF NOT EXISTS sentences(id integer primary key autoincrement, sentence text not null, source text)";
        String createParagraphTable = "CREATE TABLE IF NOT EXISTS paragraphs(id integer primary key autoincrement, paragraph text not null, source text)";

        String[] createQueries = new String[]{
                createWordTable,
                createSentencesTable,
                createParagraphTable
        };

        for (String query :
                createQueries) {
            Statement statement = getConnection().createStatement();
            statement.execute(query);

            statement.close();
        }


    }

    private static void resetDb() throws SQLException
    {
        String dropWordsTable = "DROP TABLE IF EXISTS words_frequency";
        String dropParagraphsTable = "DROP TABLE IF EXISTS paragraphs";
        String dropSentencesTable = "DROP TABLE IF EXISTS sentences";
        String[] dropQs = new String[] {
                dropWordsTable,
                dropParagraphsTable,
                dropSentencesTable
        };

        Connection connection = getConnection();
        for (String dropQ : dropQs)
        {
            connection.createStatement().execute(dropQ);
        }
        connection.close();
        createTables();
    }

    public static void main(String[] args) {
        try {
            resetDb();
            createTables();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
