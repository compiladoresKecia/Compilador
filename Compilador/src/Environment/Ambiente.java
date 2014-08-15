package Environment;

import Environment.id.Identificador;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import lexer.Token;
import lexer.Word;

public class Ambiente
{
  public static Hashtable<Word, Identificador> table;
  protected Ambiente prev;
  
  public Ambiente(Ambiente prev)
  {
    table = new Hashtable();
    this.prev = prev;
  }
  
  public Ambiente()
  {
    table = new Hashtable();
  }
  
  public void put(Word w, Identificador id)
  {
    table.put(w, id);
  }
  
  public Identificador get(Token w)
  {
    for (Ambiente e = this; e != null; e = e.prev)
    {
      Identificador found = (Identificador)table.get(w);
      if (found != null) {
        return found;
      }
    }
    return null;
  }
  
  public Identificador getAmbienteAtual(Token w)
  {
    Identificador found = (Identificador)table.get(w);
    if (found != null) {
      return found;
    }
    return null;
  }
  
  public static Word getPeloLexema(String lexema)
  {
    for (Word key : table.keySet()) {
      if (key.toString().equals(lexema)) {
        return key;
      }
    }
    return null;
  }
  
  public static Set<Word> gerarHashMap()
  {
    return table.keySet();
  }
  
  public static void MostrarTabelaSimbolos()
  {
    int i = 0;
    System.out.println("****\t\tTabela de Simbolos\t\t***\n");
    System.out.println("Entrada Identificador\tTipo\t\n");
    Set<Word> words = gerarHashMap();
    Iterator<Word> iterator = words.iterator();
    while (iterator.hasNext())
    {
      Word palavra = (Word)iterator.next();
      if (palavra.getTag() == 294)
      {
        i++;
        System.out.println(i + ":\t" + palavra.toString() + "\t\t" + palavra.getType() + "\t");
      }
    }
  }
}
