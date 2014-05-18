package lexer;



//Abrir arquivo
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;




public class Lexer {
    
    
	private int n_linha = 1;     //Numero de linhas do programa     
	private char ch = ' ';        //Caractere lido do arquivo         
	private InputStream stream;
	private Hashtable<String, Word> words = new Hashtable<String, Word>();
	private File arquivoRO;        //Arquivo somente para leitura 
        private FileReader arquivoRAM;  //Arquivo para escrita
        private FileWriter arquivoWR;  //Arquivo para escrita

        
        private String nomeArquivo,caminho;
        
        
        
        // Construtor1 (criar o arquivo de leitura e reservar as palavras na tabela de simbolo)
        public Lexer(String arquivo){
            
            try {
                      arquivoRAM = new FileReader(arquivo);
                  } catch (FileNotFoundException e) {
                       System.out.println("Arquivo não encontrado");
                  }
        
            //Insere palavras reservadas na HashTable
            reserve(new Word("start", Tag.START));
            reserve(new Word("exit", Tag.EXIT));
            reserve(new Word("int", Tag.INT));
            reserve(new Word("float", Tag.FLOAT));
            reserve(new Word("string", Tag.STRING));            
            reserve(new Word("if", Tag.IF));
            reserve(new Word("then", Tag.THEN));
            reserve(new Word ("else", Tag.ELSE));
            reserve(new Word("end", Tag.END));
            reserve(new Word("while", Tag.WHILE));
            reserve(new Word("scan", Tag.SCAN));
            reserve(new Word ("print", Tag.PRINT));
            reserve(new Word ("not", Tag.NOT));
            reserve(new Word ("and", Tag.AND));
            reserve(new Word ("or", Tag.OR));
        }
        
        // Construtor 2 (pra pegar o nome do arquivo)
        public Lexer(){            
        }
        
         public String getNomeArquivo() {
            return nomeArquivo;
        }

        public String getCaminho() {
            return caminho;
        }

        // Metodo pra ler o arquivo
        public String abrirArquivo() throws IOException{
            
          // desejavel um try catch para o arquivo
                
                // Cria um "abridor" de arquivo. 
                // O caminho é o parâmetro
                // "." siginifica diretório corrente
                JFileChooser chooser = new JFileChooser(".");

                //Abre a Janela de Diálogo
                chooser.showOpenDialog(null);
                arquivoRO = chooser.getSelectedFile();
                caminho = arquivoRO.getPath().toString();
                nomeArquivo = arquivoRO.getName().toString();                
                caminho = caminho.replaceAll(nomeArquivo,"");
                
                
                //System.out.println("\nArquivo selecionado: " + nomeArquivo +"");
                //System.out.println("Caminho do Arquivo selecionado: " + caminho + "\n");
                
                return nomeArquivo;  
                //return caminho;  
                
        }
        
        
        //public String manipularArquivo(){
        //    String nomeArquivo2;
        //return nomeArquivo2;}
        
        // Metodo para escrever no arquivo
        public void escreverArquivo(String nomeArquivo, String texto, boolean acabou){
            
            try {
                arquivoWR = new FileWriter((nomeArquivo), true);
                //WriteFile
                PrintWriter escreve_linha = new PrintWriter(arquivoWR,true);
                //escreve_linha.printf("%n" +"  "+ "%s" + "  " + "%n",texto);
                escreve_linha.println(texto);
                if (acabou){
                    escreve_linha.close();System.out.println("acabou de ler! ");}
            } catch (IOException ex) {
                System.out.println("\nErro ao tentar escrever no arquivo. -> "+ex);
            }
            
            
        }
        
        
        // Metodo pra colocar as palavras reservadas na Tabela de Simbolos	
	private void reserve(Word t){
		words.put(t.lexeme, t);
	}
	
        
        // Lê o próximo caractere do arquivo
        private void readch() throws IOException{
            ch = (char) arquivoRAM.read();
        }
        
        
        // Lê o próximo caractere do arquivo e verifica se é igual a c  
        private boolean readch(char c) throws IOException{
            readch();
            if (ch != c) return false;
                ch = ' ';
            return true;    
        }
        
        
        // Metodo pra ler os caracteres do arquivo e formar os tokens
	public Token scan() throws IOException, SyntaxException{


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
            
            
            //Tratar inteiros
            //Numeros
            /*if (Character.isDigit(ch)){
            int value=0;
            do{
                value = 10*value + Character.digit(ch,10);
                readch();
            }while(Character.isDigit(ch));
            return new Inteiro(value);
            }*/
            
            
            //Tratar numeros Inteiros e Reais
            if (Character.isDigit(ch)) {
                
                float valor;
                String numero="";                      
                int value = 0;

                 while (Character.isDigit(ch)) {
                    if (Character.isDigit(ch))
                        numero+=ch;
                    readch();
                }

                if (ch == '.') {
                    numero+=ch;                
                    readch();

                    do {                    
                        numero+=ch;
                        readch();                    
                    } while (Character.isDigit(ch));

                    valor = Float.valueOf(numero);                
                    //return new Flutuante(valor);
                    Word w = new Word(String.valueOf(valor), Tag.FLUTUANTE);
                    words.put(String.valueOf(valor), w);
                    return w;

                } else {
                    value = Integer.parseInt(numero);
                    //return new Inteiro(value);
                    Word w = new Word(String.valueOf(value), Tag.INTEIRO);
                    words.put(String.valueOf(value), w);
                    return w;
                    
                }        
            }   
            
            
            //Tratar terminais
            switch (ch) {
                
                //Operadores
                
                case '=':
                    if (readch('=')) {
                        return Word.atrib;
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
                    if (readch('=')) {
                        return Word.le;
                    } else if(readch('>')){
                        return Word.neq;
                    } else{
                        return Word.lt;}
                
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
                if (readch('/')) {
                    return new Token('/');
                }
                else{
                    ch = ' ';
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

                    
                /*case '/':
                    if (readch('/')) {
                        return new Token('/');
                    }
                    else{
                        ch = ' ';
                        return Word.dv;
                    }
                */
                
            } //fim switch
            
            
            //Tratar textos (LITERAL)
            if (ch == '"'){
                //Stack pilha = new Stack<Character>();
                //pilha.push("\"");
                StringBuffer sb = new StringBuffer();
                //do{
                    do{
                        sb.append(ch);
                        readch();
                    }while(ch!='"');
                //  pilha.pop();
                //}while(!pilha.isEmpty());
                sb.append(ch);
                readch();
                String s = sb.toString();
                return new Word (s, Tag.LITERAL);
            }
            
            
            //Tratar os Identificadores
            if (Character.isLetter(ch)) {
                StringBuffer sb = new StringBuffer();
                do {
                    sb.append(ch);
                    readch();
                } while (Character.isLetterOrDigit(ch));
                String s = sb.toString();
                Word w = (Word) words.get(s);
                if (w != null) {
                    return w; //palavra já existe na HashTable
                }
                w = new Word(s, Tag.ID);
                words.put(s, w);
                return w;
            }
            
            
            //Caracteres não identificados
            Token t = new Token(ch);
            ch = ' ';
            return t;
            
	}
        
        public void ErroLexico(Character caracter, int linha ){
            System.out.println("\nErro léxico encontrado na linha "+linha+" !");
            System.out.printf("O caracter \" %c \" não foi reconhecido. ",caracter);
        }
        
        // Faz a análise léxica propriamente dita, formando os Tokens
        public void analiseLexica(){
            
            boolean acabou = false;
            
            try {
                
                escreverArquivo("LOG",("\t\t*** TOKENS ***\n"+"\nLinha"+"\tLexema\t\t"+"Valor"),false);
                
                do{
                    
                    acabou = !arquivoRAM.ready();
                    
                    Token token = new Token(0);
                    
                    try {
                        token = scan();
                    } catch (SyntaxException ex) {
                        System.out.println("Ocorreu um erro ao criar os tokens -> "+ex);
                    }
                    
                    //outra abordagem pra escrever no arquivo -> envia o formato e as strings. 
                    //overred: Insere "\n" entre as linhas
                    //System.out.printf("%d\t%s\t%d\n", n_linha, token.toString(),token.tag);                    
                    //String saida =  (n_linha + token.toString() + token.tag) ;
                    
                    if (token.toString().matches("\\d+") && (token.tag != 292 && token.tag!=290)){
                        String s = token.toString().valueOf(Character.toChars(token.tag));;
                        Character c = s.charAt(0);                    
                        ErroLexico(c, n_linha);
                        System.out.println("\nAbortando analise lexica ");
                        //System.exit(1); 
                    }
                    
                    String saida =  ("  "+n_linha +"      "+ token.toString() +"\t\t"+ token.tag) ;                    
                    escreverArquivo("LOG",saida,acabou);
                    
                    //
                    //MODO PANICO VAI AQUI
                    // 
                    
                }while(arquivoRAM.ready());
                
            } catch (IOException ex) {
                
                System.out.println("Ocorreu um erro ao ler o arquivo. -> "+ex);
                
            }
         
        
        }
        
        
        // 
}