package Syntax;

import java.io.IOException;
import lexer.Lexer;
import lexer.SyntaxException;
import lexer.Tag;
import lexer.Token;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author wolvery
 */
public class Syntax {

    Lexer lexer;
    Token token = null;
    private final static String COMENTARIO = "COMENTARIO";
    private final static String LITERAL = "LITERAL";

    public Syntax(Lexer lexer) {
        this.lexer = lexer;
        try {
            token = lexer.scan();
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals(COMENTARIO)) {
                lexer.erroComentario();
            } else if (ex.getMessage().equals(LITERAL)) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {

            System.out.println("Ocorreu um erro ao ler o arquivo. ");

        }
    }

    private void advance() {
        try {
            token = lexer.scan();
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals(COMENTARIO)) {
                lexer.erroComentario();
            } else if (ex.getMessage().equals(LITERAL)) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {

            System.out.println("Ocorreu um erro ao ler o arquivo. ");

        }
    }

    private void erro() {
        try {
            System.out.println("Erro Sintático na linha " + lexer.scan() + " próximo ao Token " + token.toString());
        } catch (SyntaxException ex) {
            if (ex.getMessage().equals(COMENTARIO)) {
                lexer.erroComentario();
            } else if (ex.getMessage().equals(LITERAL)) {
                lexer.erroLiteral();
            }
        } catch (IOException ex) {

            System.out.println("Ocorreu um erro ao ler o arquivo. ");

        }
        System.exit(0);

    }

    private void eat(int tag) {
        if (token.getTag() == tag) {
            System.out.println("eat " + token);
            advance();
        } else {
            erro();
        }
    }

    private void analisar() {
        program();
    }

    // program -> "program" identifier body
    private void program() {
        switch (token.getTag()) {
            case Tag.START:
                eat(Tag.START);
                declList();
                stmtList();
                break;

            default:
                erro();
        }
    }

    // declList1 -> decl declList
    private void declList() {
        switch (token.getTag()) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                decl();
                declListAUX();
                break;

        }
    }

    // decl -> type identList1
    private void decl() {
        switch (token.getTag()) {
            case Tag.INT:
            case Tag.FLOAT:
            case Tag.STRING:
                type();
                identList();
                break;

            default:
                erro();
        }
    }

    private void declListAUX() {
        if (token.getTag() == Tag.PVR) {
            eat(Tag.PVR);            
            declList();
        }
    }

    // identList1 -> identifier identList
    private void identList() {
        switch (token.getTag()) {
            case Tag.ID:
                identifier();
                identListAUX();
                break;

            default:
                erro();
        }
    }

    // identList -> , identifier identList | lambda
    private void identListAUX() {
        if (token.getTag() == Tag.VR) {
            eat(Tag.VR);
            identifier();
            identList();
        }
    }

    // type -> "int" | "char"
    private void type() {
        switch (token.getTag()) {
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

    // identifier -> id
    private void identifier() {
        switch (token.getTag()) {
            case Tag.ID:
                eat(Tag.ID);
                break;

            default:
                erro();
        }
    }

    // stmtList -> ; stmt stmtList
    private void stmtList() {
        if (token.getTag() == Tag.PVR) {
            eat(';');
            //  stmt();
            stmtList();
        }
    }
    /*
     // body -> declList1 { stmtList } | { stmtList }
     private void body() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.INTEIRO:
     case Tag.LITERAL:
     declList1();
     eat('{');
     stmtList1();
     eat('}');
     break;
                
     case '{':
     eat('{');
     stmtList1();
     eat('}');
     break;
                
     default:
     erro();
     }
     }
    
     // declList1 -> decl declList
     private void declList1() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.INT:
     case Tag.LITERAL:
     decl();
     declList();
     break;
                
     default:
     erro();
     }
     }

    
     // decl -> type identList1
     private void decl() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.INT:
     case Tag.LITERAL:
     type();
     identList1();
     break;
                
     default:
     erro();
     }
     }
    
     // identList1 -> identifier identList
     private void identList1() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     identifier();
     identList();
     break;
                
     default:
     erro();
     }
     }  
    
     // identList -> , identifier identList | lambda
     private void identList() throws IOException
     {
     if(token.getTag() == ',')
     {
     eat(',');
     identifier();
     identList();
     }
     }
    
   
    
    
    
     // stmtList1 -> stmt stmtList
     private void stmtList1() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     case Tag.IF:
     case Tag.WHILE:
     case Tag.SCAN:
     case Tag.PRINT:
     stmt();
     stmtList();
     break;
     default:
     erro();
     }
     }
    
    
    
     // stmt -> assignStmt | ifStmt | whileStmt | readStmt | writeStmt
     private void stmt() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     assignStmt();
     break;
                
     case Tag.IF:
     ifStmt1();
     break;
                
     case Tag.WHILE:
     whileStmt();
     break;
                
     case Tag.SCAN:
     readStmt();
     break;
                
     case Tag.PRINT:
     writeStmt();
     break;
                
     default:
     erro();
     }
     }
    
     // assignStmt -> identifier = simpleExpr
     private void assignStmt() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     eat(Tag.ID);
     eat('=');
     simpleExpr1();
     break;
            
     default:
     erro();
     }
     }
    
     // ifStmt1 -> if ( condition ) { stmtList1 } ifStmt
     private void ifStmt1() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.IF:
     eat(Tag.IF);
     eat('(');
     condition();
     eat(')');
     eat('{');
     stmtList1();
     eat('}');
     ifStmt();
     break;
            
     default:
     erro();
     }
     }
    
     // ifStmt -> else { stmtList1 } | lambda
     private void ifStmt() throws IOException
     {
     if(token.getTag() == Tag.ELSE)
     {
     eat(Tag.ELSE);
     eat('{');
     stmtList1();
     eat('}');
     }
     }
    
     // condition -> expression
     private void condition() throws IOException
     {
     expression1();
     }
    
     // whileStmt -> stmtPrefix { stmtList1 }
     private void whileStmt() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.WHILE:
     stmtPrefix();
     eat('{');
     stmtList1();
     eat('}');
     break;
            
     default:
     erro();
     }
     }
    
     // stmtPrefix -> while ( condition )
     private void stmtPrefix() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.WHILE:
     eat(Tag.WHILE);
     eat('(');
     condition();
     eat(')');
     break;
            
     default:
     erro();
     }
     }
    
     // readStmt -> read identifier
     private void readStmt() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.SCAN:
     eat(Tag.SCAN);
     identifier();
     break;
            
     default:
     erro();
     }
     }
    
     // writeStmt -> write writable
     private void writeStmt() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.PRINT:
     eat(Tag.PRINT);
     writable();
     break;
            
     default:
     erro();
     }
     }
    
     // writable -> simpleExpr1 | literal
     private void writable() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     case Tag.INTEIRO:
     case Tag.LITERAL:
     case '(':               
     case Tag.NOT:
     case '-':
     simpleExpr1();
     break;
            
     case Tag.FLUTUANTE:
     eat(Tag.FLUTUANTE);
     break;
                
     default:
     erro();
     }
     }
    
     // expression1 -> simpleExpr1 expression
     private void expression1() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     case Tag.INTEIRO:
     case Tag.LITERAL:
     case '(':               
     case Tag.NOT:
     case '-':
     simpleExpr1();
     expression();
     break;
                
     default:
     erro();
     }
     }
    
     // expression -> relop simpleExpr1 | lambda
     private void expression() throws IOException
     {
     if(token.getTag() == Tag.EQ || token.getTag() == '>' || token.getTag() == Tag.GE ||
     token.getTag() == '<' || token.getTag() == Tag.LE || token.getTag() == Tag.NEQ)
     {
     relop();
     simpleExpr1();
     }
     }
    
     // simpleExpr1 -> term1 simpleExpr
     private void simpleExpr1() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     case Tag.INTEIRO:
     case Tag.LITERAL:
     case '(':               
     case Tag.NOT:
     case '-':
     term1();
     simpleExpr();
     break;
                
     default:
     erro();
     }
     }
    
     // simpleExpr -> + | - | or | lambda
     private void simpleExpr() throws IOException
     {
     if(token.getTag() == '+' || token.getTag() == '-' || token.getTag() == Tag.OR)
     {
     addop();
     term1();
     simpleExpr();
     }
     }
    
     // term1 -> factorA term
     private void term1() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     case Tag.INTEIRO:
     case Tag.LITERAL:
     case '(':               
     case Tag.NOT:
     case '-':
     factorA();
     term();
     break;
                
     default:
     erro();
     }
     }
    
     // term -> mulop factorA term | lambda
     private void term() throws IOException
     {
     if(token.getTag() == '*' || token.getTag() == '/' || token.getTag() == Tag.AND)
     {
     mulop();
     factorA();
     term();
     }
     }
    
     // factorA -> factor | not factor | - factor
     private void factorA() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     case Tag.INTEIRO:
     case Tag.LITERAL:
     case '(':
     factor();
     break;
                
     case Tag.NOT:
     eat(Tag.NOT);
     factor();
     break;
                
     case '-':
     eat('-');
     factor();
     break;
                
     default:
     erro();
     }
     }
    
     // factor -> identifier | constant | ( expression1 )
     private void factor() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.ID:
     eat(Tag.ID);
     break;
            
     case Tag.INTEIRO:
     case Tag.LITERAL:
     constant();
     break;
                
     case '(':
     eat('(');
     expression1();
     eat(')');
     break;
                
     default:
     erro();
     }
     }
    
     private void relop() throws IOException
     {
     switch(token.getTag())
     {
     case Tag.EQ:
     eat(Tag.EQ);
     break;
            
     case '>':
     eat('>');
     break;
                
     case Tag.GE:
     eat(Tag.GE);
     break;
                
     case '<':
     eat('<');
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
    
     private void addop() throws IOException
     {
     switch(token.getTag())
     {
     case '+':
     eat('+');
     break;
            
     case '-':
     eat('-');
     break;
                
     case Tag.OR:
     eat(Tag.OR);
     break;
                
     default:
     erro();
     }
     }
    
     // mulop -> * | / | and
     private void mulop() throws IOException
     {
     switch(token.getTag())
     {
     case '*':
     eat('*');
     break;
            
     case '/':
     eat('/');
     break;
                
     case Tag.AND:
     eat(Tag.AND);
     break;
                
     default:
     erro();
     }
     }
    
     // constant -> intConst | charConst
     private void constant() throws IOException
     {
     switch(token.getTag())
     {       
     case Tag.INTEIRO:
     eat(Tag.INTEIRO);
     break;
                
     case Tag.LITERAL:
     eat(Tag.LITERAL);
     break;
                
     default:
     erro();
     }
     }
    
     */
}
