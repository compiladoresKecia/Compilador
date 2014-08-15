package lexer;

public class Word
  extends Token
{
  public String lexeme = "";
  private String type;
  public static final Word eq = new Word("==", 272);
  public static final Word gt = new Word(">", 273);
  public static final Word ge = new Word(">=", 274);
  public static final Word le = new Word("<=", 275);
  public static final Word lt = new Word("<", 276);
  public static final Word neq = new Word("<>", 277);
  public static final Word plus = new Word("+", 278);
  public static final Word minus = new Word("-", 279);
  public static final Word mult = new Word("*", 280);
  public static final Word div = new Word("/", 281);
  public static final Word atrib = new Word("=", 282);
  public static final Word pt = new Word(".", 283);
  public static final Word vr = new Word(",", 284);
  public static final Word pvr = new Word(";", 285);
  public static final Word ap = new Word("(", 286);
  public static final Word fp = new Word(")", 287);
  public static final Word as = new Word("\"", 288);
  
  public Word(String s, int tag)
  {
    super(tag);
    this.lexeme = new String(s);
  }
  
  public Word(Token token, String s)
  {
    super(token.getTag());
    this.lexeme = new String(s);
  }
  
  public String toString()
  {
    return "" + this.lexeme;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
}