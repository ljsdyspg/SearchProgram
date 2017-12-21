package search.impl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import search.Parser;
import search.WebSpider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSpiderImpl implements WebSpider{
    public File urls = new File("urls.txt");
    public PrintStream psUrls = null;

    @Override
    public Parser getParser() {
        return new ParserImpl();
    }

    @Override
    public List<String> getHtmlFromWeb() {
        List<String> URLs = new ArrayList<>();
        try {
            psUrls = new PrintStream(new FileOutputStream(urls,false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String URL="http://search.qmul.ac.uk/s/search.html?collection=queenmary-coursefinder-undergraduate&query=&sort=title&start_rank=";
        String url=null;
        for(int i=0;i<32;i++) {
            url = URL + i + "1";
            try {
                Document URLDoc = Jsoup.connect(url).get();

                Elements h4 = URLDoc.getElementsByTag("h4");
                Document h4Doc = Jsoup.parse(h4.toString());
                Elements link = h4Doc.select("a[href]");
                String links = link.toString();
                Pattern linkPattern = Pattern.compile("(?<=title=\").*(?=\">)");
                Matcher matcherLink = linkPattern.matcher(links);
                MatchResult Program = null;
                while (matcherLink.find()) {
                    Program = matcherLink.toMatchResult();
                    System.out.println(Program.group());
                    psUrls.println("Program: "+Program.group());
                    URLs.add(Program.group().toString());//存入URLs中
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return URLs;
    }
}
