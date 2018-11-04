package sample;

import cc.purplepopo.ReadCharacter;
import cc.purplepopo.character.CharacterGlyph;
import cc.purplepopo.character.CharacterStroke;
import cc.purplepopo.character.CharacterTree;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static sample.Main.primaryStage;

public class CharacterUI{
    @FXML
    private Pane drawPane;
    @FXML
    private ListView characterList;
    @FXML
    private TreeView characterView;

    CharacterTree characterTree = new CharacterTree();

    public void MenuOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开文件");
        try {
            File file = fileChooser.showOpenDialog(primaryStage);
            ReadCharacter.Read(file,"newname", characterTree);
            characterView.setRoot(new TreeItem(file.getName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //把字符加载进itemView
        for (CharacterGlyph cg:characterTree.getCharacterGlyphArrayList()) {
            TreeItem treeItem = new TreeItem(String.valueOf(cg.getCharacterID()));
            characterView.getRoot().getChildren().add(treeItem);
        }
        //事件
        characterView.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                Node node = event.getPickResult().getIntersectedNode();

                if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                    String name = (String) ((TreeItem)characterView.getSelectionModel().getSelectedItem()).getValue();
                    //System.out.println("Node click: " + name);
                    //把笔画加入itemList
                    ObservableList<String> strList = FXCollections.observableArrayList();
                    strList.clear();
                    for (CharacterGlyph cg:characterTree.getCharacterGlyphArrayList()) {
                        //找到对应字符
                        if (name.equals(String.valueOf(cg.getCharacterID()))) {
                            //历遍笔画
                            for (CharacterStroke cs:cg.getCharacterStrokeArrayList()) {
                                //把笔画加入ListView中
                                strList.add(cs.getPoint2DArrayList().get(0).toString());
                                //绘出字符
                                drawCharacter(cg);
                            }
                        }
                    }
                    characterList.setItems(strList);
                    return;
                }
            }
        });
    }


    public void drawCharacter(CharacterGlyph characterGlyph){
        drawPane.getChildren().clear();
        for (CharacterStroke characterStroke:characterGlyph.getCharacterStrokeArrayList()) {
            drawCharacterStroke(characterStroke);
        }
    }

    private void drawCharacterStroke(CharacterStroke characterStroke){
        ArrayList<Point2D> list = characterStroke.getPoint2DArrayList();
        Path path = new Path();
        path.getElements().add(new MoveTo(list.get(0).getX()+drawPane.getLayoutX()+drawPane.getLayoutBounds().getMinX()+drawPane.getWidth()/2,
                list.get(0).getY()+drawPane.getLayoutY()+drawPane.getLayoutBounds().getMinY()+drawPane.getHeight()/2));
        for (Point2D point:list) {
            path.getElements().add(new LineTo(point.getX()+drawPane.getLayoutX()+drawPane.getLayoutBounds().getMinX()+drawPane.getWidth()/2,
                    point.getY()+drawPane.getLayoutY()+drawPane.getLayoutBounds().getMinY()+drawPane.getHeight()/2));
        }
        drawPane.getChildren().addAll(path);
    }

}
