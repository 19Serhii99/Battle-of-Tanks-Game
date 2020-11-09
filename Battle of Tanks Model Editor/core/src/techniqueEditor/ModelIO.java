package techniqueEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.utils.Array;

import technique.TechniqueType;

public class ModelIO {
	
	public static void saveModel (String path, BlockBody blockBody, Array <BlockTower> blockTowers, Array <GunPoint> gunPoints, ShellPropertiesWindow shell,
			String shotSound, String hitSound) {
		try {
			File file = new File(path);
			file.createNewFile();			
			FileWriter fileWriter = new FileWriter(file);		
			StringBuilder text = new StringBuilder();
			String separator = System.getProperty("line.separator");		
			text.append("body" + separator);
			text.append("\tx " + blockBody.getX() + separator);
			text.append("\ty " + blockBody.getY() + separator);
			text.append("\twidth " + blockBody.getWidth() + separator);
			text.append("\theight " + blockBody.getHeight() + separator);
			text.append("\tposition " + blockBody.getPosition() + separator);
			text.append("\tmoney " + blockBody.getMoney() + separator);
			text.append("\tspeedRotation " + blockBody.getSpeedRotation() + separator);
			text.append("\tname " + blockBody.getName() + separator);
			text.append("\ttechniqueType " + blockBody.getTechniqueType().toString() + separator);
			text.append("\tmaxSpeed " + blockBody.getMaxSpeed() + separator);
			text.append("\tacceleration " + blockBody.getAcceleration() + separator);
			text.append("\tmaxHealth " + blockBody.getMaxHealth() + separator);
			text.append("towers_begin " + separator);
			for (BlockTower blockTower : blockTowers) {
				text.append("\tbegin" + separator);
				text.append("\t\tname " + blockTower.getName() + separator);
				text.append("\t\tx " + blockTower.getX() + separator);
				text.append("\t\ty " + blockTower.getY() + separator);
				text.append("\t\twidth " + blockTower.getWidth() + separator);
				text.append("\t\theight " + blockTower.getHeight() + separator);
				text.append("\t\tposition " + blockTower.getPosition() + separator);
				text.append("\t\tmoney " + blockTower.getMoney() + separator);
				text.append("\t\tspeedRotation " + blockTower.getSpeedRotation() + separator);
				text.append("\t\ttimeReduction " + blockTower.getTimeReduction() + separator);
				text.append("\t\tminRadius " + blockTower.getMinRadius() + separator);
				text.append("\t\tmaxRadius " + blockTower.getMaxRadius() + separator);
				text.append("\t\trotationLeft " + blockTower.getRotationLeft() + separator);
				text.append("\t\trotationRight " + blockTower.getRotationRight() + separator);
				text.append("\t\txOrigin " + blockTower.getXOrigin() + separator);
				text.append("\t\tyOrigin " + blockTower.getYOrigin() + separator);
				text.append("\t\ttimeRecharge " + blockTower.getTimeRecharge() + separator);
				text.append("\t\ttimeRechargeShellCassette " + blockTower.getTimeRechargeShellCassette() + separator);
				text.append("\t\texperience " + blockTower.getExperience() + separator);
				text.append("\t\tminDamage " + blockTower.getMinDamage() + separator);
				text.append("\t\tmaxDamage " + blockTower.getMaxDamage() + separator);
				text.append("\t\ttotalShells " + blockTower.getTotalShells() + separator);
				text.append("\t\ttotalShellsCassette " + blockTower.getTotalShellsCassette() + separator);
				text.append("\t\tshotSound " + shotSound + separator);
				text.append("\t\thitSound " + hitSound + separator);
				text.append("\tend" + separator);
				text.append("gunPoints_begin" + separator);
				for (GunPoint point : gunPoints) {
					text.append("\tbegin" + separator);
					text.append("\t\tx " + point.getBackground().getX() + separator);
					text.append("\t\ty " + (point.getBackground().getY() + point.getBackground().getHeight() / 2) + separator);
					text.append("\tend" + separator);
				}
				text.append("end" + separator);
			}
			text.append("end" + separator);
			text.append("shell_begin" + separator);
			text.append("\ttexture " + shell.getTexture() + separator);
			text.append("\tspeed " + shell.getSpeed() + separator);
			text.append("\twidth " + shell.getWidth() + separator);
			text.append("\theight " + shell.getHeight() + separator);
			text.append("shell_end" + separator);
			fileWriter.write(text.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error saving file: " + path);
		}
	}
	
	public static void loadModel (String path, BlockBody blockBody, Array <BlockTower> blockTowers, Array <GunPoint> gunPoints) {
		try {
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			int c;
			StringBuilder tempText = new StringBuilder();
			while ((c = fileReader.read()) != - 1) {
				tempText.append((char)c);
			}
			fileReader.close();		
			String [] text = tempText.toString().split("\\s+");
			for (int i = 0; i < text.length; i++) {
				if (text[i].equals("body")) {
					++i;
					blockBody.setX(Float.parseFloat(text[++i]));
					++i;
					blockBody.setY(Float.parseFloat(text[++i]));
					++i;
					blockBody.setWidth(Float.parseFloat(text[++i]));
					++i;
					blockBody.setHeight(Float.parseFloat(text[++i]));
					++i;
					blockBody.setPosition(Integer.parseInt(text[++i]));
					++i;
					blockBody.setMoney(Integer.parseInt(text[++i]));
					++i;
					blockBody.setSpeedRotation(Float.parseFloat(text[++i]));
					++i;
					blockBody.setName(text[++i]);
					i += 2;
					if (text[i].toLowerCase().equals("tank")) {
						blockBody.setTechniqueType(TechniqueType.TANK);
					} else if (text[i].toLowerCase().equals("arty")) {
						blockBody.setTechniqueType(TechniqueType.ARTY);
					} else if (text[i].toLowerCase().equals("reactive_system")) {
						blockBody.setTechniqueType(TechniqueType.REACTIVE_SYSTEM);
					} else if (text[i].toLowerCase().equals("flamethrower_system")) {
						blockBody.setTechniqueType(TechniqueType.FLAMETHROWER_SYSTEM);
					} else {
						System.out.println("Error loading technique type");
					}
					++i;
					blockBody.setMaxSpeed(Float.parseFloat(text[++i]));
					++i;
					blockBody.setAcceleration(Float.parseFloat(text[++i]));
					++i;
					blockBody.setMaxHealth(Float.parseFloat(text[++i]));
					++i;
				}
				if (text[++i].equals("towers_begin")) {
					
					for (++i; i < text.length; i++) {
						// ???? не доделано	
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error loading file: " + path);
		} catch (IOException e) {
			System.out.println("Error reading file: " + path);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Data of file damaged");
		}
	}
}