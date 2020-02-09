package com.bruce.hw2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    public static void main(String[] args) throws Exception {
        long startTime=System.currentTimeMillis();   //获取开始时间  
        String crawlStorageFolder = "./data/crawl";
        int numberOfCrawlers = 8;
        CrawlConfig config = new CrawlConfig();
        //Configure the Crawler
        config.setCrawlStorageFolder(crawlStorageFolder);
        
        config.setMaxDepthOfCrawling(16);
        config.setMaxPagesToFetch(2000);
        config.setPolitenessDelay(600);
        config.setUserAgentString("collect date for project");
        config.setIncludeBinaryContentInCrawling(true);
        
         /* Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        /*
         * For each crawl, you need to add some seed urls. These are the first URLs that
         * are fetched and then the crawler starts following links which are found in
         * these pages
         */
        //controller.addSeed("https://www.latimes.com/projects/california-oil-well-drilling-idle-cleanup/assets/meta/favicon-16x16.5f113307.png");
        controller.addSeed("https://www.latimes.com");
        
        //controller.addSeed("https://www.latimes.com/projects/la-me-sea-level-rise-california-coast/static/img/interactive.gif");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code will
         * reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
        //open docment

        final String[] FILE_HEADER1 = {"URL","HTTP Status"};
        final String FILE_NAME1 = "fetch_LATIMES.csv";
        final String[] FILE_HEADER2 = {"URL","HTTP Status"};
        final String FILE_NAME2 = "vist_LATIMES.csv";
        final String[] FILE_HEADER3 = {"URL","HTTP Status"};
        final String FILE_NAME3 = "urls_LATIMES.csv";
        FileWriter out1 = new FileWriter(FILE_NAME1);
        CSVFormat format1 = CSVFormat.DEFAULT.withHeader(FILE_HEADER1).withSkipHeaderRecord();
        CSVPrinter printer1 = new CSVPrinter(out1, format1);
        FileWriter out2 = new FileWriter(FILE_NAME2);
        CSVFormat format2 = CSVFormat.DEFAULT.withHeader(FILE_HEADER2).withSkipHeaderRecord();
        CSVPrinter printer2 = new CSVPrinter(out2, format2);
        FileWriter out3 = new FileWriter(FILE_NAME3);
        CSVFormat format3 = CSVFormat.DEFAULT.withHeader(FILE_HEADER3).withSkipHeaderRecord();
        CSVPrinter printer3 = new CSVPrinter(out3, format3);
        
        //handle data
        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        
        List<List<String>> recordList = new ArrayList<>(); 
        for (Object localData : crawlersLocalData) {
            CrawlStat stat = (CrawlStat) localData;
            recordList = stat.getRecord1();
            for(int i =0;i<recordList.size();i++){
                printer1.printRecord(recordList.get(i));
            }
            printer1.flush();
            recordList = stat.getRecord2();
            for(int i =0;i<recordList.size();i++){
                printer2.printRecord(recordList.get(i));
            }
            printer2.flush();
            recordList = stat.getRecord3();
            for(int i =0;i<recordList.size();i++){
                printer3.printRecord(recordList.get(i));
            }
            printer3.flush();
        
        }

        //flush and close the document
        try {
            
            printer1.close();
            printer2.close();
            printer3.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        long endTime=System.currentTimeMillis(); //获取结束时间  
        System.out.println("程序运行时间： "+(endTime-startTime)/60000+"mins");
    }
}