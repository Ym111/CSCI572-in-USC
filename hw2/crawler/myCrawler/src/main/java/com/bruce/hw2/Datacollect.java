package com.bruce.hw2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Datacollect {
    public static void main(String[] arg) throws IOException {
        //
        final String[] FILE_HEADER1 = { "URL", "HTTP Status" };
        final String FILE_NAME1 = "fetch_LATIMES.csv";
        final String[] FILE_HEADER2 = { "URL", "Size","# of outlinks","Content-type" };
        final String FILE_NAME2 = "vist_LATIMES.csv";
        final String[] FILE_HEADER3 = { "URL", "Reside in website or Not" };
        final String FILE_NAME3 = "urls_LATIMES.csv";

        CSVFormat format1 = CSVFormat.DEFAULT.withHeader(FILE_HEADER1).withSkipHeaderRecord();
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader(FILE_HEADER2).withSkipHeaderRecord();
        CSVFormat format3 = CSVFormat.DEFAULT.withHeader(FILE_HEADER3).withSkipHeaderRecord();

        Reader in1 = new FileReader(FILE_NAME1);//fetch
        Iterable <CSVRecord> records1 = format1.parse(in1);
        int fetch_attempt = 0;
        int ar[] = new int[7]; // code status 
        //0 for 202; 1 for 301; 2 for 302; 3 for 403; 4 for 404;504 for 5
        for(CSVRecord record : records1){
            int code = Integer.valueOf(record.get("HTTP Status"));
            
            fetch_attempt++;
            if(code == 200) ar[0]++;
            else if(code == 301) ar[1]++;
            else if (code ==302) ar[2]++;
            else if(code == 403) ar[3]++;
            else if (code ==404) ar[4]++;
            else if (code ==504) ar[5]++;
            else ar[6]++;
        }


        Reader in2 = new FileReader(FILE_NAME2);// visit
        Iterable <CSVRecord> records2 = format2.parse(in2);
        int fetch_success = 0;
        int outlinks_sum =0;
        int sar[] = new int[7]; // code status 
        //0 for 1kb; 1 for 10kb; 2 for 100kb; 3 for 1m; 4 for greater;
        int tar[] = new int[7]; // code status 
       
        for(CSVRecord record : records2){
            //
            fetch_success++;
            int size = Integer.valueOf(record.get("Size"));
            outlinks_sum += Integer.valueOf(record.get("# of outlinks"));
            String type = record.get("Content-type");
            if(size<1024) sar[0]++;
            else if(size <10240) sar[1]++;
            else if(size <102400) sar[2]++;
            else if(size <1024000) sar[3]++;
            else sar[4]++;

            switch(type){
                case "text/html": tar[0]++;break;
                case "image/gif": tar[1]++;break;
                case "image/jpeg": tar[2]++;break;
                case "image/png": tar[3]++;break;
                case "image/svg+xml": tar[4]++;break;
                case "image/vnd.microsoft.icon": tar[5]++;break;
                default:
                    tar[6]++;
            }
        }



        Reader in3 = new FileReader(FILE_NAME3);//urls
        Iterable <CSVRecord> records3 = format3.parse(in3);
        int extrat_url = 0;
        int u_ok =0;
        int u_nok=0;
        HashSet<String> h = new HashSet<String>();
        for(CSVRecord record : records3){
            extrat_url++;
            String url = record.get("URL");
            String s_code = record.get("Reside in website or Not");
            if(!h.contains(url)){
                h.add(url);
                if(s_code.equals("OK")) u_ok++;
                else u_nok++;
            }
        }





        //document output 
        final String FILE_TXT = "CrawlReport_latimes.txt";
        FileWriter out = new FileWriter(FILE_TXT);

        out.write("Name: Yiming Chen\nUSC ID: 9600260082\nNews site crawled: latimes.com\n\nFetch Statistics\n================");
        out.write("\n# fetches attempted:"+fetch_attempt );
        out.write("\n# fetches succeeded:"+fetch_success);
        out.write("\n# fetches failed or aborted:"+(fetch_attempt-fetch_success));
        //
        out.write("\n\nOutgoing URLs\n==============");
        out.write("\nTotal URLs extracted:"+extrat_url );
        out.write("\n# unique URLs extracted:"+h.size() );
        out.write("\n# unique URLs within News Site:"+u_ok);
        out.write("\n# unique URLs outside News Site:"+u_nok);
        if(u_ok + u_nok != h.size())out.write("\n !=>not equal");
        if(outlinks_sum!=extrat_url)out.write("\n!=>Visit_url get urls:" + outlinks_sum);


        out.write("\n\nStatus Codes\n=============");
        out.write("\n200:"+ar[0] );
        out.write("\n301:"+ar[1] );
        out.write("\n302:"+ar[2] );
        out.write("\n403:"+ar[3] );
        out.write("\n404:"+ar[4] );
        out.write("\n504:"+ar[5] );
        if(ar[6]!=0)out.write("\n!=>others:"+ar[6] );
        
        out.write("\n\nFile Sizes\n===========");
        out.write("\n< 1kb:"+sar[0] );
        out.write("\n1KB ~ <10KB:"+sar[1] );
        out.write("\n10KB ~ <100KB:"+sar[2] );
        out.write("\n100KB ~ <1MB:"+sar[3] );
        out.write("\n>= 1MB:"+sar[4] );

        out.write("\n\nContent Types\n==============");
        out.write("\ntext/html:"+tar[0] );
        out.write("\nimage/gif:"+tar[1] );
        out.write("\nimage/jpeg:"+tar[2] );
        out.write("\nimage/png:"+tar[3] );
        out.write("\nimage/svg+xml:"+tar[4] );
        out.write("\nimage/vnd.microsoft.icon:"+tar[5] );
        if(tar[6]!=0)out.write("\n!=>others:"+tar[6] );

        out.close();




    }
}