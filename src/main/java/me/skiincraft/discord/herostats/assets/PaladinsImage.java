package me.skiincraft.discord.herostats.assets;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.discord.core.utils.StringUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PaladinsImage {

    private static Path getAvatarPath() {
        String assetspath = HeroStatsBot.getMain().getPlugin().getAssetsPath().getAbsolutePath();
        return Paths.get(assetspath + "/avatar/");
    }

    private static Path getBackgroundsPath() {
        String assetspath = HeroStatsBot.getMain().getPlugin().getAssetsPath().getAbsolutePath();
        return Paths.get(assetspath + "/backgrounds/");
    }

    private static Path getTierPath() {
        String assetspath = HeroStatsBot.getMain().getPlugin().getAssetsPath().getAbsolutePath();
        return Paths.get(assetspath + "/elos/");
    }

    private static Path getMapsPath() {
        String assetspath = HeroStatsBot.getMain().getPlugin().getAssetsPath().getAbsolutePath();
        return Paths.get(assetspath + "/maps/");
    }

    public static InputStream getMap(String map){
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getMapsPath())){
                if (path.toFile().getName().endsWith(".jpg")){
                    imageFile.add(path.toFile());
                }
            }

            File image = imageFile.stream().filter(file -> StringUtils.containsEqualsIgnoreCase(file.getName(), map))
                    .findAny()
                    .orElse(null);

            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getTier(Tier tier){
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getTierPath())){
                if (path.toFile().getName().endsWith(".png")){
                    imageFile.add(path.toFile());
                }
            }

            File image = imageFile.stream().filter(file -> StringUtils.containsEqualsIgnoreCase(file.getName(), tier.name()))
                    .findAny()
                    .orElse(null);

            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getBackground(Champion champion){
        try {
            List<File> imageFile = new ArrayList<>();
            File defaultbg = null;
            for (Path path : Files.newDirectoryStream(getBackgroundsPath())){
                if (path.toFile().getName().toLowerCase().endsWith("background.png")){
                    if (path.toFile().getName().toLowerCase().contains("default")){
                        defaultbg = path.toFile();
                        continue;
                    }
                    imageFile.add(path.toFile());
                }
            }

            File image = imageFile.stream().filter(file -> StringUtils.containsEqualsIgnoreCase(file.getName(), champion.getName()))
                    .findAny()
                    .orElse(defaultbg);

            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getAvatar(Champion champion){
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getAvatarPath())){
                if (path.toFile().getName().endsWith(".jpg")){
                    imageFile.add(path.toFile());
                }
            }
            File image = imageFile.stream().filter(file -> StringUtils.containsEqualsIgnoreCase(file.getName(), champion.getName()))
                    .findAny()
                    .orElseGet(() -> {
                        File file = new File(getAvatarPath().toUri() + champion.getName() + ".jpg");
                        writeInJpeg(champion.getIcon(), file);

                        return file;
                    });

            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeInJpeg(String urlImage, File output){
        try {
        int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
        ColorModel RGB_OPAQUE = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
        URL url = new URL(urlImage);

        Image img = Toolkit.getDefaultToolkit().createImage(url);

        PixelGrabber pg = new PixelGrabber(img, 0, 0, -1, -1, true);
        pg.grabPixels();
        int width = pg.getWidth(), height = pg.getHeight();

        DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
        WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
        BufferedImage bi = new BufferedImage(RGB_OPAQUE, raster, false, null);

        ImageIO.write(bi, "jpg", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
