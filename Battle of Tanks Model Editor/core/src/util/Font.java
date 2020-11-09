package util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Disposable;

/** Pattern Singleton */
public class Font implements Disposable {
	private static Font font;
	private GlyphLayout glyph;
	private FreeTypeFontGenerator freeTypeFontGenerator;
	
	private Font () {
		glyph = new GlyphLayout();
		freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
	}
	
	public static Font getFont () {
		if (font == null) {
			font = new Font();
		}
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
	
	public BitmapFont generateBitmapFont (FreeTypeFontParameter freeTypeFontParameter) {
		return freeTypeFontGenerator.generateFont(freeTypeFontParameter);
	}

	@Override
	public void dispose () {
		if (font != null) {
			freeTypeFontGenerator.dispose();
		}
	}
}