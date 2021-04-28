package g1.ISCTE;

public enum FontType{
    ROUNDED_BOLD("SF-Pro-Rounded-SemiBold.ttf"),
    DISPLAY_MEDIUM("SFProDisplay-Medium.ttf"),
    REGULAR("SFProDisplay-Regular.ttf"),
    MEDIUM("SFProDisplay-Medium.ttf"),
    BOLD("SFProText-Bold.ttf");

    public final String font;

    /**
     * @param string selected font type
     */
    private FontType(String string){
        this.font = string;
    }
}