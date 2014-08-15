package Semantics;

import Environment.Ambiente;
import Environment.id.Identificador;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import lexer.Lexer;
import lexer.Word;

public class Semantics
{
  private Word word;
  private Word word1;
  private int tipo;
  private String _tipo;
  private String lexema;
  private static Identificador idVazio;
  private int linha;
  
  public Semantics(Word word, int tipo, String lexema)
  {
    System.out.println("Semantics ---- Tipo: " + tipo);
    System.out.println("Lexema: " + lexema);
    this.word = word;
    this.tipo = tipo;
    this.lexema = lexema;
    idVazio = new Identificador(0);
  }
  
  public String Type()
  {
    System.out.println("Tipo");
    this.word1 = Ambiente.getPeloLexema(this.lexema);
    String erro = null;
    if (this.word1.getType() == null) {
      switch (this.tipo)
      {
      case 269: 
        this._tipo = "INT";
        break;
      case 270: 
        this._tipo = "FLOAT";
        break;
      case 271: 
        this._tipo = "STRING";
        break;
      default: 
        System.out.println("Erro! Tipo nao encontrado");
        erro = "Erro! Tipo nao encontrado";
        break;
      }
    } else {
      erro = Unicity();
    }
    this.word1.setType(this._tipo);
    Ambiente.table.remove(this.word);
    Ambiente.table.put(this.word1, idVazio);
    
    return erro;
  }
  
  public String Absence()
  {
    String erro = null;
    

    this.linha = Lexer.getLinhaID();
    Set<Word> words = Ambiente.gerarHashMap();
    Iterator<Word> iterator = words.iterator();
    System.out.println(this.lexema);
    while (iterator.hasNext())
    {
      Word palavra = (Word)iterator.next();
      if ((palavra.getTag() == 294) && (this.lexema.equals(palavra.toString()))) {
        if (palavra.getType() == null)
        {
          System.out.println("\n\nErro semantico! ");
          System.out.println("\nVariavel [" + palavra.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n");
          

          erro = "\nVariavel [" + palavra.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n";
          return erro;
        }
      }
    }
    return erro;
  }
  
  public String Unicity()
  {
    this.linha = Lexer.getLinhaID();
    System.out.println("-> Tipo ja cadastrado!\n** Identificador: " + this.word1.toString());
    System.out.println("**  Tipo = " + this.word1.getType() + "\n");
    return "-> Tipo ja cadastrado!\n** Identificador: " + this.word1.toString() + "\n**  Tipo = " + this.word1.getType() + "\n";
  }
  
  public Word consultLexemaEnviroment(String lexema)
  {
    Set<Word> words = Ambiente.gerarHashMap();
    Iterator<Word> iterator = words.iterator();
    while (iterator.hasNext())
    {
      Word palavra = (Word)iterator.next();
      if ((palavra.getTag() == 294) && (palavra.toString().equals(lexema))) {
        return palavra;
      }
    }
    return null;
  }
  
  public boolean tagNum(String tag)
  {
    if ((tag.equals("INT")) || (tag.equals("FLOAT")) || (tag.equals("STRING"))) {
      return true;
    }
    return false;
  }
  
  public String tipo(int tipoAnalise)
  {
    switch (tipoAnalise)
    {
    case 290: 
      this._tipo = "INT";
      break;
    case 291: 
      this._tipo = "STRING";
      break;
    case 292: 
      this._tipo = "FLOAT";
    }
    return this._tipo;
  }
  
  public String TypeAssignment(String lexema, int tag)
  {
    String erro = null;
    this.linha = Lexer.getLinhaID();
    if (this.word.getTag() == 294) {
      this.word = consultLexemaEnviroment(this.word.toString());
    }
    if (lexema != null)
    {
      System.out.println("Word leftside: " + lexema + "\n Word :" + this.word.toString());
      Word leftSide = consultLexemaEnviroment(lexema);
      if (leftSide != null && tipo == 294)
      {
        if (this.word.getType() == null )
        {
          System.out.println("\n\nErro semantico! ");
          System.out.println("\nVariavel [" + this.word.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n");
          

          erro = "\nVariavel [" + this.word.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n";
          return erro;
        }
        else if (leftSide.getType() == null)
        {
          System.out.println("\n\nErro semantico! ");
          System.out.println("\nVariavel [" + leftSide.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n");
          

          erro = "\nVariavel [" + leftSide.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n";
          return erro;
        }
        else if (this.word.getType().equals(leftSide.getType()))
        {
          System.out.println("ok");
        }
        else if ((tagNum(this.word.getType())) && (tagNum(leftSide.getType())))
        {
          System.out.println("ok");
        }
        else
        {
          System.out.println("Erro semantico! Variavel [ " + leftSide.toString() + " ] incompativel." + "\nLinha: " + this.linha);
          erro = "Erro semantico! Variavel [ " + leftSide.toString() + " ] incompativel." + "\nLinha: " + this.linha;
          return erro;
        }
      }
      
    }
    else if ((this.word.getType() == null) && (this.tipo == 294))
    {
      System.out.println("\n\nErro semantico! ");
      System.out.println("\nVariavel [" + this.word.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n");
      

      erro = "\nVariavel [" + this.word.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n";
      return erro;
    }
    else if (this.tipo == 294)
    {
      String typ = tipo(tag);
      if (this.word.getType().equals(typ))
      {
        System.out.println("ok");
      }
      else if ((tagNum(this.word.getType())) && (tagNum(typ)))
      {
        System.out.println("ok");
      }
      else
      {
        System.out.println("Erro semantico! Variavel incompativel.\nLinha: " + this.linha);
        erro = "Erro semantico! Variavel incompativel.\nLinha: " + this.linha;
        return erro;
      }
    }
    else if (this.tipo != 294)
    {
      String tipoComparacao = tipo(this.tipo);
      String typ = tipo(tag);
      if (tipoComparacao.equals(typ))
      {
        System.out.println("ok");
      }
      else if ((tagNum(tipoComparacao)) && (tagNum(typ)))
      {
        System.out.println("ok");
      }
      else
      {
        System.out.println("Erro semantico! Comparacao incompativel.\nLinha: " + this.linha);
        erro = "Erro semantico! Comparacao incompativel.\nLinha: " + this.linha;
        return erro;
      }
    }
    return erro;
  }
}
