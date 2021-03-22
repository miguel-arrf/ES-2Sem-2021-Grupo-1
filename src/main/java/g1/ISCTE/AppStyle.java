package g1.ISCTE;

import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class AppStyle {

    public static Font getFont(FontType fontType, int size){
        return Font.loadFont(AppStyle.class.getResource("/fonts/" + fontType.font).toExternalForm(), size);
    }

    public static Label getLabelWithColorAndFont(Paint paint, FontType fontType, int size, String string){
        Label label = new Label(string);
        label.setTextFill(paint);
        label.setFont(getFont(fontType, size));

        return label;
    }

}

enum FontType{
    ROUNDED_BOLD("SF-Pro-Rounded-Bold.ttf"),
    DISPLAY_MEDIUM("SFProDisplay-Medium.ttf"),
    LIGHT(""),
    REGULAR(""),
    MEDIUM(""),
    BOLD(""),
    BLACK("");

    public final String font;

    private FontType(String string){
        this.font = string;
    }
}