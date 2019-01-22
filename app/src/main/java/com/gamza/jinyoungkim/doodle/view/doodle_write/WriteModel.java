package com.gamza.jinyoungkim.doodle.view.doodle_write;

public class WriteModel {

    public String font;
    public int fontSize;
    public int lineSpacing;
    public String fontColor;

    public String text;

    public String filterName;
    public int filterAlpha;


    public WriteModel() {
    }

    // getter/setter
    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public int getFilterAlpha() {
        return filterAlpha;
    }

    public void setFilterAlpha(int filterAlpha) {
        this.filterAlpha = filterAlpha;
    }

}
