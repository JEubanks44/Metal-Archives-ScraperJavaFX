/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metalarchivesscraper;

import com.sun.prism.paint.Color;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.jsoup.*;

/**
 *
 * @author Joseph Eubanks
 */



public class MetalArchivesScraper extends Application implements EventHandler<ActionEvent> {
    
    Stage Window;
    
    Label askBandLabel;
    Label askAlbumLabel;
    
    Label bandNameLabel;
    Label albumNameLabel;
    Label releaseDateLabel;
    Label releaseTypeLabel;
    Label lineupTextLabel;
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
    VBox leftPane;
    VBox rightPane;
    HBox bottomPane;
    
    BorderPane rootPane;
    
    
    
    ScrollPane scrollBox;
    
    
    
    
    
    @Override
    public void start(Stage primaryStage) {
        Window = primaryStage;
        
        Window.setWidth(850);
        Window.setHeight(850);
        Window.setTitle("Metal-Archives Scraper");
        
        Font.loadFont(getClass().getResourceAsStream("Fonts/Norse.otf"), 10);
        
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
        controlPane.getStyleClass().add("control-pane");
        controlPane.setAlignment(Pos.CENTER);
        
        
        bandNameLabel = new Label("Band Name");
        albumNameLabel = new Label("Album Name");
        releaseDateLabel = new Label("Release Date");
        releaseTypeLabel = new Label("Release Type");
        bandLogoTextLabel = new Label("Band Logo: ");
        bandLogoLabel = new Label("");
        coverArtTextLabel = new Label("Cover Art: ");
        coverArtLabel = new Label("");
        lineupTextLabel = new Label("Lineup:");
        lineupLabel = new Label("");
        
        infoPane = new VBox();
        infoPane.getChildren().addAll(bandNameLabel, bandLogoTextLabel, bandLogoLabel, albumNameLabel, releaseDateLabel, releaseTypeLabel, coverArtTextLabel, coverArtLabel, lineupTextLabel, lineupLabel);
        infoPane.setAlignment(Pos.CENTER);
        infoPane.setFillWidth(true);
        infoPane.getStyleClass().add("info-pane");
        
        scrollBox = new ScrollPane();
        scrollBox.setContent(infoPane);
        scrollBox.setFitToHeight(false);
        scrollBox.setFitToWidth(true);
        
        leftPane = new VBox();
        rightPane = new VBox();
        bottomPane = new HBox();
        
        
        rootPane = new BorderPane();
        rootPane.setTop(controlPane);
        rootPane.setCenter(scrollBox);
        rootPane.setLeft(leftPane);
        rootPane.setRight(rightPane);
        rootPane.setBottom(bottomPane);
        
        
        Scene scene = new Scene(rootPane, 850, 850);
        scene.getStylesheets().add(MetalArchivesScraper.class.getResource("Stylesheets/mainTheme.css").toExternalForm());
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
