package lights.smile.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class Zki {

	public static final String HTTP = "http://localhost:8080/SmileWebApp/zki/";
	public static final String DIRECTORIO = "DIRECTORIO/DIRECTORIO_";
	public static final String TRABAJO_SOCIAL = "TRABAJO_SOCIAL/TRABAJO_SOCIAL_";
	public static final String EVENTO = "EVENTO/EVENTO_";
	public static final String ORGANIZACION = "ORGANIZACION/";
	public static final String NOTICIAS = "NOTICIAS/NOTICIAS_";
	public static final String PERSONAS = "PERSONA/PERSONAS_";
	public static final String VALORES = "VALORES/VALORES_";
	public static final String PARTICIPACION = "PARTICIPACION/PARTICIPACION_";

	public static final String DEFAULT = "default_image.jpg";

	public static String getPath() {
		String dir = Zki.class.getResource("Zki.txt").getFile();

		return dir.substring(3, dir.indexOf("WEB-INF")) + "zki/";
	}

	public static BufferedImage getBufferedImage(String clase, Integer id) {
		try {
			return ImageIO.read(new File(getPath() + clase + id));
		} catch (Exception e) {
			try {
				return ImageIO.read(new File(getPath() + DEFAULT));
			} catch (IOException e1) {
				return null;
			}
		}
	}

	public static BufferedImage getBufferedImage(String urlRelative) {
		try {
			return ImageIO.read(new File(getPath() + urlRelative));
		} catch (Exception e) {
			try {
				return ImageIO.read(new File(getPath() + DEFAULT));
			} catch (IOException e1) {
				return null;
			}
		}
	}

	public static Boolean save(String clase, Integer id, String fileExtension,
			byte[] bytes) {
		if (bytes != null) {
			try {
				FileUtils.writeByteArrayToFile(new File(getPath() + clase + id
						+ "." + fileExtension), bytes);

				return true;
			} catch (IOException e) {
				return false;
			}
		}

		return false;
	}

	public static Boolean remove(String urlRelative) {
		return new File(getPath() + urlRelative).delete();
	}

	public static byte[] getBytes(String urlRelative) {
		try {
			return Files.readAllBytes(Paths.get(getPath() + urlRelative));
		} catch (Exception e) {
			try {
				return Files.readAllBytes(Paths.get(getPath() + DEFAULT));
			} catch (IOException e1) {
				return null;
			}
		}
	}

	public static byte[] getBytes(String clase, Integer id) {
		try {
			return Files.readAllBytes(Paths.get(getPath() + clase + id));
		} catch (Exception e) {
			try {
				return Files.readAllBytes(Paths.get(getPath() + DEFAULT));
			} catch (IOException e1) {
				return null;
			}
		}
	}

	public static BufferedImage getImageContent(byte[] bytes) {
		if (bytes != null) {
			try {
				return ImageIO.read(new ByteArrayInputStream(bytes));
			} catch (IOException e) {
				return null;
			}
		}

		return null;
	}

	public static String getUrlWeb(String urlRelative) {
		return HTTP + urlRelative;
	}
}
