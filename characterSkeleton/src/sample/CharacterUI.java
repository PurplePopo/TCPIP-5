package sample;

import cc.purplepopo.ReadCharacter;
import cc.purplepopo.character.CharacterGlyph;
import cc.purplepopo.character.CharacterStroke;
import cc.purplepopo.character.CharacterTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.Main.primaryStage;

public class CharacterUI implements Initializable {
    @FXML
    private Pane drawPane;
    @FXML
    private ListView characterList;
    @FXML
    private TreeView characterView;
    @FXML
    private Label totalLable;
    @FXML
    private ToolBar toolbar;

    CharacterTree characterTree = new CharacterTree();
    CharacterGlyph characterGlyph = new CharacterGlyph(0,"");
    private Group characterGroup = new Group();
    private int currentNum = 0;
    private double perX = 0;
    private double perY = 0;

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
                                //显示总笔元数
                                totalLable.setText("Total:"+cg.getCharacterName());
                                //链接字符
                                characterGlyph=cg;
                                currentNum=characterGlyph.getCharacterStrokeArrayList().size();
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
        characterGroup.getChildren().clear();
        for (int i = 0; i < currentNum; i++) {
            drawCharacterStroke(characterGlyph.getCharacterStrokeArrayList().get(i));
        }
        drawPane.getChildren().add(characterGroup);
    }

    private void drawCharacterStroke(CharacterStroke characterStroke){
        ArrayList<Point2D> list = characterStroke.getPoint2DArrayList();
        Path path = new Path();
        path.getElements().add(new MoveTo(list.get(0).getX()+perX,list.get(0).getY()+perY));
        for (Point2D point:list) {
            path.getElements().add(new LineTo(point.getX()+perX,point.getY()+perY));
        }
        characterGroup.getChildren().add(path);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        perX = drawPane.getLayoutX()/2+drawPane.getWidth();
        perY = drawPane.getLayoutY()/2+drawPane.getHeight();
        drawPane.addEventHandler(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                characterGroup.setScaleX(characterGroup.getScaleX()+event.getDeltaY()/50);
                characterGroup.setScaleY(characterGroup.getScaleY()+event.getDeltaY()/50);
            }
        });
    }

    public void tool_Clean(){
        currentNum = 0;
        drawCharacter(characterGlyph);
    }

    public void tool_Last(){
        if (currentNum>0)
        currentNum--;
        drawCharacter(characterGlyph);
    }

    public void tool_Next(){
        if (currentNum<characterGlyph.getCharacterStrokeArrayList().size())
        currentNum++;
        drawCharacter(characterGlyph);
    }

    public void tool_Stream(){

    }

    public void tool_Complete(){
        currentNum=characterGlyph.getCharacterStrokeArrayList().size();
        drawCharacter(characterGlyph);
    }
}
