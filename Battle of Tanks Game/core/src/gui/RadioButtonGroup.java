package gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class RadioButtonGroup {
	private Array <CheckBox> items;
	private CheckBox checkBox;
	private int id;
	private float alpha = 1.0f;
	
	public RadioButtonGroup () {
		items = new Array <CheckBox>(1);
	}
	
	public void addItem (CheckBox item) {
		items.add(item);
	}
	
	public void setId (int id) {
		this.id = id;
		items.get(id).setChecked(true);
		checkBox = items.get(id);
	}
	
	public void setAlpha (float alpha) {
		this.alpha = alpha;
	}
	
	public void show (SpriteBatch batch) {
		checkBox = null;
		for (int i = 0; i < items.size; i++) {
			items.get(i).setAlpha(alpha);
			items.get(i).show(batch);
			if (items.get(i).isChecked()) {
				if (i != id) {
					items.get(id).setChecked(false);
				}
				checkBox = items.get(i);
				id = i;
			}
		}
		if (checkBox == null) {
			checkBox = items.get(id);
			checkBox.setChecked(true);
		}
	}
	
	public int getId () {
		return id;
	}
	
	public CheckBox getCheckBox () {
		return checkBox;
	}
}