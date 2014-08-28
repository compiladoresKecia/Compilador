/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.util.LinkedList;
import java.util.Stack;
import lexer.Tag;
import lexer.Token;
import lexer.Word;

/**
 * Gerador de Codigo.
 *
 *
 *
 *
 * Por conveniência, é permitido que os nomes que aparecem no programa fonte
 * apareçam também como endereços no código de três endereços
 *
 * @author wolvery
 */
public class generatorCode {

    /**
     * Codigo completo.
     */
    LinkedList<TreeAdressLine> code;
    /**
     * Operador do codigo de 3 enderecos.
     */
    private int operatorTemporario;
    /**
     * Pilha de operadores.
     */
    private Stack<Integer> operatorStack;
    /**
     * Argumentos.
     */
    private String argumentTemporario;
    /**
     * Pilha argumentos.
     */
    
    private Stack<String> pilhaArg;
    /**
     * Resultado da linha do codigo.
     */
    private String resultFinal;

    private final int max_Indice = 31;

    private final String temporario[] = {"t1", "t2", "t3", "t4", "t5", "t6",
        "t7", "t8", "t9", "t10", "t11", "t12", "t13", "t14", "t15", "t16",
        "t17", "t18", "t19", "t20", "t21", "t22", "t23", "t24", "t25", "t26",
        "t27", "t28", "t29", "t30", "t31", "t32"};
    
    private final static int Lado_Esquerdo = 0,Lado_Direito = 1,Lado_Condicao = 2;
    private final String temporario_Condicao[] ={"tl","tr","tCo"};
    
    private int condicionalOperador;

    private int indiceTemporario;

    public generatorCode() {
        code = new LinkedList<>();        
        indiceTemporario = 0;
        pilhaArg = new Stack<>();
        operatorStack = new Stack<>();
        
    }
    public void limparPilhas(){
        indiceTemporario = 0;
        pilhaArg = new Stack<>();
        operatorStack = new Stack<>();
    }
    public void adicionarAtrib() {
        if (indiceTemporario > 0) {
            code.add(new TreeAdressLine(Tag.ATRIB, temporario[indiceTemporario - 1], null, resultFinal));
            indiceTemporario = 0;
        } else {
            code.add(new TreeAdressLine(Tag.ATRIB, argumentTemporario, null, resultFinal));
        }
        limparPilhas();
    }

    public void incrementaIndiceTemporario() {
        indiceTemporario++;
        if (indiceTemporario > max_Indice) {
            indiceTemporario = 1;
        }
    }
    /**
     * Obtem os valores da pilha.
     *
     */
    public void remover1lancePilha(){            
            this.operatorTemporario = this.obterOperatorDaPilha();
            this.argumentTemporario = this.obterArgumentPilha();
    }
    public void adicionarAssigmentExpression(String arg) {
        if (indiceTemporario == 0) {
            //Importante para retirar o ultimo lance caso exista apenas 1 na pilha
            remover1lancePilha();
            code.add(new TreeAdressLine(this.getOperatorTemporario(), 
                    arg, this.argumentTemporario, temporario[indiceTemporario]));
        }else {
            remover1lancePilha();
            code.add(new TreeAdressLine(this.getOperatorTemporario(), 
                     this.argumentTemporario,temporario[indiceTemporario-1]
                    , temporario[indiceTemporario]));
        }
        
        incrementaIndiceTemporario();
    }

    public void adicionarIFStatement() {

    }
    
    public void adicionarLadoEsquerdoCondicao(){
        code.add(new TreeAdressLine(Tag.ATRIB,temporario[indiceTemporario-1] , null, temporario_Condicao[Lado_Esquerdo]));
        limparPilhas();
    }

    public void adicionarCondicao() {
        code.add(new TreeAdressLine(Tag.ATRIB,temporario[indiceTemporario-1] , null, temporario_Condicao[Lado_Direito]));
        code.add(new TreeAdressLine(operatorTemporario, 
                temporario_Condicao[Lado_Esquerdo], 
                temporario_Condicao[Lado_Direito],
                temporario_Condicao[Lado_Condicao]));
        limparPilhas();

    }

    public void adicionarCodigo(int tag) {
        code.add(new TreeAdressLine(tag, this.argumentTemporario, null, null));
        limparPilhas();
    }

    public LinkedList<TreeAdressLine> getCode() {
        return code;
    }

    public void setCode(LinkedList<TreeAdressLine> code) {
        this.code = code;
    }
    public int obterOperatorDaPilha(){
        return operatorStack.pop();
    }
    public int getOperatorTemporario() {
        return operatorTemporario;
    }

    public void setOperatorTemporario(int operatorTemporario) {
        this.operatorTemporario = operatorTemporario;
        operatorStack.push(operatorTemporario);
    } 
    
    
    public String obterArgumentPilha(){
        return pilhaArg.pop();
    }

    public String getArgumenTemporario() {
        return argumentTemporario;
    }

    public void setArgumentTemporario(String argument1Temporario) {
        this.argumentTemporario = argument1Temporario;
        pilhaArg.push(argument1Temporario);
    }

    public String getResultFinal() {
        return resultFinal;
    }

    public void setResultFinal(String resultFinal) {
        this.resultFinal = resultFinal;
    }

    public int getIndiceTemporario() {
        return indiceTemporario;
    }

    public void setIndiceTemporario(int indiceTemporario) {
        this.indiceTemporario = indiceTemporario;
    }

    public int getCondicionalOperador() {
        return condicionalOperador;
    }

    public void setCondicionalOperador(int condicionalOperador) {
        this.condicionalOperador = condicionalOperador;
    }
    

}
