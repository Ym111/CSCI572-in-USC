package com.bruce.csci572.hw5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.SAXException;

/**
 * parse words in html and output a big.txt
 *
 */
public class ParseWord {
    public static void main(String[] args) throws IOException, SAXException, TikaException {
        //

        PrintWriter writer = new PrintWriter("big.txt");
        String dirPath = "/Users/Bruce/Desktop/20spring/572/CSCI572-in-USC/hw4/solr-7.7.2/crawl_data/latimes";
        File dir = new File(dirPath);
        File[] docs = dir.listFiles();
        Integer cnt=0;
        for (int i = 0; i < docs.length; i++) {
            File doc = docs[i];
            // different tyoe
            cnt++;
                BodyContentHandler handler = new BodyContentHandler(-1);
                Metadata metadata = new Metadata();
                ParseContext pcontext = new ParseContext();
                FileInputStream inputstream = new FileInputStream(doc);
                HtmlParser htmlparser = new HtmlParser();

                htmlparser.parse(inputstream, handler, metadata, pcontext);
                String line = handler.toString();
                String [] words = line.split(" ");
                // output file
                for(int j = 0;j<words.length;j++){
                    String word = words[j];
                    if(word.matches("[a-zA-Z]+\\.?"))
                    writer.print(word + " ");
                }
                System.out.println(cnt);
        }
        writer.close();

    }
}
