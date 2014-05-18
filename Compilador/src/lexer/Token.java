package lexer;

public class Token {

    private final int tag;

    public Token(int t) {
        tag = t;
    }

    public String toString() {
        return "" + tag;
    }

    public int getTag() {
        return tag;
    }

}
