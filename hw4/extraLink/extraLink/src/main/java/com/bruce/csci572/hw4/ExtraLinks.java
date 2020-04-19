package com.bruce.csci572.hw4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtraLinks

{
    public static void main(String[] args) {
        //output txt
        File output = new File("/Users/Bruce/Desktop/extraLinks.txt");
        
            
        

        // load csv 
        File csv = new File("/Users/Bruce/Desktop/20spring/572/CSCI572-in-USC/hw4/solr-7.7.2/crawl_data/URLtoHTML_latimes_news.csv");
        HashMap<String,String> hashMap = new HashMap<>();
        try{
            BufferedReader textFile = new BufferedReader(new FileReader(csv));
            String lineDta = "";
        
            while ((lineDta = textFile.readLine()) != null){
                //System.out.println(lineDta);
                String str1 = lineDta.split(",")[0];
                String str2 = lineDta.split(",")[1];
                hashMap.put(str2,str1);
            }
            textFile.close();
        }catch (IOException e){
            System.out.println("没有找到指定文件");
        }
        // handle all the document in floder
        String path = "/Users/Bruce/Desktop/20spring/572/CSCI572-in-USC/hw4/solr-7.7.2/crawl_data/latimes/";
        File file1 = new File(path);   
        File[] array = file1.listFiles();  
        try {
            output.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(output));
            for(int i=0;i<array.length;i++){
                if(array[i].isFile()){
                    File file = new File(array[i].getPath() );
                    Document doc  = Jsoup.parse(file,"UTF-8","");
                    Elements links = doc.select("a[href]");
                    for(Element link : links) {
                        if(hashMap.containsKey(link.attr("abs:href"))){
                            out.write(array[i].getName()+" "+hashMap.get(link.attr("abs:href")) +"\n" );
                        }
                    }
                    out.flush();
                }
            }
        out.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        
        
    }
}
