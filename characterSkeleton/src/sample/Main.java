package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("characterUI.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 858, 740);
        CharacterUI characterUI = loader.getController();


/*        CharacterGlyph characterGlyph=new CharacterGlyph("123");
        ArrayList<CharacterStroke> list = new ArrayList();
        ArrayList<Point2D> list2 = new ArrayList();
        CharacterStroke characterStroke=new CharacterStroke("234");
        list.add(characterStroke);
        list2.add(new Point2D(22,44));
        list2.add(new Point2D(44,88));
        characterGlyph.setCharacterStrokeArrayList(list);
        characterStroke.setPoint2DArrayList(list2);

        characterUI.drawCharacter(characterGlyph);*/

        this.primaryStage=primaryStage;
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
