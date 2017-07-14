package raytracer;

import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.awt.image.Raster;
import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import java.awt.Point;
import java.io.File;

public class BufferedImageToByteArrayConverter {

	public static byte[] imageToByteArray(BufferedImage image) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "bmp", baos);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}

	public static BufferedImage byteArrayToImage(int width, int height, byte[] srcbuf) throws Exception {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		result.setData(Raster.createRaster(result.getSampleModel(), new DataBufferByte(srcbuf, srcbuf.length), new Point()));
		return result;
	}

	public static void main(String[] args) {
		BufferedImage img = null;
		try {
    			img = ImageIO.read(new File("/home/cristian/Workspace/raytracer-master/test05.bmp"));
			byte[] bytes = BufferedImageToByteArrayConverter.imageToByteArray(img);
			ImageIO.write(img, "bmp", new File("/home/cristian/Workspace/raytracer-master/test05-copy.bmp"));
		} catch (Exception e) {
			e.printStackTrace();		
		}	
	}
}
