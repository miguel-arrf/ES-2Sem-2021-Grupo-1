package g1.ISCTE;

public enum FontType{
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