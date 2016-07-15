/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treebplus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author César
 */
public class BPlusTree {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException{
        File fD = new File("E:\\Dados.txt");
        fD.createNewFile();
        File fI = new File("E:\\Indice.txt");
        fI.createNewFile();

        try (FileWriter arqDados = new FileWriter("E:\\Dados.txt")) {
            PrintWriter gravarArqDados = new PrintWriter(arqDados);
            gravarArqDados.write("");
            gravarArqDados.printf("V100|Don Leurindo|Mertix|2013|Brasil%n");
            gravarArqDados.printf("V102|São Francisco|Mertix|2007|Brasil%n");
            gravarArqDados.printf("V105|São Braz|Mertix|2013|Brasil%n");
            gravarArqDados.printf("V107|Santo Antônio|Mertix|1999|Brasil%n");
            gravarArqDados.printf("V108|Vinho Portugal|Mertix|2012|Brasil%n");
            gravarArqDados.printf("V109|Vinho Argentina|Mertix|2014|Brasil%n");
        } 
        
        try (FileWriter arqIndice = new FileWriter("E:\\Indice.txt")) {
            PrintWriter gravarArqIndice = new PrintWriter(arqIndice);
            gravarArqIndice.write("");
            gravarArqIndice.printf("2013,L,3,EInd%n");
            gravarArqIndice.printf("2013,R,5,EInd%n");
            gravarArqIndice.printf("2012,L,7,EInd%n");
            gravarArqIndice.printf("2012,R,9,EInd%n");
            gravarArqIndice.printf("2013,L,10,EInd%n");
            gravarArqIndice.printf("2013,R,11,EInd%n");
            gravarArqIndice.printf("1999,LE,1,EDad%n");
            gravarArqIndice.printf("2007,LE,2,EDad%n");
            gravarArqIndice.printf("2012,LE,3,EDad%n");
            gravarArqIndice.printf("2013,LE,4,EDad%n");
            gravarArqIndice.printf("2013,LE,5,EDad%n");
            gravarArqIndice.printf("2014,LE,6,EDad%n");
        }
        
        new Principal().setVisible(true);
    }
    
}
