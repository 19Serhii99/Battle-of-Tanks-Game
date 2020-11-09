package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

public class Font implements Disposable {
	private static Font font;
	private GlyphLayout glyph;
	private FreeTypeFontGenerator freeTypeFontGenerator;
	private String chars;
	
	private Font () {
		glyph = new GlyphLayout();
		freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
		initLanguages();
	}
	
	private void initLanguages () {
		String ru = new String("éöóêåíãøùçõúôûâàïğîëäæıÿ÷ñìèòüáşÉÖÓÊÅÍÃØÙÇÕÚÔÛÂÀÏĞÎËÄÆİß×ÑÌÈÒÜÁŞ¸¨");
		String ua = new String("³²ºª¿¯");
		String en = new String("qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM");
		String symbols = new String("`~!@#$%^&*()_-+=[]{};,.<>\"\\||'¹:?/1234567890");
		chars = new String(ru + ua + en + symbols);
	}
	
	public static Font getInstance () {
		if (font == null) font = new Font();
		return font;
	}
	
	public float getWidth (BitmapFont font, String text) {
		glyph.setText(font, text);
		return glyph.width;
	}
	
	public float getHeight (BitmapFont font, String text) {
		glyph.setText(font, text);
		return glyph.height;
	}
	
	public BitmapFont generateBitmapFont (Color color, int size) {
		FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontParameter();
		freeTypeFontParameter.size = size;
		freeTypeFontParameter.characters = chars;
		freeTypeFontParameter.color = color;
		return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
	}

	@Override
	public void dispose () {
		if (font != null) {
			freeTypeFontGenerator.dispose();
		}
	}
}