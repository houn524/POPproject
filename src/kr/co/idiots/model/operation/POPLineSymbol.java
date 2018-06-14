package kr.co.idiots.model.operation;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import kr.co.idiots.model.POPNodeType;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class POPLineSymbol extends POPOperationSymbol {

    private ImageView line;

    public POPLineSymbol() {
        super();

        InputStream stream = getClass().getResourceAsStream("/images/StringOperation.png");
        Image img = new Image(stream);
        imgShape.setImage(img);

        this.type = POPNodeType.Line;

        stream = getClass().getResourceAsStream("/images/Line.png");
        img = new Image(stream);
        line = new ImageView(img);
        contents.getChildren().add(line);

        setInitWidth();
    }

    @Override
    public void playSymbol() throws NullPointerException {

    }

    @Override
    public Object executeSymbol() { return null; }
}
