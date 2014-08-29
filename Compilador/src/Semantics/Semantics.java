package Semantics;

import Environment.Ambiente;
import Environment.id.Identificador;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import lexer.Lexer;
import lexer.Tag;
import lexer.Word;

public class Semantics {

    private Word word;
    private Word word1;
    private int tipo;
    private String _tipo;
    private String lexema;
    private static Identificador idVazio;
    private int linha;

    public Semantics(Word word, int tipo, String lexema) {
        System.out.println("Semantics ---- Tipo: " + tipo);
        System.out.println("Lexema: " + lexema);
        this.word = word;
        this.tipo = tipo;
        this.lexema = lexema;
        idVazio = new Identificador(0);
    }

    /**
     * Declarar tipo e verificar unicidade.
     *
     * @return
     */
    public String Type() {
        System.out.println("Tipo");
        this.word1 = Ambiente.getPeloLexema(this.lexema);
        String erro = null;
        if (this.word1.getType() == null) {
            switch (this.tipo) {
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
            this.word1.setType(this._tipo);
            Ambiente.table.remove(this.word);
            Ambiente.table.put(this.word1, Ambiente.gerarIdentificador());
        } else {
            erro = Unicity();
        }

        return erro;
    }

    /**
     * Ausencia de tipo.
     *
     * @return
     */
    public String Absence() {
        String erro = null;

        this.linha = Lexer.getLinhaID();
        Set<Word> words = Ambiente.gerarHashMap();
        Iterator<Word> iterator = words.iterator();
        System.out.println(this.lexema);
        while (iterator.hasNext()) {
            Word palavra = (Word) iterator.next();
            if ((palavra.getTag() == Tag.ID) && (this.lexema.equals(palavra.toString()))) {
                if (palavra.getType() == null) {
                    System.out.println("\n\nErro semantico! ");
                    System.out.println("\nVariavel [" + palavra.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n");

                    erro = "\nVariavel [" + palavra.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n";
                    return erro;
                }
            }
        }
        return erro;
    }

    /**
     * Unicidade.
     *
     * @return
     */
    public String Unicity() {
        this.linha = Lexer.getLinhaID();
        System.out.println("-> Tipo ja cadastrado!\n** Identificador: " + this.word1.toString());
        System.out.println("**  Tipo = " + this.word1.getType() + "\n");
        return "-> Tipo ja cadastrado!\n** Identificador: " + this.word1.toString() + "\n**  Tipo = " + this.word1.getType() + "\n";
    }

    public Word consultLexemaEnviroment(String lexema) {
        Set<Word> words = Ambiente.gerarHashMap();
        Iterator<Word> iterator = words.iterator();
        while (iterator.hasNext()) {
            Word palavra = (Word) iterator.next();
            if ((palavra.getTag() == Tag.ID) && (palavra.toString().equals(lexema))) {
                return palavra;
            }
        }
        return null;
    }

    public boolean tagNum(String tag) {
        if ((tag.equals("INT")) || (tag.equals("FLOAT")) || (tag.equals("STRING"))) {
            return true;
        }
        return false;
    }

    public int tipoDaPalavra(String tipoAnalise) {
        int tipoRetorno = -1;
        if (tipoAnalise != null) {
            switch (tipoAnalise) {
                case "INT":
                    tipoRetorno = Tag.INTEIRO;
                    break;
                case "STRING":
                    tipoRetorno = Tag.FLUTUANTE;
                    break;
                case "FLOAT":
                    tipoRetorno = Tag.FLUTUANTE;
            }
        }
        return tipoRetorno;
    }

    public int tipoIDLexema(String lexema) {
        if (lexema != null) {
            return tipoDaPalavra(consultLexemaEnviroment(lexema).getType());
        }
        return -1;
    }

    public String TypeAssignment(int tipoA, int tipoB) {
        if (tipoA != tipoB) {
            this.linha = Lexer.getLinhaID();
            System.out.println("Erro semantico!");
            System.out.println("Erro: Tipos incompativeis na linha " + this.linha);
            return "Erro: Tipos incompativeis na linha" + this.linha;
        }
        return null;
    }

    /*
     public String TypeAssignment(String lexema, int tag)
     {
     String erro = null;
     this.linha = Lexer.getLinhaID();
     if (this.word.getTag() == Tag.ID) {
     this.word = consultLexemaEnviroment(this.word.toString());
     }
     if (lexema != null)
     {
     System.out.println("Word leftside: " + lexema + "\n Word :" + this.word.toString());
     Word leftSide = consultLexemaEnviroment(lexema);
     if (leftSide != null && tipo == Tag.ID)
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
     else if ((this.word.getType() == null) && (this.tipo == Tag.ID))
     {
     System.out.println("\n\nErro semantico! ");
     System.out.println("\nVariavel [" + this.word.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n");
      

     erro = "\nVariavel [" + this.word.toString() + "] foi utilizada mas NAO foi declarada." + "\nLinha: " + this.linha + "\n\n";
     return erro;
     }
     else if (this.tipo == Tag.ID)
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
     else if (this.tipo != Tag.ID)
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
     }*/
}
