package Engine;

import java.io.*;
import java.util.Vector;
import java.sql.*;

public class Management {
    private final Dictionary dictionary = new Dictionary();

    public void insertFromFiles () {
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader("dictionaries.txt"));

            String textInALine;

            // Read from file one line to others.
            while ((textInALine = br.readLine()) != null) {
                String[] s = textInALine.split("\t", 2);
                String[] arr = new String[2];
                int i = 0;
                for (String separate : s) {
                    arr[i] = separate;
                    i++;
                }
                Word newWord = new Word();
                newWord.setWord_target(arr[0]);
                newWord.setWord_explain(arr[1]);
                dictionary.insert(newWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert br != null;
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void exportToFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("dictionaries.txt"));
        Vector<Word> allWords = dictionary.getAllWords();
        for (Word allWord : allWords) {
            bw.write(allWord.getWord_target() + "\t" + allWord.getWord_explain() + "\n");
        }
        bw.close();
    }

    public Connection connect() {
        // SQLite connection string.
        String url = "jdbc:sqlite:dict_hh.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * get data of some columns from board av
     */
    public void selectAll() {
        String sql = "SELECT word, description FROM av";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {
                Word newWord = new Word();
                newWord.setWord_target(rs.getString("word"));
                newWord.setWord_explain(rs.getString("description"));
                dictionary.insert(newWord);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertDB(Word newWord) {
        String sql = "INSERT INTO av(word, description) VALUES(?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, newWord.getWord_target());
            preparedStatement.setString(2, newWord.getWord_explain());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDB(Word oldWord) {
        String sql = "DELETE FROM av WHERE word = ?";
        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            // set the corresponding param
            preparedStatement.setString(1, oldWord.getWord_target());
            // execute the delete statement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Vector<Word> getAllWords() {
        return dictionary.getAllWords();
    }

    public Word getWord(String word) {
        return dictionary.find(word);
    }

    public void addWord(Word newWord) {
        dictionary.insert(newWord);
        insertDB(newWord);
    }

    public void editWord(Word oldWord, Word newWord) {
        dictionary.delete(oldWord.getWord_target());
        deleteDB(oldWord);
        dictionary.insert(newWord);
        insertDB(newWord);
    }

    public void deleteWord(Word deletingWord) {
        dictionary.delete(deletingWord.getWord_target());
        deleteDB(deletingWord);
    }

    public Vector<Word> searcher(String searchingWord) {
        return dictionary.getSearcherResult(searchingWord);
    }
}
