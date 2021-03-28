package Engine;

import java.util.HashMap;

public class TrieNode {
    private final HashMap<Character, TrieNode> children = new HashMap<>();
    private boolean endOfWord;
    private Word word;
    private final String content;

    public TrieNode() {
        this.endOfWord = false;
        this.word = null;
        this.content = "";
    }

    public TrieNode(String content) {
        this.endOfWord = false;
        this.word = null;
        this.content = content;
    }

    public HashMap<Character, TrieNode> getChildren() {
        return children;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Word getWord() {
        return word;
    }

    public String getContent() {
        return content;
    }
}
