package club.sk1er.patcher.hooks;

// Compile only
@SuppressWarnings("unused")
public class FontRendererHook
{
    private void create() {}

    private float getCharWidthFloat(char ch)
    {
        return 0;
    }

    public float renderChar(char ch, boolean italic)
    {
        return 0;
    }
}