package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.HashSet;

public class Crawler {

    HashSet<String> urlSet;  //to store urls present on webpage and also eliminate duplicate urls

    int maxDepth=2; //defime max  depth for dfs
    Crawler(){
        urlSet=new HashSet<String>(); //store visited webpages
    }

    //function use DFS
    public void getPageTextAndLinks(String url, int depth){
        //base case if already visited webpage
        if(urlSet.contains(url)){
            return;
        }

        //base case max depth achieved
        if(depth>=maxDepth){
            return;
        }
//
        if(urlSet.add(url)){
           System.out.println(url);
        }

        depth++;
     try {
         Document document =  Jsoup.connect(url).timeout(5000).get(); //conver html object to java document object with help of java jsoup library
         //indexer work start here
         Indexer indexer=new Indexer(document,url);

         System.out.println(document.title());
         Elements availableLinksOnPage = document.select("a[href]"); //storing all links of url webpage in availableLinksOnPage object

         //try to visit each link which was saved in availableLinksOnPage
         for (Element currentLink : availableLinksOnPage) {
             getPageTextAndLinks(currentLink.attr("abs:href"), depth);
         }
     }
     catch (IOException ioException){
         ioException.printStackTrace();
     }
    }
    public static void main(String[] args) {
       Crawler crawler=new Crawler();
       crawler.getPageTextAndLinks("https://www.javatpoint.com",0);
    }
}