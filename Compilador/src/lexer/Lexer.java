package lexer;

import Environment.Ambiente;
import Environment.id.Identificador;
import carregarArquivo.BaseTXT;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class Lexer
{
  private int n_linha = 1;
  private static int linha_identificador = 1;
  private char ch = ' ';
  private char chAnterior = ' ';
  private final BaseTXT baseTXT;
  private static Identificador idVazio;
  private final ArrayList<Integer> palavrasReservadas;
  private final ArrayList<Token> tokenPalavraReservada;
  private final ArrayList<String> listaDeErros;
  private static final String COMENTARIO = "COMENTARIO";
  private static final String LITERAL = "LITERAL";
  
  public Lexer(BaseTXT baseTXT)
  {
    idVazio = new Identificador(0);
    this.listaDeErros = new ArrayList();
    this.palavrasReservadas = new ArrayList();
    this.tokenPalavraReservada = new ArrayList();
    Ambiente ambiente = new Ambiente();
    
    inserirPalavrasReservadas();
    this.baseTXT = baseTXT;
  }
  
  private void inserirPalavrasReservadas()
  {
    reserve(new Word("start", 256));
    reserve(new Word("exit", 257));
    reserve(new Word("int", 269));
    reserve(new Word("float", 270));
    reserve(new Word("string", 271));
    reserve(new Word("if", 258));
    reserve(new Word("then", 259));
    reserve(new Word("else", 260));
    reserve(new Word("end", 261));
    reserve(new Word("do", 262));
    reserve(new Word("while", 263));
    reserve(new Word("scan", 264));
    reserve(new Word("print", 265));
    reserve(new Word("not", 266));
    reserve(new Word("and", 268));
    reserve(new Word("or", 267));
    this.palavrasReservadas.add(Integer.valueOf(256));
    this.palavrasReservadas.add(Integer.valueOf(257));
    this.palavrasReservadas.add(Integer.valueOf(269));
    this.palavrasReservadas.add(Integer.valueOf(270));
    this.palavrasReservadas.add(Integer.valueOf(271));
    this.palavrasReservadas.add(Integer.valueOf(258));
    this.palavrasReservadas.add(Integer.valueOf(259));
    this.palavrasReservadas.add(Integer.valueOf(260));
    this.palavrasReservadas.add(Integer.valueOf(261));
    this.palavrasReservadas.add(Integer.valueOf(262));
    this.palavrasReservadas.add(Integer.valueOf(263));
    this.palavrasReservadas.add(Integer.valueOf(264));
    this.palavrasReservadas.add(Integer.valueOf(265));
    this.palavrasReservadas.add(Integer.valueOf(266));
    this.palavrasReservadas.add(Integer.valueOf(268));
    this.palavrasReservadas.add(Integer.valueOf(267));
  }
  
  private static void reserve(Word t)
  {
    Ambiente.table.put(t, idVazio);
  }
  
  private void readch()
    throws IOException
  {
    this.ch = this.baseTXT.readch();
  }
  
  private boolean readch(char c)
    throws IOException
  {
    readch();
    if (this.ch != c) {
      return false;
    }
    this.ch = ' ';
    return true;
  }
  
  public int getN_linha()
  {
    return this.n_linha;
  }
  
  public static int getLinhaID()
  {
    return linha_identificador;
  }
  
  private void desconsideraDelimitadores()
    throws IOException
  {
    for (;; readch()) {
      if ((this.ch != ' ') && (this.ch != '\t') && (this.ch != '\r') && (this.ch != '\b'))
      {
        if (this.ch != '\n') {
          break;
        }
        this.n_linha += 1;
      }
    }
  }
  
  public Token tratarNumeros()
    throws IOException
  {
    String numero = "";
    int value = 0;
    while (Character.isDigit(this.ch))
    {
      if (Character.isDigit(this.ch)) {
        numero = numero + this.ch;
      }
      readch();
    }
    if (this.ch == '.')
    {
      numero = numero + this.ch;
      readch();
      do
      {
        numero = numero + this.ch;
        readch();
      } while (Character.isDigit(this.ch));
      float valor = Float.parseFloat(numero);
      
      Word w = new Word(String.valueOf(valor), 292);
      Ambiente.table.put(w, idVazio);
      return w;
    }
    value = Integer.parseInt(numero);
    
    Word w = new Word(String.valueOf(value), 290);
    Ambiente.table.put(w, idVazio);
    return w;
  }
  
  public Token caracterNaoIdentificado()
  {
    Token t = new Token(this.ch);
    this.ch = ' ';
    return t;
  }
  
  public Token tratarTerminal()
    throws IOException, SyntaxException
  {
    switch (this.ch)
    {
    case '=': 
      if (readch('=')) {
        return Word.eq;
      }
      return Word.atrib;
    case '>': 
      if (readch('=')) {
        return Word.ge;
      }
      return Word.gt;
    case '<': 
      readch();
      if (this.ch == '=') {
        return Word.le;
      }
      if (this.ch == '>') {
        return Word.neq;
      }
      return Word.lt;
    case '+': 
      this.ch = ' ';
      return Word.plus;
    case '-': 
      this.ch = ' ';
      return Word.minus;
    case '*': 
      this.ch = ' ';
      return Word.mult;
    case '/': 
      readch();
      if (this.ch == '/')
      {
        do
        {
          readch();
        } while (this.ch != '\n');
        this.ch = ' ';
        return scan();
      }
      if (this.ch == '*')
      {
        readch();
        desconsideraDelimitadores();
        this.chAnterior = this.ch;
        while ((this.chAnterior != '*') && (this.ch != '/'))
        {
          this.chAnterior = this.ch;
          desconsideraDelimitadores();
          readch();
          if (!this.baseTXT.arquivoLidoPronto()) {
            throw new SyntaxException("COMENTARIO");
          }
        }
        this.ch = ' ';
        return scan();
      }
      return Word.div;
    case '(': 
      this.ch = ' ';
      return Word.ap;
    case ',': 
      this.ch = ' ';
      return Word.vr;
    case ';': 
      this.ch = ' ';
      return Word.pvr;
    case '.': 
      this.ch = ' ';
      return Word.pt;
    case ')': 
      this.ch = ' ';
      return Word.fp;
    }
    return caracterNaoIdentificado();
  }
  
  public Token tratarLiteral()
    throws IOException, SyntaxException
  {
    StringBuffer sb = new StringBuffer();
    do
    {
      sb.append(this.ch);
      readch();
      if (!this.baseTXT.arquivoLidoPronto()) {
        throw new SyntaxException("LITERAL");
      }
    } while (this.ch != '"');
    sb.append(this.ch);
    readch();
    String s = sb.toString();
    return new Word(s, 291);
  }
  
  public Token tratarIdentificador()
    throws IOException
  {
    StringBuffer sb = new StringBuffer();
    do
    {
      sb.append(this.ch);
      readch();
    } while (Character.isLetterOrDigit(this.ch));
    String s = sb.toString();
    Word w = Ambiente.getPeloLexema(s);
    if (w != null) {
      return w;
    }
    w = new Word(s, 294);
    Ambiente.table.put(w, idVazio);
    linha_identificador = this.n_linha;
    
    return w;
  }
  
  public Token scan()
    throws IOException, SyntaxException
  {
    desconsideraDelimitadores();
    if (Character.isDigit(this.ch)) {
      return tratarNumeros();
    }
    if (this.ch == '"') {
      return tratarLiteral();
    }
    if (Character.isLetter(this.ch)) {
      return tratarIdentificador();
    }
    return tratarTerminal();
  }
  
  public void erroLexico(Character caracter, int linha)
  {
    this.listaDeErros.add("O caracter " + caracter + " na linha :" + linha + " não foi reconhecido. ");
  }
  
  public void erroComentario()
  {
    this.listaDeErros.add("O comentario nao foi devidamente encerrado");
  }
  
  public void erroLiteral()
  {
    this.listaDeErros.add("O literal nao foi devidamente encerrado");
  }
  
  public boolean arquivoPronto()
  {
    try
    {
      return this.baseTXT.arquivoLidoPronto();
    }
    catch (IOException e)
    {
      System.out.println("Ocorreu um erro ao ler o arquivo. -> " + e);
    }
    return false;
  }
  
  public void analiseLexica()
  {
    try
    {
      this.baseTXT.escreverArquivo("\t*** TOKENS ***\n", false);
      this.baseTXT.escreverArquivo("\nLinha\tLexema\t\tValor", false);
      do
      {
        Token token = new Token(0);
        try
        {
          token = scan();
          if ((token.toString().matches("\\d+")) && (token.getTag() != 292) && (token.getTag() != 290))
          {
            token.toString();String s = String.valueOf(Character.toChars(token.getTag()));
            
            Character c = Character.valueOf(s.charAt(0));
            erroLexico(c, this.n_linha);
          }
          if (this.palavrasReservadas.contains(Integer.valueOf(token.getTag()))) {
            this.tokenPalavraReservada.add(token);
          }
          String saida = "  " + this.n_linha + "      " + token.toString() + "\t\t" + token.getTag();
          
          this.baseTXT.escreverArquivo(saida, false);
        }
        catch (SyntaxException ex)
        {
          if (ex.getMessage().equals("COMENTARIO")) {
            erroComentario();
          } else if (ex.getMessage().equals("LITERAL")) {
            erroLiteral();
          }
        }
      } while (this.baseTXT.arquivoLidoPronto());
      obterIdentificadores();
    }
    catch (IOException ex)
    {
      System.out.println("Ocorreu um erro ao ler o arquivo. -> " + ex);
    }
  }
  
  private void obterIdentificadores()
  {
    this.baseTXT.escreverArquivo("", false);
    this.baseTXT.escreverArquivo("\t**Identificadores**", false);
    Set<Word> words = Ambiente.gerarHashMap();
    Iterator<Word> iterator = words.iterator();
    while (iterator.hasNext())
    {
      Word palavra = (Word)iterator.next();
      if (palavra.getTag() == 294) {
        this.baseTXT.escreverArquivo(" " + palavra.lexeme, false);
      }
    }
    this.baseTXT.escreverArquivo("\t**Palavras Reservadas**", false);
    while (!this.tokenPalavraReservada.isEmpty()) {
      this.baseTXT.escreverArquivo(" " + ((Token)this.tokenPalavraReservada.remove(0)).toString(), false);
    }
    this.baseTXT.escreverArquivo("\t**Erros**", false);
    while (!this.listaDeErros.isEmpty()) {
      this.baseTXT.escreverArquivo("\t" + ((String)this.listaDeErros.remove(0)).toString(), false);
    }
    this.baseTXT.escreverArquivo("", true);
  }
  
  public void gravarSintatico(StringBuffer stBuffer)
  {
    this.baseTXT.escreverArquivoSintatico(stBuffer);
  }
  
  public void gravarSemantico(StringBuffer stBuffer)
  {
    this.baseTXT.escreverArquivoSemantico(stBuffer);
  }
  
  public void gravarArquivoCodigo(StringBuffer stBuffer)
  {
    this.baseTXT.escreverArquivoCodigo(stBuffer);
  }
  
  
  
}
