/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metalarchivesscraper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jsoup.*;

/**
 *
 * @author Joseph
 */



public class MetalArchivesScraper extends Application implements EventHandler<ActionEvent> {
    
    Stage Window;
    
    Label askBandLabel;
    Label askAlbumLabel;
    
    Label bandNameLabel;
    Label albumNameLabel;
    Label releaseDateLabel;
    Label releaseTypeLabel;
    Label lineupLabel;
    Label coverArtLabel;
    Label coverArtTextLabel;
    Label bandLogoLabel;
    Label bandLogoTextLabel;
    
    TextField bandNameTextField;
    TextField albumNameTextField;
    
    Button loadInfoButton;
    
    HBox controlPane;
    VBox infoPane;
    VBox rootPane;
    
    
    
    ScrollPane scrollBox;
    
    
    
    @Override
    public void start(Stage primaryStage) {
        Window = primaryStage;
        
        Window.setWidth(850);
        Window.setHeight(850);
        Window.setTitle("Metal-Archives Scraper");
        
        Image loadInfoButtonIcon = new Image(getClass().getResourceAsStream("Assets/LoadCover.png"));
        loadInfoButton = new Button("Load Info");
        loadInfoButton.setPrefSize(120, 40);
        loadInfoButton.setOnAction(this);
        askBandLabel = new Label("Band Name");
        askBandLabel.setPrefSize(120, 40);
        bandNameTextField = new TextField();
        bandNameTextField.setPrefSize(200, 40);
        askAlbumLabel = new Label("Album Name");
        askAlbumLabel.setPrefSize(120, 40);
        albumNameTextField = new TextField();
        albumNameTextField.setPrefSize(200,40);
        
        
        controlPane = new HBox();
        controlPane.getChildren().addAll(loadInfoButton, askBandLabel, bandNameTextField, askAlbumLabel, albumNameTextField);
        controlPane.setSpacing(10);
        
        
        bandNameLabel = new Label("Band Name");
        albumNameLabel = new Label("Album Name");
        releaseDateLabel = new Label("Release Date");
        releaseTypeLabel = new Label("Release Type");
        bandLogoTextLabel = new Label("Band Logo: ");
        bandLogoLabel = new Label();
        coverArtTextLabel = new Label("Cover Art: ");
        coverArtLabel = new Label();
        
        infoPane = new VBox();
        infoPane.getChildren().addAll(bandNameLabel, bandLogoTextLabel, bandLogoLabel, albumNameLabel, releaseDateLabel, releaseTypeLabel, coverArtTextLabel, coverArtLabel);
        infoPane.setAlignment(Pos.CENTER);
        
        scrollBox = new ScrollPane();
        scrollBox.setContent(infoPane);
        
        
        
        
        rootPane = new VBox();
        rootPane.getChildren().addAll(controlPane, scrollBox);
        Scene scene = new Scene(rootPane, 850, 850);
        Window.setScene(scene);
        Window.show();
        
        
    }
    
    @Override
    public void handle(ActionEvent event){
        HTMLScraper scraper = new HTMLScraper();
        if(event.getSource()==loadInfoButton) {
            System.out.println("Load _Info_Button_Pressed");
            try {
                
                String bandName = bandNameTextField.getText().trim();
                String albumName = albumNameTextField.getText().trim();
                
                scraper.getCoverArt(bandName, albumName, coverArtLabel);
                scraper.getBandLogo(bandName, bandLogoLabel);
                scraper.getLineupList(bandName, albumName, lineupLabel);
                scraper.getReleaseDate(bandName, albumName, releaseDateLabel);
                scraper.getReleaseType(bandName, albumName, releaseTypeLabel);
                
            }
            catch (Exception e1) {
                
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
