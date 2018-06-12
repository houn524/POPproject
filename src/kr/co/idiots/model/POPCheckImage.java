package kr.co.idiots.model;

import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class POPCheckImage {
	private ImageView imgView;
	
	public POPCheckImage(ImageView imgView) {
        this.imgView = imgView;
    }
}
