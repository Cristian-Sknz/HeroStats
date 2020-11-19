package me.skiincraft.discord.herostats.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static InputStream toInputStream(File imagefile) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ImageIO.read(imagefile), "png", os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	public static InputStream toInputStream(BufferedImage image) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(image, "png", os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	public static InputStream toInputStream(URL imageurl) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(ImageIO.read(imageurl), "png", os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	public static InputStream toInputStream(String url) throws IOException {
		return toInputStream(new URL(url));
	}

	public static Color getPredominatColor(BufferedImage image) {
		try {
			BufferedImage img = new BufferedImage(40, 40, 2);
			Graphics2D graph = img.createGraphics();
			graph.drawImage(image, 0, 0, 40, 40, null);
			graph.dispose();

			ColorThief.CMap result;

			result = ColorThief.computeMap(img, 3);

			Iterator<ColorThief.DenormalizedVBox> boxes = result.getBoxes().iterator();

			int[] cor = boxes.next().getColor();
			return new Color(cor[0], cor[1], cor[2]);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean existsImage(URL url) {
		try {
			BufferedImage b = ImageIO.read(url);
			b.createGraphics();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static boolean existsImage(String url) {
		try {
			BufferedImage b = ImageIO.read(new URL(url));
			b.createGraphics();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
