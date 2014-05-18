/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package carregarArquivo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import javax.swing.JFileChooser;

/**
 * Carregar e ler arquivo.
 * @author Guilherme
 */
public class CarregarTXT {
    /**
     * Arquivo para escrita
     */
    private FileReader arquivoEscrita;  
    /**
     * Arquivo para gravacao
     */
    private FileWriter arquivoGravar;  
    
   
    
     /**
      * Leitura do arquivo para um filereader.
      * @return FileReader
      */
    public FileReader abrirArquivo()  {
        FileReader arquivoLido = null;  //Arquivo para escrita
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
}