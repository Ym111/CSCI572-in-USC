package com.bruce.hw2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css" + "|mp3|zip|gz|rss|js))$");

    // private final static Pattern DOCUM_PATTERN = Pattern.compile(".*(\\.(
    // |pdf|html|HTML|doc" + "|bmp|gif|jpe?g|png|tiff?))$");
    // Limit your crawler so it only visits HTML, doc, pdf and different image
    // format URLs and record the
    // meta data for those file types

    /**
     * This method receives two parameters. The first parameter is the page in which
     * we have discovered this new url and the second parameter is the new url. You
     * should implement this function to specify whether the given url should be
     * crawled or not (based on your crawling logic). In this example, we are
     * instructing the crawler to ignore urls that have css, js, git, ... extensions
     * and to only accept urls that start with "http://www.viterbi.usc.edu/". In
     * this case, we didn't need the referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (href.startsWith("https://www.latimes.com/") || href.startsWith("http://www.latimes.com/")
                || href.startsWith("https://latimes.com/") || href.startsWith("http://latimes.com/")) {
            List<String> record1 = new ArrayList<>();
            record1.add(String.valueOf(url));
            record1.add(String.valueOf("OK"));
            myCrawlStat.task3(record1);
            // myCrawlStat.task3(String.valueOf(url), 1);
        } else {
            List<String> record1 = new ArrayList<>();
            record1.add(String.valueOf(url));
            record1.add(String.valueOf("N_OK"));
            myCrawlStat.task3(record1);
        }

        // return DOCUM_PATTERN.matcher(href).matches() &&
        // href.startsWith("https://www.latimes.com/");
        /*
         * if(href.startsWith("https://www.latimes.com/") ||
         * href.startsWith("http://www.latimes.com/")||
         * href.startsWith("https://latimes.com/")){
         * myCrawlStat.task3(String.valueOf(url), 1); }else{
         * myCrawlStat.task3(String.valueOf(url), 0); }
         */
        return !FILTERS.matcher(href).matches() && href.startsWith("https://www.latimes.com/");

    }

    /*
     * This part of function is to collect statistics of Webpage using the CrawlStat
     * class to satisfy the demand
     */
    CrawlStat myCrawlStat;

    public MyCrawler() {
        myCrawlStat = new CrawlStat();
    }

    /**
     * This function is called when a page is fetched and ready to be processed by
     * your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if( url.indexOf(".js") != -1) return;
        // System.out.println("URL: " + url);
        if (page.getParseData() instanceof HtmlParseData) { // html
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            ParseData parseData = page.getParseData();
            try {
                List<String> record1 = new ArrayList<>();
                record1.add(url);
                record1.add(String.valueOf(htmlParseData.getText().getBytes("UTF-8").length));
                record1.add(String.valueOf(parseData.getOutgoingUrls().size()));
                record1.add(page.getContentType().replace(";charset=UTF-8", ""));
                myCrawlStat.task2(record1);

                // myCrawlStat.task2(url, htmlParseData.getText().getBytes("UTF-8").length,
                // parseData.getOutgoingUrls().size(),
                // page.getContentType().replace(";charset=UTF-8",""));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if ((page.getParseData() instanceof BinaryParseData)) { // binary data
            // BinaryParseData binaryParseData = (BinaryParseData) page.getParseData();
            ParseData parseData = page.getParseData();
            List<String> record1 = new ArrayList<>();
            record1.add(url);
            record1.add(String.valueOf(page.getContentData().length));
            record1.add(String.valueOf(parseData.getOutgoingUrls().size()));
            record1.add(page.getContentType().replace(";charset=UTF-8", ""));
            myCrawlStat.task2(record1);
            // myCrawlStat.task2(url, page.getContentData().length,
            // parseData.getOutgoingUrls().size(),
            // page.getContentType().replace(";charset=UTF-8",""));

        }

        // static

    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        // TODO Auto-generated method stub
        List<String> record1 = new ArrayList<>();
        record1.add(webUrl.getURL());
        record1.add(String.valueOf(statusCode));
        myCrawlStat.task1(record1);
        super.handlePageStatusCode(webUrl, statusCode, statusDescription);
    }

    @Override
    public Object getMyLocalData() {
        return myCrawlStat;
    }

}