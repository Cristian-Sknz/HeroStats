package me.skiincraft.discord.herostats.utils;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageBuilder {

	public enum Alignment {
		Center, Right, Left, Top, Botton,
		Top_left, Bottom_left,
		Top_Right, Bottom_Right
	}
	
	private BufferedImage base;
	private String imagename;

	private Dimension size;
	private Graphics2D graphic;

	public ImageBuilder(String imagename, int w, int h) {
		this.base = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		this.size = new Dimension(w, h);

		this.imagename = imagename;
		this.graphic = base.createGraphics();
	}

	public void drawImage(BufferedImage image, int x, int y, Dimension size, Alignment align) {
		int posX = x;
		int posY = y;
		
		if (align == Alignment.Center) {
			posX =  x- ((int)size.getWidth() /2);
			posY =  y- ((int)size.getHeight() / 2);
		}
		
		if (align == Alignment.Left) {
			posY =  y- ((int)size.getHeight() / 2);
		}
		
		if (align == Alignment.Right) {
			posX =  x- ((int)size.getWidth());
			posY =  y- ((int)size.getHeight() / 2);
		}
		
		if (align == Alignment.Botton) {
			posX =  x- ((int)size.getWidth() /2);
		}
		if (align == Alignment.Top) {
			posY =  y- ((int)size.getHeight());
			posX =  x- ((int)size.getWidth() /2);
		}
		
		if (align == Alignment.Bottom_left) {
			posY = y;
		}
		
		if (align == Alignment.Top_left) {
			posY =  y- ((int)size.getHeight());
		}
		
		if (align == Alignment.Bottom_Right) {
			posX =  x- ((int)size.getWidth());
		}
		if (align == Alignment.Top_Right) {
			posX =  x- ((int)size.getWidth());
			posY =  y- ((int)size.getHeight());
		}
		
		graphic.drawImage(image, posX, posY, size.width, size.height, null);
	}
	
	public void drawImage(URL image, int x, int y, Dimension size, Alignment align) throws IOException {
		this.drawImage(ImageIO.read(image), x, y, size, align);
	}
	
	public void drawImage(File image, int x, int y, Dimension size, Alignment align) throws IOException {
		this.drawImage(ImageIO.read(image), x, y, size, align);
	}
	
	public void drawImage(BufferedImage image, int x, int y, Dimension size) {
		this.drawImage(image, x, y, size, Alignment.Center);
	}
	
	public void drawImage(InputStream image, int x, int y, Dimension size) throws IOException {
		this.drawImage(ImageIO.read(image), x, y, size, Alignment.Center);
	}
	
	public void drawImage(File image, int x, int y, Dimension size) throws IOException {
		this.drawImage(ImageIO.read(image), x, y, size, Alignment.Center);
	}

	public void drawImage(URL url, int x, int y, Dimension size) throws IOException {
		this.drawImage(ImageIO.read(url), x, y, size, Alignment.Center);
	}

	public void addString(String string, int x, int y, int fontSize) {
		graphic.setFont(new Font("Arial", Font.PLAIN, fontSize));
		graphic.drawString(string, x, y);
	}
	public void addStringBox(String string, int x, int y, int w, Font font) {
		graphic.setFont(font);
		
		List<String> strings = new ArrayList<>();
		int index = 0;
		while (index < string.length()) {
		    strings.add(string.substring(index, Math.min(index + w, string.length())));
		    index += w;
		}
		
		for (String string2 : strings) {
			addCentralizedString(string2, x, y, font);
			y += graphic.getFontMetrics().getAscent() + graphic.getFontMetrics().getDescent();
		}
	}

	public void addString(String string, int x, int y, Font font) {
		graphic.setFont(font);
		graphic.drawString(string, x, y);
	}

	public void addCentralizedString(String string, int x, int y, int fontSize) {
		graphic.setFont(new Font("Arial", Font.PLAIN, fontSize));
		FontMetrics fm = graphic.getFontMetrics();

		x = x * 2;
		y = y * 2;

		int w = (x - fm.stringWidth(string)) / 2;
		int h = (fm.getAscent() + (y - (fm.getAscent() + fm.getDescent())) / 2);

		graphic.drawString(string, w, h);
	}

	public void addCentralizedString(String string, int x, int y, Font font) {
		graphic.setFont(font);
		FontMetrics fm = graphic.getFontMetrics();

		x = x * 2;
		y = y * 2;

		int w = (x - fm.stringWidth(string)) / 2;
		int h = (fm.getAscent() + (y - (fm.getAscent() + fm.getDescent())) / 2);

		graphic.drawString(string, w, h);
	}

	public void addCentralizedStringY(String string, int x, int y, Font font) {
		graphic.setFont(font);
		FontMetrics fm = graphic.getFontMetrics();

		y = y * 2;

		int h = (fm.getAscent() + (y - (fm.getAscent() + fm.getDescent())) / 2);

		graphic.drawString(string, x, h);
	}

	public void addRightStringY(String string, int x, int y, Font font) {
		graphic.setFont(font);
		FontMetrics fm = graphic.getFontMetrics();

		y = y * 2;

		int w = (x - fm.stringWidth(string));
		int h = (fm.getAscent() + (y - (fm.getAscent() + fm.getDescent())) / 2);

		graphic.drawString(string, w, h);
	}
	
	public void addCentralizedStringX(String string, int x, int y, Font font) {
		graphic.setFont(font);
		FontMetrics fm = graphic.getFontMetrics();

		x = x * 2;

		int w = (x - fm.stringWidth(string)) / 2;

		graphic.drawString(string, w, y);
	}

	public Image build() {
		graphic.dispose();

		return base;
	}
	
	public BufferedImage buildImage() {
		graphic.dispose();

		return base;
	}

	public File buildFile() throws IOException {
		graphic.dispose();
		File file = new File(this.imagename + ".png");
		file.mkdirs();
		ImageIO.write(base, "png", file);

		return file;
	}

	public InputStream buildInput() throws IOException {
		graphic.dispose();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(base, "png", os);
		return new ByteArrayInputStream(os.toByteArray());
	}

	public String getImageame() {
		return imagename;
	}

	public Dimension getSize() {
		return size;
	}

	public Graphics2D getGraphic() {
		return graphic;
	}
	
}
