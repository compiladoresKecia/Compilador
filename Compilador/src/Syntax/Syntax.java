package Syntax;

import Environment.Ambiente;
import Semantics.Semantics;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashSet;
import lexer.Lexer;
import lexer.SyntaxException;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

/**
 * Syntax Criado.
 * 
*
 * @author Guilherme e Alan Goncalves
 * @version 0.1 Syntax Finalizado.
 * @version 0.1 Semantics Unicidade feito
 * 
*/

/*
 Gramatica (regras semanticas)
 1 program ::= start [decl-list] stmt-list exit
 2 decl-list ::= decl {decl}
 3 decl ::= type ident-list ";"
 4 ident-list ::= identifier {"," identListAUX }
 5 identListAUX := identList | λ.
 6 type ::= int | float | string
 7 stmt-list ::= stmt { stmtListAUX }
 8 stmtListAUX := stmt | λ.
 9 stmt ::= assign-stmt ";" | if-stmt | while-stmt
 10 | read-stmt ";" | write-stmt ";"
 11 assign-stmt ::= identifier "=" simple_expr1 (se identifier.tipo = expression.tipo assign-stmt.tipo = erro senao assign-stmt.tipo =error)
 12 if-stmt ::= if condition then stmt-list else-stmt
 13 else-stmt ::= end | stmt-list end
 14 condition ::= expression
 15 while-stmt ::= do stmt-list stmt-sufix
 16 stmt-sufix ::= while condition end
 17 read-stmt ::= scan "(" identifier ")"
 18 write-stmt ::= print "(" writable ")"
 19 writable ::= simple-expr | literal
 20 expression1 ::= simple-expr | expression
 21 expression ::= relop simple-expr1 | λ (expression.tipo = simple-expr1.tipo)
 22 simple-expr1 ::= term (simple.expr1.tipo = term.tipo)
| simple-expr  (simple.expr1.tipo = simple-expr.tipo)
 23 simple-expr ::= addop term | λ  (simple-expr.tipo = term.tipo)
 24 term1 ::= factor-a (term1.tipo = factor-a.tipo)
| term (term1.tipo = term.tipo)
 25 term ::= mulop factor-a | λ (term.tipo = factor-a.tipo)
 26 fator-a ::= factor | not factor | "-" factor (factor-a.tipo = factor.tipo)
 27 factor ::= identifier ( factor.tipo = id.tipo)
| constant (factor.tipo = constant.tipo)
| "(" expression ")"  (factor.tipo = expression.tipo)
 28 relop ::= "==" | ">" | ">=" | "<" | "<=" | "<>"
 29 addop ::= "+" | "-" | or
 30 mulop ::= "*" | "/" | and
 31 constant ::= integer_const (constant.tipo = INTEIRO)
| float_const (constat.tipo = FLUTUANTE)
| literal   (constant.tipo = LITERAL)
 32 integer_const ::= digit {digit}
 33 float_const ::= digit{digit} “.”digit{digit}
 34 literal ::= " “" {caractere} "”"
 35 identifier ::= letter {letter | digit }
 36 letter ::= [A-za-z]
 37 digit ::= [0-9]
 38 caractere ::= um dos caracteres ASCII, exceto “” e quebra de linha
 */
public class Syntax {

    /**
     * Lexer para a analise sintatica.
     */
    Lexer lexer;

    /**
     * Semantics para a analise semantica.
     */
    Semantics semantic;
    private int linha_ERRO;
    /**
     * tipo da variavel para analise semantica.
     */
    private int tipo, tipo_MOSTLEFT, tipo_temporario, indice_Temporario;

    /**
     * Word (lexema) atual para analise semantica.
     */
    Word current;
    /**
     * Token carregado pelo lexer.
     */
    Token token = null;
    /**
     * COMENTARIO.
     */
    private static final String COMENTARIO = "COMENTARIO";
    /**
     * LITERAL.
     */
    private static final String LITERAL = "LITERAL";
    /**
     * Argumento auxiliar.
     */
    private String argAux;
    
    
    
    /**
     * StringBuffer relativo a gravacao.
     */
    private StringBuffer strBuffer;
    private StringBuffer strBufferSemantico;
    private generator.generatorCode generatorCode;
    private LinkedHashSet<String> stringTemporario = new LinkedHashSet<String>();
    private boolean DeuErro = false;
    private int linha;

    public Syntax(Lexer lexer) {
        generatorCode = new generator.generatorCode();
        this.strBuffer = new StringBuffer();
        this.strBufferSemantico = new StringBuffer();
        this.lexer = lexer;
        try {
            this.token = lexer.scan();
            this.current = ((Word) this.token);
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals("COMENTARIO")) {
                lexer.erroComentario();
            } else if (ex.getMessage().equals("LITERAL")) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {
            System.out.println("Ocorreu um erro ao ler o arquivo. ");
        }
    }

    private void advance() {
        try {
            this.token = this.lexer.scan();

            this.current = new Word(this.token, this.token.toString());
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals("COMENTARIO")) {
                this.lexer.erroComentario();
            } else if (ex.getMessage().equals("LITERAL")) {
                this.lexer.erroLiteral();
            }
        } catch (IOException ex) {
            System.out.println("Ocorreu um erro ao ler o arquivo.");
        }
    }

    private void erro() {
        this.linha = this.lexer.getN_linha();
        this.DeuErro = true;

        String erro = "Erro Sintático na linha " + this.linha + " próximo ao Token " + this.token.toString() + "\n";

        System.out.println(erro);
        this.strBuffer.append(erro);
    }

    private void erroInfos(int tag) {
        erro();
        System.out.println(" Tag a ser comido: " + tag + "\n");
        this.strBuffer.append("Token esperado: " + tag + " \n");
    }

    private void tratarErro(int tag) {
        erroInfos(tag);
        System.out.println("LOOOP: Token esperado: " + tag + " Tokens:");
        do {
            advance();
            System.out.println(this.token.getTag());
        } while ((this.token.getTag() != tag) && (this.lexer.arquivoPronto()) && (this.token.getTag() != Tag.PVR));
        if (this.token.getTag() == Tag.PVR) {
            eat(Tag.PVR);
        } else if (this.token.getTag() == tag) {
            eat(tag);
        }
    }

    private void eat(int tag) {
        if (this.token.getTag() == tag) {
            advance();
        } else {
            tratarErro(tag);
        }
    }

    public int getLinha() {
        return this.linha;
    }

    public void analisar() {
        program();
        if (!this.DeuErro) {
            System.out.println("\n\n- Nenhum erro foi encontrado.\nAnalise Sintatica realizada com sucesso!! -\n\n");
            String msg = "\n\n- Nenhum erro foi encontrado.\n\nAnalise Sintatica realizada com sucesso!! -\n\n";
            this.strBuffer = new StringBuffer(msg);
        }
        this.lexer.gravarSintatico(this.strBuffer);
        gravarBufferSemantico();
        if (strBufferSemantico.length() == 0) {
            strBufferSemantico.append("Nenhum erro foi encontrado!!!\n\nAnalise semantica realizada com sucesso");
            if (!this.DeuErro){
                generatorCode.gerarCodigo();
            }
        }
        this.lexer.gravarSemantico(this.strBufferSemantico);

        Ambiente.MostrarTabelaSimbolos();
    }

    private void program() {
        switch (this.token.getTag()) {
            case Tag.START:
                eat(Tag.START);
                declList();
                Ambiente.MostrarTabelaSimbolos();
                stmtList();
                eat(Tag.EXIT);
                break;
            default:
                erroInfos(Tag.START);
                declList();
                stmtList();
                eat(Tag.EXIT);
        }
    }

    private void declList() {
        switch (this.token.getTag()) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                decl();
                declListAUX();
        }
    }

    private void declListAUX() {
        eat(Tag.PVR);
        declList();
    }

    private void decl() {
        switch (this.token.getTag()) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                this.tipo = this.token.getTag();
                type();
                identList();
                break;
            default:
                erro();
        }
    }

    private void identList() {
        switch (this.token.getTag()) {
            case Tag.ID:
                this.semantic = new Semantics(this.current, this.tipo, this.token.toString());
                adicionarElemento(this.semantic.Type());
                identifier();
                identListAUX();
                break;
            default:
                erro();
        }
    }

    private void identListAUX() {
        if (this.token.getTag() == Tag.VR) {
            eat(Tag.VR);
            identList();
        }
    }

    private void type() {
        switch (this.token.getTag()) {
            case Tag.INT:
                eat(Tag.INT);
                break;
            case Tag.STRING:
                eat(Tag.STRING);
                break;
            case Tag.FLOAT:
                eat(Tag.FLOAT);
                break;
            default:
                erro();
        }
    }

    private void identifier() {
        switch (this.token.getTag()) {
            case Tag.ID:
                generatorCode.setArgumentTemporario(this.token.toString());
                eat(Tag.ID);
                break;
            default:
                erro();
        }
    }

    private void stmtList() {
        switch (this.token.getTag()) {
            case Tag.SCAN:
            case Tag.PRINT:
            case Tag.ID:
                stmt();
                eat(Tag.PVR);
                stmtListAUX();
                break;
            case Tag.IF:
            case Tag.DO:
                stmt();
                stmtListAUX();
                break;
            case Tag.EXIT:
                break;
            default:
                erro();
        }
    }

    private void stmtListAUX() {
        switch (this.token.getTag()) {
            case Tag.SCAN:
            case Tag.PRINT:
            case Tag.ID:
                stmt();
                eat(Tag.PVR);
                stmtListAUX();
                break;
            case Tag.IF:
            case Tag.DO:
                stmt();
                stmtListAUX();
                break;
        }
    }

    /**
     * stmt := assignStmt | ifStmt | whileStmt | readStmt | writeStmt.
     */
    private void stmt() {
        switch (this.token.getTag()) {
            case Tag.ID:
                this.semantic = new Semantics(this.current, Tag.ID, this.token.toString());                
                adicionarElemento(this.semantic.Absence());
                generatorCode.setResultFinal(this.token.toString());
                assignStmt();
                generatorCode.adicionarAtrib();
                break;
            case Tag.IF:
                ifStmt();
                break;
            case Tag.DO:
                whileStmt();
                break;
            case Tag.SCAN:
                readStmt();
                break;
            case Tag.PRINT:
                writeStmt();
                break;
            case Tag.EXIT:
                break;
            default:
                erro();
        }
    }

    private void assignStmt() {
        switch (this.token.getTag()) {
            case Tag.ID:
                tipo_MOSTLEFT = semantic.tipoIDLexema(this.token.toString());
                eat(Tag.ID);
                eat(Tag.ATRIB);
                simpleExpr1();
                break;
            default:
                erro();
        }
    }

    private void ifStmt() {
        switch (this.token.getTag()) {
            case Tag.IF:
                eat(Tag.IF);
                condition();                
                eat(Tag.THEN);
                generatorCode.adicionarIFStatement();
                stmtList();                
                generatorCode.inserirLinhaRotuloIF();
                elseStmt();
                break;
            default:
                erro();
        }
    }

    private void elseStmt() {
        if (this.token.getTag() == Tag.ELSE) {
            generatorCode.adicionarElsetatement();
            generatorCode.inserirLinhaRotuloELSE();
            eat(Tag.ELSE);
            stmtList();
        }
        eat(Tag.END);
    }

    private void condition() {
        expression1();
    }

    private void whileStmt() {
        switch (this.token.getTag()) {
            case Tag.DO:
                eat(Tag.DO);
                generatorCode.setLinhaRetorno();
                stmtList();
                stmtSufix();
                break;
            default:
                erro();
        }
    }

    private void stmtSufix() {
        switch (this.token.getTag()) {
            case Tag.WHILE:
                eat(Tag.WHILE);
                condition();                
                eat(Tag.END);
                generatorCode.adicionarWhile();
                break;
            default:
                erro();
        }
    }

    private void readStmt() {
        switch (this.token.getTag()) {
            case Tag.SCAN:
                eat(Tag.SCAN);
                eat(Tag.AP);
                identifier();
                generatorCode.adicionarCodigo(Tag.SCAN);
                eat(Tag.FP);
                break;
            default:
                erro();
        }
    }

    private void writeStmt() {
        switch (this.token.getTag()) {
            case Tag.PRINT:
                eat(Tag.PRINT);
                eat(Tag.AP);
                writable();
                generatorCode.adicionarCodigo(Tag.PRINT);
                eat(Tag.FP);
                break;
            default:
                erro();
        }
    }

    private void writable() {
        switch (this.token.getTag()) {
            case Tag.INTEIRO:
            case Tag.FLUTUANTE:
            case Tag.ID:
                adicionarElemento(this.semantic.Absence());
                simpleExpr1();
                break;
            case Tag.LITERAL:
                generatorCode.setArgumentTemporario(this.token.toString());
                eat(Tag.LITERAL);
                break;
            case 293:
            default:
                erro();
        }
    }

    private void expression1() {
        switch (this.token.getTag()) {
            case Tag.NOT:
            case Tag.MINUS:
            case Tag.AP:
                simpleExpr1();                
                expression();
                break;
            case Tag.INTEIRO:
            case Tag.LITERAL:
            case Tag.FLUTUANTE:
                tipo_MOSTLEFT = this.token.getTag();
                simpleExpr1();
                expression();
                break;
            case Tag.ID:                
                tipo_MOSTLEFT = semantic.tipoIDLexema(this.token.toString());
                simpleExpr1();
                expression();
                break;
            default:
                erro();
        }
    }

    private void expression() {
        if ((this.token.getTag() == Tag.EQ) || (this.token.getTag() == Tag.GT) || (this.token.getTag() == Tag.GE) || (this.token.getTag() == Tag.LT) || (this.token.getTag() == Tag.LE) || (this.token.getTag() == Tag.NEQ)) {
            generatorCode.setCondicionalOperador(this.token.getTag());
            generatorCode.adicionarLadoEsquerdoCondicao();
            relop();
            simpleExpr1();
            adicionarElemento(semantic.TypeAssignment(tipo_MOSTLEFT, tipo_temporario));
            generatorCode.adicionarCondicao();
            
        }
    }

    private void simpleExpr1() {
        switch (this.token.getTag()) {
            case Tag.ID:
                adicionarElemento(this.semantic.Absence());
                term1();
                adicionarElemento(semantic.TypeAssignment(tipo_MOSTLEFT, tipo_temporario));
                simpleExpr();                
                break;
            case Tag.INTEIRO:
            case Tag.LITERAL:
            case Tag.FLUTUANTE:
            case Tag.NOT:
            case Tag.MINUS:
            case Tag.AP:
                term1();
                simpleExpr();
                break;
            default:
                erro();
        }
    }

    private void simpleExpr() {
        if ((this.token.getTag() == Tag.PLUS) || (this.token.getTag() == Tag.MINUS) || (this.token.getTag() == Tag.OR)) {            
            generatorCode.setOperatorTemporario(this.token.getTag());
            indice_Temporario = generatorCode.getIndiceTemporario();
            if (indice_Temporario==0){
                argAux = generatorCode.getArgumenTemporario();
            }
            addop();            
            term1();
            generatorCode.adicionarAssigmentExpression(argAux);
            adicionarElemento(semantic.TypeAssignment(tipo_MOSTLEFT, tipo_temporario));
            simpleExpr();
        }
    }

    private void term1() {
        switch (this.token.getTag()) {
            case Tag.NOT:
            case Tag.MINUS:
            case Tag.AP:
            case Tag.INTEIRO:
            case Tag.LITERAL:
            case Tag.FLUTUANTE:
            case Tag.ID:
                factorA();
                term();
                break;
            default:
                erro();
        }
    }

    private void term() {
        if ((this.token.getTag() == Tag.MULT) || (this.token.getTag() == Tag.DIV) || (this.token.getTag() == 268)) {
            generatorCode.setOperatorTemporario(this.token.getTag());
            argAux = generatorCode.getArgumenTemporario();
            mulop();            
            factorA();
            generatorCode.adicionarAssigmentExpression(argAux);
            adicionarElemento(semantic.TypeAssignment(tipo_MOSTLEFT, tipo_temporario));
            term();
        }
    }

    private void factorA() {
        switch (this.token.getTag()) {
            case Tag.AP:
            case Tag.INTEIRO:
            case Tag.LITERAL:
            case Tag.FLUTUANTE:
            case Tag.ID:
                factor();
                break;
            case Tag.NOT:
                eat(Tag.NOT);
                factor();
                generatorCode.adicionarNot();
                break;
            case Tag.MINUS:
                eat(Tag.MINUS);
                factor();
                generatorCode.adicionarMinus();
                break;
            default:
                erro();
        }
    }

    private void factor() {
        switch (this.token.getTag()) {
            case Tag.ID:
                //   adicionarElemento(this.semantic.TypeAssignment(this.token.toString(), 0));
                tipo_temporario = semantic.tipoIDLexema(token.toString());
                //ID  com o resultado final
                generatorCode.setArgumentTemporario(this.token.toString());
                eat(Tag.ID);
                break;
            case Tag.INTEIRO:
            case Tag.LITERAL:
            case Tag.FLUTUANTE:
                constant();
                break;
            case Tag.AP:
                eat(Tag.AP);
                expression1();
                eat(Tag.FP);
                break;
            case Tag.FP:
            case Tag.AS:
            case 289:
            case 293:
            default:
                erro();
        }
    }

    private void relop() {
        switch (this.token.getTag()) {
            case Tag.EQ:
                eat(Tag.EQ);
                break;
            case Tag.GT:
                eat(Tag.GT);
                break;
            case Tag.GE:
                eat(Tag.GE);
                break;
            case Tag.LT:
                eat(Tag.LT);
                break;
            case Tag.LE:
                eat(Tag.LE);
                break;
            case Tag.NEQ:
                eat(Tag.NEQ);
                break;
            default:
                erro();
        }
    }

    private void addop() {
        switch (this.token.getTag()) {
            case Tag.PLUS:
                eat(Tag.PLUS);
                break;
            case Tag.MINUS:
                eat(Tag.MINUS);
                break;
            case Tag.OR:
                eat(Tag.OR);
                break;
            default:
                erro();
        }
    }

    private void mulop() {
        switch (this.token.getTag()) {
            case Tag.MULT:
                eat(Tag.MULT);
                break;
            case Tag.DIV:
                eat(Tag.DIV);
                break;
            case 268:
                eat(268);
                break;
            default:
                erro();
        }
    }

    private void constant() {
        switch (this.token.getTag()) {
            case Tag.INTEIRO:
                tipo_temporario = Tag.INTEIRO; 
                generatorCode.setArgumentTemporario(this.token.toString());
                eat(Tag.INTEIRO);
                break;
            case Tag.LITERAL:
                tipo_temporario = Tag.LITERAL;
                generatorCode.setArgumentTemporario(this.token.toString());
                eat(Tag.LITERAL);
                break;
            case Tag.FLUTUANTE:
                tipo_temporario = Tag.FLUTUANTE;
                generatorCode.setArgumentTemporario(this.token.toString());
                eat(Tag.FLUTUANTE);
                break;
            default:
                erro();
        }
    }

    public void adicionarElemento(String entrada) {
        if (entrada != null) {
            this.stringTemporario.add(entrada);
        }
    }

    public void gravarBufferSemantico() {
        Iterator<String> iterator = stringTemporario.iterator();
        while (iterator.hasNext()) {
            strBufferSemantico.append(iterator.next());
        }

    }

}
