package sample;


import javafx.application.Application;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import javax.swing.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {

    String Filepath ;

    @Override
    public void start(Stage Stage) throws Exception{


        TextField ta = new TextField();

        ta.setMaxWidth(300);
        PasswordField pas = new PasswordField();
        pas.setMaxWidth(300);

        Label intro = new Label(" Login Page");
        intro.getStyleClass().add("intro");
        Label text = new Label("Username:  ");
        Label password = new Label("Password:   ");
        Button btn = new Button("Log in");
        Text value = new Text();
        value.setStyle("-fx-font-size:13px;");
        VBox vbox = new VBox();
        HBox hbox1 = new HBox();
        hbox1.getChildren().addAll(text,ta);
        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(password,pas);
        hbox1.setAlignment(Pos.CENTER);
        hbox2.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        vbox.setPadding(new Insets(10,10,10,10));
        vbox.getChildren().addAll(intro,hbox1,hbox2, btn,value );

        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox,500, 500);
        scene.getStylesheets().add("style.css");

        BorderPane layout2 = new BorderPane();

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu view = new Menu("view");
        Menu Format = new Menu("Format");
        Menu helpMenu = new Menu("Help");


        Menu open = new Menu("open... ");
        MenuItem open_doc = new MenuItem("open doc ");
        MenuItem open_pdf = new MenuItem("open pdf");

        MenuItem New = new MenuItem("New ");
        MenuItem save = new MenuItem("Save...");
        MenuItem Exit = new MenuItem("exit ");
        MenuItem copy = new MenuItem("Copy ");
        MenuItem paste = new MenuItem("Paste ");

        open.getItems().addAll(open_doc,open_pdf);
        FileChooser fileChooser = new FileChooser();
        FileChooser fileChooser1 = new FileChooser();
        FileChooser fileChooser2 = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                 new FileChooser.ExtensionFilter("TXT File", "*.txt"),
                new FileChooser.ExtensionFilter("Doc Files", "*.docx"),
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")

        );



        Exit.setOnAction( e-> Platform.exit());

        TextArea textArea = new TextArea();

        textArea.setMaxWidth(500);
        textArea.setMaxHeight(500);
        textArea.setPrefColumnCount(20);

        save.setOnAction( e-> {
            String textInput = textArea.getText();
            try {
                File file =  fileChooser.showSaveDialog(Stage);

                BufferedWriter bw = new BufferedWriter( new FileWriter( file));
                bw.write(textInput);
                bw.close();
            } catch (IOException ioException) {
                return;
            }

        });
            paste.setOnAction( e-> {
                textArea.appendText(Filepath);
            });
        AtomicReference<String> store = new AtomicReference<>("");
        open_doc.setOnAction(e -> {
            textArea.setText("");
            File file = fileChooser1.showOpenDialog(Stage);
            store.set(file.getPath());

            try {
                Stage.setTitle(file.getName() + " -Text Editor");
                FileInputStream fileInputStream = new FileInputStream(file.getPath());

                HWPFDocument doc = new HWPFDocument(fileInputStream);
                WordExtractor wordExtractor = new WordExtractor(doc);
                String doc_txt = wordExtractor.getText();
                textArea.setText(doc_txt);




            } catch (FileNotFoundException t){
                t.printStackTrace();

            }
            catch (IOException ioException){
                ioException.printStackTrace();
            }

        });

        open_pdf.setOnAction(e -> {
            textArea.setText("");
            File openFile = fileChooser2.showOpenDialog(Stage);
            store.set(openFile.getPath());

            try {

                Stage.setTitle(openFile.getName() + " -Text Editor");
                FileInputStream fileInputStream = new FileInputStream(openFile.getPath());

                PDDocument document = Loader.loadPDF(fileInputStream);

                if (!document.isEncrypted()) {
                    PDFTextStripper tStripper = new PDFTextStripper();
                    String pdfFileInText = tStripper.getText(document);
                    String lines[] = pdfFileInText.split("\\r?\\n");
                    for (String line : lines) {
                        textArea.setText(line);
                    }
                }
//




            } catch (FileNotFoundException t){
                t.printStackTrace();

            }
            catch (IOException ioException){
                ioException.printStackTrace();
            }

        });
        copy.setOnAction( e-> {

            try {
                Filepath = "";
                File newFile = new  File(path);
                BufferedReader br = new BufferedReader( new FileReader(newFile));

                String filetext ;
                while ( (filetext = br.readLine() )!= null ){
//                    textArea.appendText(filetext+"\n");
                    Filepath += filetext+ "\n";
                }
                br.close();
            } catch (FileNotFoundException ignored) {
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        });

        open.setOnAction( e-> {

          File file = fileChooser.showOpenDialog(Stage);

            try {
                textArea.setText("");
                BufferedReader br = new BufferedReader( new FileReader(file));
               Stage.setTitle(file.getName());
               path = file.getPath();
                String filetext ;
                while ( (filetext = br.readLine() )!= null ){
                   textArea.appendText(filetext+"\n");
                }
                br.close();
            } catch (FileNotFoundException ignored) {
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
         New.setOnAction( e-> {
             textArea.setText("");
             Stage.setTitle("Text editor");
         });

        editMenu.getItems().add(copy);
        editMenu.getItems().add(paste);

        fileMenu.getItems().add(New);
        fileMenu.getItems().add(open);
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(Exit);

        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color:#cdb79e;" +
                "-fx-font-size:13px;");
        menuBar.getMenus().addAll(fileMenu,editMenu,Format,view ,helpMenu);

        layout2.setTop(menuBar);
        layout2.setCenter(textArea);

        // switching scene
        Scene scene2 = new Scene(layout2, 500, 500);

        btn.setOnAction(e -> {
            String value1 = String.valueOf(ta.getText());
            String value2 = String.valueOf(pas.getText());
            String name = "eyosiyas";
            String pass ="password";
            if(value1.equals(name) && value2.equals(pass) ){
                Stage.setScene(scene2);

            }
            else {
                value.setText("Invalid credentials!!");
                value.setFill(Color.RED);
            }
        });
        Image icon = new Image("logo.png");
        Stage.getIcons().add(icon);
        Stage.setTitle("Text editor");
        Stage.setScene(scene);
        Stage.show();


    }
    String path;


    public static void main(String[] args) {
        launch(args);
    }
}
