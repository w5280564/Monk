package com.qingbo.monk.base.rich.bean;

public class SpanPart extends MyFontStyle {
        public int start;
    public int end;

    public SpanPart(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public SpanPart(MyFontStyle fontStyle) {
        this.isBold = fontStyle.isBold;
        this.isItalic = fontStyle.isItalic;
        this.isStreak = fontStyle.isStreak;
        this.isUnderline = fontStyle.isUnderline;
        this.fontSize = fontStyle.fontSize;
    }
}
