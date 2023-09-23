import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import ij.IJ;

public class ImageManipulationPlugin implements PlugInFilter {

    @Override
    public int setup(String arg, ImagePlus imp) {
        return DOES_ALL;
    }

    @Override
    public void run(ImageProcessor ip) {
        int width = ip.getWidth();
        int height = ip.getHeight();

        // Calculate dimensions for panels
        int leftWidth = width / 2;
        int rightWidth = width - leftWidth;
        int topHeight = height / 2;
        int bottomHeight = height - topHeight;
        int diagonalSplit = Math.min(width, height); // For diagonal split

        // Create ImageProcessors for panels
        ImageProcessor leftPanel = ip.createProcessor(leftWidth, height);
        ImageProcessor rightPanel = ip.createProcessor(rightWidth, height);
        ImageProcessor topPanel = ip.createProcessor(width, topHeight);
        ImageProcessor bottomPanel = ip.createProcessor(width, bottomHeight);
        ImageProcessor diagonalPanel1 = ip.createProcessor(diagonalSplit, diagonalSplit);
        ImageProcessor diagonalPanel2 = ip.createProcessor(width - diagonalSplit, height - diagonalSplit);

        // Copy pixel values to panels
        for (int x = 0; x < leftWidth; x++) {
            for (int y = 0; y < height; y++) {
                leftPanel.set(x, y, ip.get(x, y));
                rightPanel.set(x, y, ip.get(x + leftWidth, y));
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < topHeight; y++) {
                topPanel.set(x, y, ip.get(x, y));
                bottomPanel.set(x, y, ip.get(x, y + topHeight));
            }
        }
        
        // Copy pixels for diagonal split into diagonalPanel1
        for (int y = 0; y < diagonalSplit; y++) {
            for (int x = 0; x < diagonalSplit; x++) {
                diagonalPanel1.putPixel(x, y, ip.getPixel(x, y));
            }
        }

        // Copy pixels for diagonal split into diagonalPanel2
        for (int y = 0; y < height - diagonalSplit; y++) {
            for (int x = 0; x < width - diagonalSplit; x++) {
                diagonalPanel2.putPixel(x, y, ip.getPixel(x + diagonalSplit, y + diagonalSplit));
            }
        }

        // Swap left and right panels horizontally within the original image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < leftWidth; x++) {
                int temp = ip.getPixel(x, y);
                ip.putPixel(x, y, ip.getPixel(x + leftWidth, y));
                ip.putPixel(x + leftWidth, y, temp);
            }
        }
        
        // Swap top and bottom panels vertically within the original image
	for (int y = 0; y < topHeight; y++) {
            for (int x = 0; x < width; x++) {
        	int temp = ip.getPixel(x, y);
        	ip.putPixel(x, y, ip.getPixel(x, y + bottomHeight));
        	ip.putPixel(x, y + bottomHeight, temp);
    	    }
	}

        // Show the modified image
        ImagePlus modifiedImage = new ImagePlus("Modified Image", ip.duplicate());
        modifiedImage.show();

        // Save the modified image in the *.png format
        String outputPath = "/home/tigran/Downloads/ij153-linux64-java8/ImageJ/images/copy.png"; // Replace with your desired file path
        IJ.saveAs(modifiedImage, "PNG", outputPath);
    }
}
