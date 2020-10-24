package me.skiincraft.discord.herostats.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonConfiguration {
	
	private final File file;
	private final JsonObject object;
	
	public JsonConfiguration(File file) throws IOException {
		if (!file.getName().toLowerCase().endsWith(".json")) {
			throw new IOException("The requested file extension is not .json");
		}
		this.file = file;
		object = new JsonParser().parse(new FileReader(file)).getAsJsonArray().get(0).getAsJsonObject();
	}
	
	public String get(String key) {
		if (isJsonObject(key)) {
			if (key.endsWith(".")) {
				return null;
			}
			String[] obj = key.replace(".", "#").split("#");
			JsonObject item = object;
			for (int i = 1; i <= obj.length; i++) {
				if (!item.has(obj[i-1])) {
					return null;
				}
				if (i == obj.length) {
					break;
				}
				if (item.isJsonNull()) {
					return null;
				}
				item = item.get(obj[i-1]).getAsJsonObject();
			}
			return item.get(obj[obj.length-1]).getAsString();
		}
		if (object.has(key)) {
			if (object.get(key).isJsonPrimitive()) {
				return object.get(key).getAsString();
			}
			//return object.get(key).toString();
		}
		return null;
	}
	
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(get(key));
	}
	
	public float getFloat(String key) {
		return Float.parseFloat(get(key));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(get(key));
	}
	
	public int getInteger(String key) {
		return Integer.parseInt(get(key));
	}
	
	public long getLong(String key) {
		return Long.parseLong(get(key));
	}
	
	private void setObject(JsonObject item, String prop, Object ob) {
		if (ob instanceof Number) {
			item.addProperty(prop, (Number)ob);
		}
		if (ob instanceof Boolean) {
			item.addProperty(prop, (Boolean)ob);
		}
		if (ob instanceof String) {
			item.addProperty(prop, (String)ob);
		}
	}

	private void setObject(String key, Object objeto) {
		if (isJsonObject(key)) {
			if (key.endsWith(".")) {
				return;
			}
			String[] obj = key.replace(".", "#").split("#");
			JsonObject item = object;
			for (int i = 1; i <= obj.length; i++) {
				if (item.has(obj[i - 1])) {
					if (i == obj.length) {
						if (objeto == null) {
							item.add(obj[i - 1], JsonNull.INSTANCE);
							return;
						}
						setObject(item, obj[i - 1], objeto);
						return;
					}
					item = item.get(obj[i - 1]).getAsJsonObject();
				} else {
					while (i <= obj.length) {
						if (i == obj.length) {
							if (objeto == null) {
								item.add(obj[i - 1], JsonNull.INSTANCE);
								return;
							}
							setObject(item, obj[i - 1], objeto);
							return;
						}
						JsonObject o = new JsonObject();
						item.add(obj[i - 1], o);
						item = o;
						i++;
					}
				}
			}
		}
		if (objeto == null) {
			object.add(key, JsonNull.INSTANCE);
			return;
		}
		setObject(object, key, objeto);
	}
	
	public void set(String key, String value) {
		this.setObject(key, value);
	}
	
	public void set(String key, Integer value) {
		this.setObject(key, value);
	}
	
	public void set(String key, Float value) {
		this.setObject(key, value);
	}
	
	public void set(String key, Double value) {
		this.setObject(key, value);
	}
	
	public void set(String key, Long value) {
		this.setObject(key, value);
	}
	
	public JsonObject getObject() {
		return object;
	}
	
	public void save(File file) {
		try {
			FileWriter writer = new FileWriter(file);
			JsonArray array = new JsonArray();
			array.add(object);
			writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(array));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		save(getFile());
	}
	
	public File getFile() {
		if (file == null) {
			return new File("newJsonFile");
		}
		return file;
	}
	
	private boolean isJsonObject(String ob) {
		return ob.contains(".");
	}

}