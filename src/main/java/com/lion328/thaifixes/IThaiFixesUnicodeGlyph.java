package com.lion328.thaifixes;

public interface IThaiFixesUnicodeGlyph
{
    float getRealAdvance();
    void setRemoveAdvance(boolean flag);
    void setFlag(boolean flag);
    boolean getFlag();
    void setCharacters(char ch);
    char getCharacters();
}