/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.dpcrawl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author sxu
 */
public class DPCrawl {

    /**
     * @param args the command line arguments
     */
    

    public static void StoreURL2File(String link, String localfile) throws MalformedURLException, IOException{
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

            URL url = new URL(link);

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");

            String contentType = urlConn.getContentType();

            System.out.println("contentType:" + contentType);

            InputStream is = urlConn.getInputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(localfile));
            byte[] buf = new byte[256];  
            int n = 0;  
            while ((n=bis.read(buf))>=0){
                bos.write(buf, 0, n);  
            }
            bos.flush();
            bos.close();
            bis.close();

    }

    
    public static void main(String[] args){
        // TODO 1. build crawl list
        //2. read from crawl list and keep webpages
        //3. parse the stored webpages and store the 
        File seedlist=new File("seedlist.txt");
        String seedDir="seedDir";
        String line="";
        try {
            BufferedReader br=new BufferedReader(new FileReader(seedlist));
            while ((line=br.readLine())!=null){
                String output=seedDir+System.getProperty("file.separator")+line.replaceAll("/", "_").replaceAll(":", "")+".html";
                StoreURL2File(line, output);
            }
        } catch (IOException ex) {
            Logger.getLogger(DPCrawl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
}
