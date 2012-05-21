/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.listgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sxu
 */
public class ListGenerator {
    
    public static void main(String[] args){
        File f=new File("seedlist.txt");
        String pref="http://www.dianping.com/search/category/2/10/p"; 
        int start=1;
        int end=50;
        ListGenForDP(pref, start, end, f);
    }

    private static void ListGenForDP(String pref, int start, int end, File f) {
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(f));
            for (int i=start;i<end;i++){
                bw.append(pref+i);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ListGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
