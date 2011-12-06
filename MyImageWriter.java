
/*
 * --------------------------------------------------------------------------
 * CS111 - Digital Image Processing - Spring '10
 * Instructor - Prof Aditi Majumder
 * TA - Uddipan Mukherjee - umukherj@uci.edu
 * --------------------------------------------------------------------------
 This is a class for writing a sample image file
 The image data is in a 3D array - a 2D array for each color channel
 The filename of the image and the output filename are passed as  strings
 
 */

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;

public class MyImageWriter
{

  public static boolean writeImage2(String outputFileName, int[][][] imageData)
  {
      BufferedImage outputImage = new BufferedImage( imageData[0].length, imageData[0][0].length, BufferedImage.TYPE_BYTE_GRAY );
      WritableRaster outputRaster;
      outputRaster = outputImage.getRaster().createCompatibleWritableRaster();

      // GRAY
      // when writing color files, we will have to handle multible bands
      int[] pixelData = new int[ outputRaster.getNumBands() ];
      int band = 0;
      int numbands = outputRaster.getNumBands();

      int height, width;
      height = outputRaster.getHeight();
      width = outputRaster.getWidth();

      for ( int y = 0; y < height; y++ )
          for ( int x = 0; x < width; x++ )
          {
              for ( band = 0; band < numbands; band++ )
              {
                  pixelData[ band ] = imageData[band][y][x];
              }
              outputRaster.setPixel(x, y, pixelData );
          }
      outputImage.setData( outputRaster );

      File outputFile = new File( outputFileName );
      try
      {
          if ( !ImageIO.write( outputImage, "jpg", outputFile ))
          {
              System.out.println("Could not find image format for output image.");
              return false;
          }
      }
      catch ( Exception e )
      {
          System.out.println("Could not write output file.");
          return false;
      }

      return true;
  }


    public static boolean writeImage(String inputFileName, String outputFileName, int[][][] imageData)
    {
        BufferedImage inputImage = MyImageReader.readImageIntoBufferedImage( inputFileName );
        if ( inputImage == null )
        {
            System.out.println(" Could not open input image.");
            return false;
        }

        BufferedImage outputImage = new BufferedImage( inputImage.getWidth(), inputImage.getHeight(),
                                                       inputImage.getType() );
        WritableRaster outputRaster, inputRaster;
        inputRaster = inputImage.getRaster();
        outputRaster = inputRaster.createCompatibleWritableRaster();

        // GRAY
        // when writing color files, we will have to handle multible bands
        int[] pixelData = new int[ outputRaster.getNumBands() ];
        int band = 0;
        int numbands = outputRaster.getNumBands();

        int height, width;
        height = outputRaster.getHeight();
        width = outputRaster.getWidth();

        for ( int y = 0; y < height; y++ )
            for ( int x = 0; x < width; x++ )
            {
                for ( band = 0; band < numbands; band++ )
                {
                    pixelData[ band ] = imageData[band][y][x];
                }
                outputRaster.setPixel(x, y, pixelData );
            }
        outputImage.setData( outputRaster );

        File outputFile = new File( outputFileName );
        try
        {
            if ( !ImageIO.write( outputImage, "jpg", outputFile ))
            {
                System.out.println("Could not find image format for output image.");
                return false;
            }
        }
        catch ( Exception e )
        {
            System.out.println("Could not write output file.");
            return false;
        }

        return true;
    }
}