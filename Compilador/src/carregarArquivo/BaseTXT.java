/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carregarArquivo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 * Carregar e ler arquivo.
 *
 * @author Guilherme
 */
public class BaseTXT {

    /**
     * Arquivo para escrita
     */
    private FileReader arquivoLido;
    /**
     * Arquivo para gravacao
     */
    private FileWriter arquivoGravar;

    public BaseTXT() {
        this.arquivoLido = abrirArquivo();
        try {
            arquivoGravar = new FileWriter("LOG.txt", true);
        } catch (IOException ex) {
            Logger.getLogger(BaseTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Leitura do arquivo para um filereader.
     *
     * @return FileReader
     */
    private FileReader abrirArquivo() {
        
        // desejavel um try catch para o arquivo
        // Cria um "abridor" de arquivo. 
        // O caminho é o parâmetro
        // "." siginifica diretório corrente
        JFileChooser chooser = new JFileChooser(".");
        /*
         FileNameExtensionFilter txtfilter = new FileNameExtensionFilter(
         "txt files (*.txt)", "txt");
         chooser.setFileFilter(txtfilter);
         chooser.setDialogTitle("Open schedule file");
         // set selected filter
         chooser.setFileFilter(txtfilter);
         */
        //Abre a Janela de Diálogo
        int flagRead;
        do {
            flagRead = chooser.showOpenDialog(null);
        } while (flagRead != JFileChooser.APPROVE_OPTION);
        try {
            arquivoLido = new FileReader(chooser.getSelectedFile());
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado");
        }

        //System.out.println("\nArquivo selecionado: " + nomeArquivo +"");
        //System.out.println("Caminho do Arquivo selecionado: " + caminho + "\n");
        return arquivoLido;
        //return caminho;  
    }

    /**
     * Proximo caracter.
     *
     * @return proximo caracter do arquivo
     */
    public char readch() throws IOException {
        return (char) arquivoLido.read();
        
        
    }

    /**
     * obter o arquivoEscrita.
     *
     * @return
     */
    public FileReader getArquivoEscrita() {
        return arquivoLido;
    }

    /**
     * Gravacao da string..
     * @param texto a ser gravado
     * @param acabou o fim do arquivo?
     */
    public void escreverArquivo(String texto, boolean acabou) {
        //WriteFile
        PrintWriter escreve_linha = new PrintWriter(arquivoGravar, true);
        //escreve_linha.printf("%n" +"  "+ "%s" + "  " + "%n",texto);
        escreve_linha.println(texto);
        if (acabou) {
            escreve_linha.close();
            System.out.println("acabou de ler! ");
        }

    }
    
    public boolean arquivoLidoPronto() throws IOException{
        return arquivoLido.ready();
    }

}
