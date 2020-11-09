package util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;

public class TextureCreator {
	public static Texture createTexture (Color color) {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGB888);
		pixmap.setColor(color);
		pixmap.fill();
		Texture texture = new Texture(pixmap);
		//pixmap.dispose();
		return texture;
	}
}