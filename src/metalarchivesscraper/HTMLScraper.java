/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metalarchivesscraper;

/**
 *
 * @author Joseph
 */
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.net.URL;
import java.util.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
   This Class contains functions for retrieving specific data from the website metal-archives.com|
   It utilizes the framework Jsoup to retrieve HTML elements from sections of the page which are then
   applied to GUI Elements
  
*/
public class HTMLScraper{
   
   
   /*
      Method Name: getCoverArt
      
      Function: Retrieves a URL to the album cover image of the selected album and places the image ona label
      
      Description: The method starts by formatting the user inputs to apply to the URL system on metal-archives.com
                   The method then executes the standard Jsoup procedure of loading HMTL, selecting the cover art Image
                   Element, and then setting the Graphic of the input label to that image
   */
    public void getCoverArt(String bandName, String albumName, Label label)throws Exception{
        bandName.replace(" ", "_");
        albumName.replace(" ", "_");
        String url = String.format("https://www.metal-archives.com/albums/%s/%s", bandName, albumName);
	Document document = Jsoup.connect(url).get();
	Element cover = document.select(".image#cover").first();
        String relHref = cover.attr("href");
        String absHref = cover.attr("abs:href");
        Image coverArt = new Image(absHref);
        ImageView artView = new ImageView(coverArt);
        artView.setFitHeight(500);
        artView.setFitWidth(500);
        artView.setSmooth(true);
        label.setGraphic(artView);
}
   
   /*
      Method Name: getBandLogo
    
    Inputs:
        bandName = The name of a metal band found on metal-archives.com
        label = the label you want to apply the found image too
      
      Function: Retrieves a URL to the logo image of a selected band from metal-archives.com and places it on a Label
      
      Description: 
   */
   
   public void getBandLogo(String bandName, Label label) throws Exception{;
      bandName.replace(" ", "_");
      String url = String.format("https://www.metal-archives.com/bands/%s", bandName);
      Document document = Jsoup.connect(url).get();
      Element logo = document.select(".image#logo").first();
      String relHref = logo.attr("href");
      String absHref = logo.attr("abs:href");
      System.out.println(absHref);
      Image bandLogo = new Image(absHref);
      ImageView logoView = new ImageView(bandLogo);
      logoView.setFitHeight(500);
      logoView.setFitWidth(500);
      logoView.setSmooth(true);
      label.setGraphic(logoView);
   }
   
   public void getReleaseDate(String bandName, String albumName, Label label) throws Exception{
      bandName.replace(" ", "_");
      albumName.replace(" ", "_");
      String url = String.format("https://www.metal-archives.com/albums/%s/%s", bandName, albumName);
      Document document = Jsoup.connect(url).get();
      Element date = document.select(".float_left > dd").get(1);
      System.out.println(date.text());
      String dateString = date.text();
      
      label.setText("Release Date: " + dateString);
      

   }
   
   public void getReleaseType(String bandName, String albumName, Label label) throws Exception{
      bandName.replace(" ", "_");
      albumName.replace(" ", "_");
      String url = String.format("https://www.metal-archives.com/albums/%s/%s", bandName, albumName);
      Document document = Jsoup.connect(url).get();
      Element type = document.select(".float_left > dd").get(0);
      System.out.println(type.text());
      String typeString = type.text();
      
      label.setText("Release Type: " + typeString);    
   }
   
   public URL getYouTubeURL(String bandName, String albumName) throws Exception{
      bandName.replace(" ", "+");
      albumName.replace(" ", "+");
      String url = ("https://www.youtube.com/results?search_query="+bandName+" "+albumName).replace(" ", "+");
      URL site = new URL(url);
      
      
      return site;
   }
   
   public void getLineupList(String bandName, String albumName, Label label) throws Exception{
      String lineup = "";
      bandName.replace(" ", "_");
      albumName.replace(" ", "_");
      String url = String.format("https://www.metal-archives.com/albums/%s/%s", bandName, albumName);
      Document document = Jsoup.connect(url).get();
      Elements types = document.select("div#album_members_lineup > div > table > tbody > tr > td > a");
      
      for (Element elm : types){
        System.out.println(elm.text());
        String member = elm.text() + "\n";
        lineup += member;   
      }
      label.setText(lineup);
      

      
   }




}
    

