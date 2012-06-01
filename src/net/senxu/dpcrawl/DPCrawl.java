/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.senxu.dpcrawl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

            //System.out.println("contentType:" + contentType);

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
        // TODO 
        //1. build crawl list
        //2. read from crawl list and keep webpages
        //3. parse the stored webpages and store the URLs (of every shop)
        //summery end
        //step 1.
        File seedlist=new File("seedlist.txt");
        String seedDir="seedDir";
        String shopsDir="shopsDir";
        String shopIDCrawled="shopIDCrawled.txt";
        String line="";
        File ListDir=new File(seedDir);
        if (!ListDir.exists())    ListDir.mkdir();
        try {
            BufferedReader br=new BufferedReader(new FileReader(seedlist));
            while ((line=br.readLine())!=null){
                String output=seedDir+System.getProperty("file.separator")+line.replaceAll("/", "_").replaceAll(":", "")+".html";
                StoreURL2File(line, output);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(DPCrawl.class.getName()).log(Level.SEVERE, null, ex);
        }
        //step 2.

        File outputDir=new File (shopsDir);
        File shopIDstored=new File(shopIDCrawled);
        if (!shopIDstored.exists()){
            try {
                shopIDstored.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(DPCrawl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!outputDir.exists()){
        outputDir.mkdir();
        }
        for (File f:ListDir.listFiles()){
            //for every file inside the dir
            //parse f and store the parsed urls in outputDir
            ParseStore(f,outputDir,shopIDstored);
        }
        
        //clean up step:
        //delete seedDir
        for (File f:ListDir.listFiles()){
            System.out.println("Deleting: "+f.toString());
            f.delete();
        }
        ListDir.delete();
        System.out.println("ListDir deleted");
        //delete seedlist.txt
        seedlist.delete();
        System.out.println("seedlist deleted");
        
        
    }

    private static void ParseStore(File f, File outputDir, File shopIDstored) {
        //for file f, parse the useful urls and store the ones that haven't been stored before in oldURL
        
        //read in the oldURL as a list
        ArrayList<String> shopIDList=readArrayListFromFile(shopIDstored);
        
        String shopIDcandidate;
        String regEx="a href=\"/shop/";
        try {
            BufferedReader br=new BufferedReader(new FileReader(f));
            BufferedWriter bwList=new BufferedWriter(new FileWriter(shopIDstored,true));
            String line;
            while ((line=br.readLine())!=null){
                shopIDcandidate=ParseString(line,regEx);
                if (shopIDcandidate!=null){
                    System.out.println("shop found: "+shopIDcandidate);
                    //compare the url
                    if (shopIDList.contains(shopIDcandidate)){//if the url has already been collected
                        System.out.println("which has already been collected.");
                        continue;
                    }else{
                        //write the url to shopIDstored
                        bwList.append(shopIDcandidate);
                        bwList.newLine();
                        System.out.println("yeah! new shop ID added");
                        //and the url to shopIDList
                        shopIDList.add(shopIDcandidate);
                        //store the webpage of the url to outputDir
                        String url="http://www.dianping.com/shop/"+shopIDcandidate;
                        String localfile=outputDir.toString()+System.getProperty("file.separator")+shopIDcandidate;
                        StoreURL2File(url,localfile);
                    }
                }
            }
            bwList.flush();
            bwList.close();
            bwList.close();
            br.close();
                
        } catch (IOException ex) {
            Logger.getLogger(DPCrawl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

    private static ArrayList<String> readArrayListFromFile(File f) {
        BufferedReader br = null;
        try {
            ArrayList<String> ret=new ArrayList<String>();
            br = new BufferedReader(new FileReader(f));
            String line;
            while ((line=br.readLine())!=null){
                ret.add(line);
            }
            return ret;
        } catch (IOException ex) {
            Logger.getLogger(DPCrawl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(DPCrawl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static String ParseString(String line, String regEx) {
        String ret;
        Pattern p=Pattern.compile(regEx+"([\\d]+)");
        if (line.indexOf(regEx)>-1){
            Matcher m=p.matcher(line);
            if (m.find()){
                return m.group(1);
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
