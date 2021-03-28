package Engine;

import java.util.Vector;

public class Dictionary {
    private final TrieNode root;

    public Dictionary() {
        root = new TrieNode();
    }

    public void insert(Word newWord) {
        TrieNode current = root;
        for (char i : newWord.getWord_target().toCharArray()) {
            String finalContent = current.getContent() + i;
            current.getChildren().computeIfAbsent(i, c -> new TrieNode(finalContent));
            current = current.getChildren().get(i);
        }
        current.setEndOfWord(true);
        current.setWord(newWord);
    }

    public Word find(String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (current.getChildren().isEmpty() || !current.getChildren().containsKey(ch)) {
                // System.out.println("Stop at : " + i + " as \"" + word.charAt(i) + "\"\n");
                return new Word(word , "***");
            } else {
                current = current.getChildren().get(ch);
            }
        }
        if (!current.isEndOfWord()) {
            return new Word(word, "***");
        }
        return current.getWord();
    }

    public void delete(String deletingWord) {
        deleteNode(root, deletingWord, 0);
    }

    private boolean deleteNode(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                // System.out.println("Deleting word is not found !");
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        TrieNode node = current.getChildren().get(ch);
        if (node == null) {
            // System.out.println("Deleting word is not found !");
            return false;
        }
        boolean deleteCurrentNode = deleteNode(node, word, index + 1) && !node.isEndOfWord();

        if (deleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }

    public Vector<Word> getAllWords() {
        Vector<Word> allWords = new Vector<>();
        allWords = getAllAt(root, allWords);
        return allWords;
    }

    public Vector<Word> getSearcherResult(String searchingWord) {
        Vector<Word> searchResult = new Vector<>();
        TrieNode current = root;
        for (int i = 0; i < searchingWord.length(); i++) {
            char ch = searchingWord.charAt(i);
            if (!current.getChildren().isEmpty() && current.getChildren().containsKey(ch)) {
                current = current.getChildren().get(ch);
            } else {
                return searchResult;
            }
        }
        searchResult = getAllAt(current, searchResult);
        return searchResult;
    }

    private Vector<Word> getAllAt(TrieNode current, Vector<Word> allWords) {
        if (current.isEndOfWord()) {
            allWords.add(current.getWord());
        }

        if (current.getChildren().containsKey(' ')) {
            allWords = getAllAt(current.getChildren().get(' '), allWords);
        }
        for (char i = 'a'; i <= 'z'; ++ i) {
            if (current.getChildren().containsKey(i)) {
                allWords = getAllAt(current.getChildren().get(i), allWords);
            }
        }
        if (current.getChildren().containsKey('-')) {
            allWords = getAllAt(current.getChildren().get('-'), allWords);
        }

        return allWords;
    }
}

