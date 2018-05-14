/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metalarchivesscraper;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;
import java.io.FileInputStream;

/**
 *
 * @author Joseph Eubanks
 */
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.net.URL;
import java.util.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import java.io.InputStream;
import java.io.InputStreamReader;




/*
   This Class contains functions for retrieving specific data from the website metal-archives.com|
   It utilizes the framework Jsoup to retrieve HTML elements from sections of the page which are then
   applied to GUI Elements
  
*/
public class HTMLScraper{
   
    
    private static final String APPLICATION_NAME = "MetalArchivesScraper";
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), "./credentials/metalarchivesscraper" );
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport HTTP_TRANSPORT;
    private static final List<String> SCOPES = Arrays.asList(YouTubeScopes.YOUTUBE_READONLY);
    
    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        }
        catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    public static Credential authorize() throws Exception {
        InputStreamReader in = new InputStreamReader(new FileInputStream("src/metalarchivesscraper/client_secret.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, in);
        
        GoogleAuthorizationCodeFlow flow = 
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }
    
    public static YouTube getYouTubeService() throws Exception {
        Credential credential = authorize();
        return new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
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
   
    public void getYouTubeURL(String bandName, String albumName, WebView webview) throws Exception{
        Properties properties = new Properties();
        String apiKey = properties.getProperty("youtube.apikey");
        YouTube youtube = getYouTubeService();
        YouTube.Search.List videoList = youtube.search().list("id, snippet");
        videoList.setQ(bandName+" "+albumName);
        videoList.setOrder("relevance");
        videoList.setKey(apiKey);
        
        SearchListResponse response = videoList.execute();
        SearchResult video = response.getItems().get(0);
        System.out.println(videoList);
        System.out.println(video.getId().getVideoId());
        System.out.println(video.getSnippet().getTitle());
        
        String videoID = video.getId().getVideoId();
        
        bandName.replace(" ", "+");
        albumName.replace(" ", "+");
       
        String videoEmbedURL = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/"+ videoID+"\"frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>";

        webview.getEngine().loadContent(videoEmbedURL);
      
     
   }
   
   public void getLineupList(String bandName, String albumName, Label label) throws Exception{
      String lineup = "";
      bandName.replace(" ", "_");
      albumName.replace(" ", "_");
      String url = String.format("https://www.metal-archives.com/albums/%s/%s", bandName, albumName);
      Document document = Jsoup.connect(url).get();
      Elements types = document.select("div#album_members_lineup > div > table > tbody > tr > td");
      for (int i = 0; i < types.size(); i+=2){
        System.out.println(types.get(i).text() + types.get(i+1).text());
        String member = types.get(i).text() + " ("+ types.get(i+1).text() + ")\n";
        lineup += member;   
      }
      label.setText(lineup);
      
   }
   
   public void getBandName(String bandName, Label bandNameLabel, Label bottomPaneLabel) throws Exception{
        String lineup = "";
        bandName.replace(" ", "_");

        String url = String.format("https://www.metal-archives.com/bands/%s", bandName);
        Document document = Jsoup.connect(url).get();
        Element type = document.select("#band_info > h1 > a").first();
        bandNameLabel.setText("Band Name: " + type.text());
        bottomPaneLabel.setText(type.text());
   }
   
   public void getTrackList(String bandName, String albumName, Label trackLabel) throws Exception {
      String tracks = "";
      bandName.replace(" ", "_");
      albumName.replace(" ", "_");
      String url = String.format("https://www.metal-archives.com/albums/%s/%s", bandName, albumName);
      Document document = Jsoup.connect(url).get();
      Elements types = document.select(".wrapwords");
      for (Element track : types){
          tracks+=track.text() + "\n";
      }
      trackLabel.setText(tracks);
   }

   




}
    

