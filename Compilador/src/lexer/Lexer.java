package lexer;

//Abrir arquivo
import Environment.Ambiente;
import Environment.id.Identificador;
import carregarArquivo.BaseTXT;
import java.io.*;
import java.util.*;

/**
 * Analisador lexico.
 *
 * Whats New
 *
 *
 * To Do Tratar Excecoes
 *
 * @author Alan e Guilherme
 * @version 0.1 Tokens Escritos
 * @vesion 0.2 correcoes e incrementos dos comentarios
 * @version 0.3 adicionada o objeto ambiente, foi dado um identificado vazio
 * @version 0.4 excecoes adicionadas
 *
 */
public class Lexer {

    private int n_linha = 1;     //Numero de linhas do programa     
    private static int linha_identificador = 1;  //Linha do Identificador     
    private char ch = ' ', chAnterior = ' ';//Caractere lido do arquivo 
    private final BaseTXT baseTXT;
    private static Identificador idVazio;
    private final ArrayList<Integer> palavrasReservadas;
    //Tokens que sao uma palavra reservada
    private final ArrayList<Token> tokenPalavraReservada;
    private final ArrayList<String> listaDeErros;
    private final static String COMENTARIO = "COMENTARIO";
    private final static String LITERAL = "LITERAL";

    /**
     * Construtor (criar o arquivo de leitura e reservar as palavras na tabela
     * de simbolo).
     *
     * @param baseTXT
     */
    public Lexer(BaseTXT baseTXT) {
        this.idVazio = new Identificador(0);
        listaDeErros = new ArrayList<>();
        palavrasReservadas = new ArrayList<>();
        tokenPalavraReservada = new ArrayList<>();
        Ambiente ambiente = new Ambiente();
        //Insere palavras reservadas na HashTable
        inserirPalavrasReservadas();
        this.baseTXT = baseTXT;
    }

    /**
     * Insere palavras reservadas na HashTable.
     */
    private void inserirPalavrasReservadas() {
        reserve(new Word("start", Tag.START));
        reserve(new Word("exit", Tag.EXIT));
        reserve(new Word("int", Tag.INT));
        reserve(new Word("float", Tag.FLOAT));
        reserve(new Word("string", Tag.STRING));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("then", Tag.THEN));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("end", Tag.END));
        reserve(new Word("do", Tag.DO));        
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("scan", Tag.SCAN));
        reserve(new Word("print", Tag.PRINT));
        reserve(new Word("not", Tag.NOT));
        reserve(new Word("and", Tag.AND));
        reserve(new Word("or", Tag.OR));
        palavrasReservadas.add((Tag.START));
        palavrasReservadas.add((Tag.EXIT));
        palavrasReservadas.add((Tag.INT));
        palavrasReservadas.add((Tag.FLOAT));
        palavrasReservadas.add((Tag.STRING));
        palavrasReservadas.add((Tag.IF));
        palavrasReservadas.add((Tag.THEN));
        palavrasReservadas.add((Tag.ELSE));
        palavrasReservadas.add((Tag.END));
        palavrasReservadas.add((Tag.DO));
        palavrasReservadas.add((Tag.WHILE));
        palavrasReservadas.add((Tag.SCAN));
        palavrasReservadas.add((Tag.PRINT));
        palavrasReservadas.add((Tag.NOT));
        palavrasReservadas.add((Tag.AND));
        palavrasReservadas.add((Tag.OR));
    }

    /**
     * Metodo pra colocar as palavras reservadas na Tabela de Simbolos.
     *
     * @param t
     */
    private static void reserve(Word t) {
        Ambiente.table.put(t, idVazio);
    }

    /**
     * Lê o próximo caractere do arquivo
     *
     * @throws IOException
     */
    private void readch() throws IOException {
        ch = baseTXT.readch();
    }

    //  
    /**
     * Lê o próximo caractere do arquivo e verifica se é igual a c.
     *
     * @param c
     * @return Verdadeiro se e´ igual.
     * @throws IOException
     */
    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) {
            return false;
        }
        ch = ' ';
        return true;
    }

    /**
     * Obter a linha correspodente a execucao.
     *
     * @return
     */
    public int getN_linha() {
        return n_linha;
    }
    
    /*
     * Linha de um identificador
     */
    public static int getLinhaID() {
        return linha_identificador;
    }

    /**
     * Desconsidera delimitadores na entrada.
     *
     * @throws IOException
     */
    private void desconsideraDelimitadores() throws IOException {
        //Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') {
                continue;
            } else if (ch == '\n') {
                n_linha++; //conta linhas
            } else {
                break;
            }
        }
    }

    /**
     * Tratar numeros Inteiros e Reais.
     *
     * @return
     * @throws IOException
     */
    public Token tratarNumeros() throws IOException {
        float valor;
        String numero = "";
        int value = 0;

        while (Character.isDigit(ch)) {
            if (Character.isDigit(ch)) {
                numero += ch;
            }
            readch();
        }

        if (ch == '.') {
            numero += ch;
            readch();

            do {
                numero += ch;
                readch();
            } while (Character.isDigit(ch));

            valor = Float.parseFloat(numero);
            //return new Flutuante(valor);
            Word w = new Word(String.valueOf(valor), Tag.FLUTUANTE);
            Ambiente.table.put(w, idVazio);
            return w;

        } else {
            value = Integer.parseInt(numero);
            //return new Inteiro(value);
            Word w = new Word(String.valueOf(value), Tag.INTEIRO);
            Ambiente.table.put(w, idVazio);
            return w;

        }

    }

    /**
     * Caracteres não identificados em toda analise.
     *
     * @return
     */
    public Token caracterNaoIdentificado() {
        //Caracteres não identificados
        Token t = new Token(ch);
        ch = ' ';
        return t;
    }

    /**
     * Tratar terminais.
     *
     * @return
     * @throws IOException
     * @throws SyntaxException
     */
    public Token tratarTerminal() throws IOException, SyntaxException {
        switch (ch) {

            //Operadores
            case '=':
                if (readch('=')) {
                    return Word.eq;
                } else {
                    return Word.atrib;
                }

            case '>':
                if (readch('=')) {
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '<':
                readch();
                if (ch == ('=')) {
                    return Word.le;
                } else if (ch == ('>')) {
                    return Word.neq;
                } else {
                    return Word.lt;
                }

            case '+':
                ch = ' ';
                return Word.plus;

            case '-':
                ch = ' ';
                return Word.minus;

            case '*':
                ch = ' ';
                return Word.mult;

            case '/':
                readch();
                if (ch == ('/')) {
                    do {
                        readch();
                    } while (ch != '\n');
                    ch = ' ';
                    return scan();

                } else if (ch == ('*')) {
                    readch();
                    desconsideraDelimitadores();
                    chAnterior = ch;
                    while (chAnterior != '*' && ch != '/') {
                        chAnterior = ch;
                        desconsideraDelimitadores();
                        readch();
                        if (!baseTXT.arquivoLidoPronto()) {
                            throw new SyntaxException(COMENTARIO);
                        }
                    }
                    ch = ' ';
                    return scan();

                } else {                    
                    return Word.div;
                }

            case '(':
                ch = ' ';
                return Word.ap;

            case ',':
                ch = ' ';
                return Word.vr;

            case ';':
                ch = ' ';
                return Word.pvr;

            case '.':
                ch = ' ';
                return Word.pt;

            case ')':
                ch = ' ';
                return Word.fp;
            default:
                return caracterNaoIdentificado();
        } //fim switch
    }

    /**
     * Tratar textos (LITERAL).
     *
     * @return
     * @throws IOException
     */
    public Token tratarLiteral() throws IOException, SyntaxException {
        StringBuffer sb = new StringBuffer();

        do {
            sb.append(ch);
            readch();
            if (!baseTXT.arquivoLidoPronto()) {
                throw new SyntaxException(LITERAL);
            }
        } while (ch != '"');
        sb.append(ch);
        readch();
        String s = sb.toString();
        return new Word(s, Tag.LITERAL);
    }

    /**
     * Tratar os Identificadores.
     *
     * @return
     * @throws IOException
     */
    public Token tratarIdentificador() throws IOException {        
        StringBuffer sb = new StringBuffer();
        do {
            sb.append(ch);
            readch();
        } while (Character.isLetterOrDigit(ch));
        String s = sb.toString();
        Word w = Ambiente.getPeloLexema(s);
        if (w != null) {
            return w; //palavra já existe na HashTable
        }
        w = new Word(s, Tag.ID);
        Ambiente.table.put(w, idVazio);
        linha_identificador = n_linha;
        // Ambiente.MostrarTabelaSimbolos();  //TESTE
        return w;
    }

    /**
     * Metodo pra ler os caracteres do arquivo e formar os tokens.
     *
     * @return a token dessa palavra
     * @throws IOException A resolver.
     * @throws SyntaxException A resolver.
     */
    public Token scan() throws IOException, SyntaxException {

        //Desconsidera delimitadores na entrada
        desconsideraDelimitadores();

        //Tratar numeros Inteiros e Reais
        if (Character.isDigit(ch)) {
            return tratarNumeros();
        }

        //Tratar textos (LITERAL)
        if (ch == '"') {
            return tratarLiteral();
        }

        //Tratar os Identificadores
        if (Character.isLetter(ch)) {
            return tratarIdentificador();
        }
        //Tratar terminais
        return tratarTerminal();

    }

    /**
     * Erro lexico.
     *
     * @param caracter
     * @param linha
     */
    public void erroLexico(Character caracter, int linha) {
        listaDeErros.add("O caracter " + caracter + " na linha :" + linha + " não foi reconhecido. ");
    }

    /**
     * Erro comentario.
     */
    public void erroComentario() {
        listaDeErros.add("O comentario nao foi devidamente encerrado");
    }

    /**
     * Erro da String.
     */
    public void erroLiteral() {
        listaDeErros.add("O literal nao foi devidamente encerrado");
    }

    public boolean arquivoPronto() {
        try {
            return baseTXT.arquivoLidoPronto();
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao ler o arquivo. -> " + e);
        }
        return false;

    }

    /**
     * Faz a análise léxica propriamente dita, formando os Tokens.
     */
    public void analiseLexica() {
        try {
            baseTXT.escreverArquivo(("\t*** TOKENS ***\n"), false);
            baseTXT.escreverArquivo(("\nLinha"
                    + "\tLexema\t\t" + "Valor"), false);

            do {
                Token token = new Token(0);

                try {
                    token = scan();
                    if (token.toString().matches("\\d+")
                            && (token.getTag() != Tag.FLUTUANTE && token.getTag() != Tag.INTEIRO)) {
                        String s = token.toString().
                                valueOf(Character.toChars(token.getTag()));;
                        Character c = s.charAt(0);
                        erroLexico(c, n_linha);

                    }

                    if (palavrasReservadas.contains(token.getTag())) {
                        tokenPalavraReservada.add(token);
                    }
                    String saida = ("  " + n_linha
                            + "      " + token.toString() + "\t\t" + token.getTag());
                    baseTXT.escreverArquivo(saida, false);
                } catch (SyntaxException ex) {
                    if (ex.getMessage().equals(COMENTARIO)) {
                        erroComentario();
                    } else if (ex.getMessage().equals(LITERAL)) {
                        erroLiteral();
                    }
                }

            } while (baseTXT.arquivoLidoPronto());
            obterIdentificadores();

        } catch (IOException ex) {

            System.out.println("Ocorreu um erro ao ler o arquivo. -> " + ex);

        }

    }

    /**
     * Grava os identificadores no arquivo LOG.
     */
    private void obterIdentificadores() {
        baseTXT.escreverArquivo("", false);
        baseTXT.escreverArquivo("\t**Identificadores**", false);
        Set<Word> words = Ambiente.gerarHashMap();
        Iterator<Word> iterator = words.iterator();
        while (iterator.hasNext()) {
            Word palavra = iterator.next();
            if (palavra.getTag() == Tag.ID) {
                baseTXT.escreverArquivo(" " + palavra.lexeme, false);
            }
        }
        baseTXT.escreverArquivo("\t**Palavras Reservadas**", false);
        while (!tokenPalavraReservada.isEmpty()) {
            baseTXT.escreverArquivo(" " + tokenPalavraReservada.remove(0).toString(), false);
        }
        baseTXT.escreverArquivo("\t**Erros**", false);
        while (!listaDeErros.isEmpty()) {
            baseTXT.escreverArquivo("\t" + listaDeErros.remove(0).toString(), false);
        }

        baseTXT.escreverArquivo("", true);

    }
    
    public void gravarSintatico(StringBuffer stBuffer){
        baseTXT.escreverArquivoSintatico(stBuffer);
    }

}
