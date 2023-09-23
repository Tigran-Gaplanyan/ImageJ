import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;
import ij.IJ;

public class CreateBinaryImagePlugin implements PlugInFilter {

    @Override
    public int setup(String arg, ImagePlus imp) {
        // This method is called when the plugin is set up.
        // You can specify the image types your plugin works with here.
        return DOES_8G; // This plugin works with 8-bit grayscale images.
    }

    @Override
    public void run(ImageProcessor ip) {
        // Create a new ByteProcessor of size 81x81 filled with white
        ByteProcessor byteProcessor = new ByteProcessor(81, 81);
        byteProcessor.setValue(255); // Set the value for white (255)
        byteProcessor.fill();

        // Read your individual *.stu file line-by-line
        String filePath = "/home/tigran/Downloads/ij153-linux64-java8/ImageJ/hec-s-92.stu"; // Replace with your file path
        String fileContent = IJ.openAsString(filePath);
        String[] lines = fileContent.split("\n");

        // Loop through each line
        for (String line : lines) {
            // Split the line into tokens based on whitespace
            String[] tokens = line.trim().split("\\s+");

            // Skip lines with a single token
            if (tokens.length == 1) {
                continue;
            }

            // Check if there are an even number of tokens
            if (tokens.length % 2 != 0) {
                // Handle error or skip this line
                continue;
            }

            // Process pairs of tokens as x and y coordinates
            for (int i = 0; i < tokens.length; i += 2) {
                try {
                    int x = Integer.parseInt(tokens[i]);
                    int y = Integer.parseInt(tokens[i + 1]);

                    // Assuming the coordinates are valid (within the 81x81 image size)
                    // Mark the pixel in black
                    byteProcessor.putPixel(x, y, 0); // 0 for black
                } catch (NumberFormatException e) {
                    // Handle parsing errors, if any
                    e.printStackTrace();
                }
            }
        }

        // Create a new ImagePlus based on the constructed ByteProcessor
        ImagePlus resultImage = new ImagePlus("Binary Image", byteProcessor);

        // Show the created image
        resultImage.show();

        // Save the created image in the *.png format (replace 'outputPath' with your desired path)
        String outputPath = "/home/tigran/Downloads/ij153-linux64-java8/ImageJ/images/hec-s-92.png"; // Specify the correct file path
        IJ.save(resultImage, outputPath);
    }
}

