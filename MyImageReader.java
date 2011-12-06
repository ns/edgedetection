
/*
 * --------------------------------------------------------------------------
 * CS111 - Digital Image Processing - Spring '10
 * Instructor - Prof Aditi Majumder
 * TA - Uddipan Mukherjee - umukherj@uci.edu
 * --------------------------------------------------------------------------
 This is a class for reading a sample image file
 The image should be in .jpg format
 The filename of the image is passed as a string
 
 */

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;

public class MyImageReader
{
    public static BufferedImage readImageIntoBufferedImage(String fileName)
    {
        BufferedImage image = null;

        //Check that the file name indicates we have a jpg file
        if ( !fileName.endsWith(".jpg") )
        {
            System.out.println("This is not a jpg file.");
            return null;
        }

        try {
            // Read from a file
            File file = new File(fileName);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println("Could not open file.");
            return null;
        }
        return image;
    }

    /* This method gives the number of bands (number of color channels) in for an input image */

    public static int numchannels(String fileName)
    {
		BufferedImage image = null;

		image = readImageIntoBufferedImage(fileName);
		return image.getRaster().getNumBands();
	}

    /* This method opens the jpg mage file given by the input string, and decodes the jpg,
     * putting the relevant values into a two dimensional array of ints. For the time being,
     * we are only dealing with grayscale. However, in the future, we will need to specify
     * on which "band" we are doing the constrast enhancement.
     * If any of the values are too large to fit in a int, the method returns null.
     */
    public static int[][][] readImageInto2DArray(String fileName)
    {
        BufferedImage image = null;
        int height, width, band, numbands;

        image = readImageIntoBufferedImage( fileName );

        WritableRaster raster = image.getRaster();

        //GRAY: For now, the number of bands should be 1.
        int[] pixelValues = new int[ raster.getNumBands() ];
        height = raster.getHeight();
        width = raster.getWidth();
        //ADITI: Accomodates color image
        numbands = raster.getNumBands();

        // GRAY: This may be specified differently for color images
        //ADITI: This is not needed any longer
        //band = 0;

        //allocate our two dimensional array.
        //ADITI: Changed to accomodate color. It is better to have the channel as the first
        //dimension, since it can allow us to work with each channel independently.
        int rasterValues[][][];
        rasterValues = new int[numbands][][];
        for (band=0; band < numbands; band++)
        {
        	rasterValues[band] = new int[height][];
        	for ( int y = 0; y < rasterValues[band].length; y++ )
        	    rasterValues[band][y] = new int[ width ];
		}

        for ( int x = 0; x < width; x++ )
        	for( int y = 0; y <height; y++ )
        	{
				pixelValues = raster.getPixel( x, y, pixelValues);
				for (band = 0; band < numbands; band++)
				{
        	    	if ( pixelValues[band] > Integer.MAX_VALUE )
        	    	{
						System.out.println(" Pixel value too big for int.");
        	    	    return null;
        	    	}
        	    	else
        	            rasterValues[band][y][x] = pixelValues[band];
        	    }
			}

        return rasterValues;

    }
}