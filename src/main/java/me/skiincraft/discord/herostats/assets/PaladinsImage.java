package me.skiincraft.discord.herostats.assets;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.enums.Tier;
import me.skiincraft.discord.core.utils.StringUtils;
import me.skiincraft.discord.herostats.HeroStatsBot;

import javax.annotation.Nullable;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
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

    private static Path getAssetsPath() {
        String assetspath = HeroStatsBot.getMain().getPlugin().getAssetsPath().getAbsolutePath();
        return Paths.get(assetspath + "/");
    }

    @Nullable
    public static InputStream getAssetsImage(String imagename) {
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getAssetsPath())) {
                if (path.toFile().getName().endsWith(".jpg")) {
                    imageFile.add(path.toFile());
                }
                if (path.toFile().getName().endsWith(".png")) {
                    imageFile.add(path.toFile());
                }
            }

            File image = imageFile.stream().filter(file -> file.getName().substring(0, file.getName().lastIndexOf(".")).equalsIgnoreCase(imagename))
                    .findAny()
                    .orElse(null);

            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getMap(String map) {
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getMapsPath())) {
                if (path.toFile().getName().endsWith(".jpg")) {
                    imageFile.add(path.toFile());
                }
            }

            File image = imageFile.stream().filter(file -> StringUtils.containsEqualsIgnoreCase(file.getName(), map))
                    .findAny()
                    .orElse(null);

            if (image == null) return getMap("default_map");
            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getTier(Tier tier) {
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getTierPath())) {
                if (path.toFile().getName().endsWith(".png")) {
                    imageFile.add(path.toFile());
                }
            }

            File image = imageFile.stream().filter(file -> file.getName().substring(0, file.getName().lastIndexOf(".")).equalsIgnoreCase(tier.name()))
                    .findAny()
                    .orElse(null);

            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getBackground(String championName) {
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getBackgroundsPath())) {
                if (path.toFile().getName().toLowerCase().endsWith("background.png")) {
                    imageFile.add(path.toFile());
                }
            }

            File image = imageFile.stream().filter(file -> StringUtils.containsEqualsIgnoreCase(file.getName(), championName))
                    .findAny()
                    .orElse(null);

            if (image == null) return getBackground("default_background");
            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getBackground(Champion champion) {
        return getBackground(champion.getName());
    }

    public static InputStream getAvatar(Champion champion) {
        try {
            List<File> imageFile = new ArrayList<>();
            for (Path path : Files.newDirectoryStream(getAvatarPath())) {
                if (path.toFile().getName().endsWith(".jpg")) {
                    imageFile.add(path.toFile());
                }
            }
            File image = imageFile.stream().filter(file -> StringUtils.containsEqualsIgnoreCase(file.getName(), champion.getName()))
                    .findAny()
                    .orElseGet(() -> {
                        try {
                            File file = new File(HeroStatsBot.getMain().getPlugin().getAssetsPath()+ "/avatar/" + champion.getName() + ".jpg");
                            file.createNewFile();
                            writeInJpeg(ImageIO.read(new URL(champion.getIcon())), file);
                            return file;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    });

            return new FileInputStream(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeInJpeg(BufferedImage bufferedImage, File output) {
        try {
            JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
            jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(1f);

            final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(new FileImageOutputStream(output));

            writer.write(null, new IIOImage(bufferedImage, null, null), jpegParams);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
