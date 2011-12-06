import java.awt.image.*;

class Main {
	public static void main(String[] args) {
    int[][][] imgData = MyImageReader.readImageInto2DArray("flowergray.jpg");
    // int[][][] imgData = MyImageReader.readImageInto2DArray("CARTOON.jpg");
		
		int iw = imgData[0][0].length;
		int ih = imgData[0].length;
		
		int band = 0;
		int fw = 2;
		int fh = 2;
		float[][] filter = new float[fw][fh];
		filter[0][0] = 0.25f;
		filter[0][1] = 0.25f;
		filter[1][0] = 0.25f;
		filter[1][1] = 0.25f;
		
		// int fw = 3;
		// int fh = 3;
		// float[][] filter = new float[fw][fh];
		// filter[0][0] = 1.0f/9.0f;
		// filter[0][1] = 1.0f/9.0f;
		// filter[0][2] = 1.0f/9.0f;
		// filter[1][0] = 1.0f/9.0f;
		// filter[2][0] = 1.0f/9.0f;
		// filter[1][1] = 1.0f/9.0f;
		// filter[1][2] = 1.0f/9.0f;
		// filter[2][1] = 1.0f/9.0f;
		// filter[2][2] = 1.0f/9.0f;
		
    int band2 = 0;
    int fw2 = 3;
    int fh2 = 3;
    float[][] filter2 = new float[fw2][fh2];
    filter2[0][0] = -0.125f;
    filter2[0][1] = -0.125f;
    filter2[0][2] = -0.125f;
    filter2[1][0] = -0.125f;
    filter2[2][0] = -0.125f;
    filter2[1][1] = 1;
    filter2[1][2] = -0.125f;
    filter2[2][1] = -0.125f;
    filter2[2][2] = -0.125f;
		
    int[][][] levelData = imgData.clone();
    for (int lvl = 0; lvl < 8; lvl++) {
      // convolute by shrinking image
      int[][] imgData2 = convolve2(levelData[band], filter, levelData[band].length, levelData[band][0].length, fw, fh);
      levelData[0] = imgData2;
      
      imgData2 = resizeBilinearGray2(imgData2, imgData2[0].length, imgData2.length, imgData[0][0].length, imgData[0].length);
      int[][][] levelData3 = new int[1][imgData2.length][imgData2[0].length];
      levelData3[0] = imgData2;
      
      MyImageWriter.writeImage2("gen/GaussianPyramidSameSize/GaussianPyramidImage0Level" + lvl + ".jpg", levelData3);
    }
    
    levelData = imgData.clone();
    for (int lvl = 0; lvl < 8; lvl++) {
      int[][] imgData2 = convolve2(levelData[band], filter, levelData[band].length, levelData[band][0].length, fw, fh);
      levelData[0] = imgData2;
      MyImageWriter.writeImage2("gen/GaussianPyramid/GaussianPyramidImage0Level" + lvl + ".jpg", levelData);
    }
    
    levelData = imgData.clone();
    for (int lvl = 0; lvl < 10; lvl++) {
     int[][] imgData2 = convolve2(levelData[band], filter, levelData[band].length, levelData[band][0].length, fw, fh);
     int[][] imgData3 = resizeBilinearGray2(imgData2, imgData2[0].length, imgData2.length, levelData[0][0].length, levelData[0].length);
     
     int[][][] lapLevelData = new int[1][levelData.length][levelData[0].length];
     lapLevelData[0] = subtract(levelData[0], imgData3);
     levelData[0] = imgData2;
     MyImageWriter.writeImage2("gen/LaplacianPyramidImage/LaplacianPyramidImage1Level" + lvl + ".jpg", lapLevelData);
    }
		
    int boxband = 0;
    int boxfw = 2;
    int boxfh = 2;
    float[][] boxfilter = new float[boxfw][boxfh];
    boxfilter[0][0] = 0.25f;
    boxfilter[0][1] = 0.25f;
    boxfilter[1][0] = 0.25f;
    boxfilter[1][1] = 0.25f;
    
    levelData = imgData.clone();
    for (int lvl = 0; lvl < 8; lvl++) {
      int[][] imgData2 = convolve2(levelData[boxband], boxfilter, levelData[boxband].length, levelData[boxband][0].length, boxfw, boxfh);
      levelData[0] = imgData2;
      
      imgData2 = resizeBilinearGray2(imgData2, imgData2[0].length, imgData2.length, imgData[0][0].length, imgData[0].length);
      int[][][] levelData3 = new int[1][imgData2.length][imgData2[0].length];
      levelData3[0] = imgData2;
      
      int[][] imgData3 = convolve(levelData3[band], filter2, levelData3[band].length, levelData3[band][0].length, fw2, fh2);
      int[][][] lapLevelData = new int[1][imgData3.length][imgData3[0].length];
      lapLevelData[0] = imgData3;
      MyImageWriter.writeImage2("gen/LapOp/LapOp" + lvl + ".jpg", lapLevelData);
      
      
      int[][] segmentedImgData = segment(imgData3);
      int[][][] segLevelData = new int[1][segmentedImgData.length][segmentedImgData[0].length];
      segLevelData[0] = segmentedImgData;
      MyImageWriter.writeImage2("gen/LapOpSegmented/LapOpSegmented" + lvl + ".jpg", segLevelData);
      
      
      int[][] zeroCrossImgData = detectZeroCrossing(segmentedImgData);
      int[][][] zeroCrossLevelData = new int[1][zeroCrossImgData.length][zeroCrossImgData[0].length];
      zeroCrossLevelData[0] = zeroCrossImgData;
      MyImageWriter.writeImage2("gen/LapOpSegmentedZeroCrossed/LapOpSegmentedZeroCrossed" + lvl + ".jpg", zeroCrossLevelData);
      
      
      int[][] varianceImgData = detectVarianceCrossing(zeroCrossImgData, imgData3, imgData2);
      int[][][] varianceLevelData = new int[1][zeroCrossImgData.length][zeroCrossImgData[0].length];
      varianceLevelData[0] = varianceImgData;
       
      MyImageWriter.writeImage2("gen/LapOpSegmentedZeroCrossedVariance/LapOpSegmentedZeroCrossedVarianceImage1Level" + lvl + ".jpg", varianceLevelData);
    }
	}
	
	public static int[][] convolve(int[][] gsImg, float[][] filter, int iw, int ih, int fw, int fh) {
		int[][] newImg = new int[ih][iw];
		for (int x = 0; x < iw; x++) {
			for (int y = 0; y < ih; y++) {
				int sum = 0;
				
				for (int fx = fw-1; fx >= 0; fx--) {
					for (int fy = fh-1; fy >= 0; fy--) {
						if ( (x - fx) < 0 || (y + fy) < 0 || (x - fx) > (iw-1) || (y + fy) > (ih-1) ) {
							sum += 0;
						}
						else {
							sum += gsImg[y + fy][x - fx] * filter[fx][fy];
						}
					}
				}
				
				newImg[y][x] = sum;
			}
		}
		return newImg;
	}
	
	public static int[][] convolve2(int[][] gsImg, float[][] filter, int iw, int ih, int fw, int fh) {
		int[][] newImg = new int[ih/fh][iw/fw];
		for (int x = 0; x < iw/fw; x++) {
			for (int y = 0; y < ih/fh; y++) {
				int sum = 0;
				
				for (int fx = fw-1; fx >= 0; fx--) {
					for (int fy = fh-1; fy >= 0; fy--) {
					  
					  if ( (x*fw) - fx < 0 ) {
							sum += gsImg[(y*fh) + fy][-((x*fw) - fx)] * filter[fx][fy];
					  }
					  else if ((y*fh) + fy < 0) {
							sum += gsImg[-((y*fh) + fy)][(x*fw) - fx] * filter[fx][fy];
					  }
					  else if ((x*fw) - fx > (iw-1)) {
							sum += gsImg[(y*fh) + fy][(iw-1)+((iw-1)-((x*fw) - fx))] * filter[fx][fy];
					  }
					  else if ((y*fh) + fy > (ih-1)) {
							sum += gsImg[(ih-1)+((ih-1)-((y*fh) + fy))][(x*fw) - fx] * filter[fx][fy];
					  }
            // if ( (x*fw) - fx < 0 || (y*fh) + fy < 0 || (x*fw) - fx > (iw-1) || (y*fh) + fy > (ih-1) ) {
            //  sum += 0;
            // }
						else {
							sum += gsImg[(y*fh) + fy][(x*fw) - fx] * filter[fx][fy];
						}
					}
				}
				
				newImg[y][x] = sum;
			}
		}
		return newImg;
	}
	
	// a and b must be same size
	public static int[][] subtract(int[][] a, int[][] b) {
		int[][] result = new int[a.length][a[0].length];
		for (int x = 0; x < a.length; x++) {
			for (int y = 0; y < a[0].length; y++) {
        // System.out.println(a[y][x]  + " " + b[y][x]);
				result[y][x] = 100+(a[y][x] - b[y][x]);
			}
		}
		return result;
	}
	
	public static int[][] segment(int[][] a) {
		int[][] result = new int[a.length][a[0].length];
		for (int x = 0; x < a.length; x++) {
			for (int y = 0; y < a[0].length; y++) {
				if (a[y][x] > 0) {
					result[y][x] = 255;
				}
				else {
					result[y][x] = 0;
				}
			}
		}
		return result;
	}
	
	public static int[][] detectZeroCrossing(int[][] a) {
		int[][] result = new int[a.length][a[0].length];
		for (int x = 0; x < a.length; x++) {
			for (int y = 0; y < a[0].length; y++) {
				
				int val = a[y][x];
				
				if (x > 0 && val != a[y][x-1]) {
					result[y][x] = 255;
				}
				else if (y > 0 && val != a[y-1][x]) {
					result[y][x] = 255;
				}
				else if (x > 0 && y > 0 && val != a[y-1][x-1]) {
					result[y][x] = 255;
				}
				else if (x < a.length-1 && val != a[y][x+1]) {
					result[y][x] = 255;
				}
				else if (y < a[0].length-1 && val != a[y+1][x]) {
					result[y][x] = 255;
				}
				else if (x < a.length-1 && y < a[0].length-1 && val != a[y+1][x+1]) {
					result[y][x] = 255;
				}
				else {
					result[y][x] = 0;
				}
			}
		}
		return result;
	}
	
	public static int[][] detectVarianceCrossing(int[][] zeroCrossingImgData, int[][] secondOrderDrvImgData, int[][] gaussianImgData) {
	  int threshold = 60000;
		int[][] result = new int[zeroCrossingImgData.length][zeroCrossingImgData[0].length];
		for (int x = 0; x < zeroCrossingImgData.length; x++) {
			for (int y = 0; y < zeroCrossingImgData[0].length; y++) {
				int val = zeroCrossingImgData[y][x];
				
				result[y][x] = 0;
				
				if (val == 255) {
					if (x > 0) {
						result[y][x] += Math.pow(secondOrderDrvImgData[y][x] - gaussianImgData[y][x], 2);
					}
					else if (y > 0) {
						result[y][x] += Math.pow(secondOrderDrvImgData[y][x] - gaussianImgData[y][x], 2);
					}
					else if (x > 0 && y > 0) {
						result[y][x] += Math.pow(secondOrderDrvImgData[y][x] - gaussianImgData[y][x], 2);
					}
					else if (x < zeroCrossingImgData.length-1) {
						result[y][x] += Math.pow(secondOrderDrvImgData[y][x] - gaussianImgData[y][x], 2);
					}
					else if (y < zeroCrossingImgData[0].length-1) {
						result[y][x] += Math.pow(secondOrderDrvImgData[y][x] - gaussianImgData[y][x], 2);
					}
					else if (x < zeroCrossingImgData.length-1 && y < zeroCrossingImgData[0].length-1) {
						result[y][x] += Math.pow(secondOrderDrvImgData[y][x] - gaussianImgData[y][x], 2);
					}
					else {
						result[y][x] += 0;
					}
					
					if (result[y][x] < threshold) {
						result[y][x] = 0;
					}
				}
			}
		}
		return result;
	}
	
	public static int[][] resizeBilinearGray2(int[][] pixels, int w, int h, int w2, int h2) {
	    int[][] temp = new int[h2][w2];
	    int A, B, C, D, x, y, index, gray ;
	    float x_ratio = ((float)(w-1))/w2 ;
	    float y_ratio = ((float)(h-1))/h2 ;
	    float x_diff, y_diff, ya, yb ;
	    int offset = 0 ;
	    for (int i=0;i<h2;i++) {
	        for (int j=0;j<w2;j++) {
	            x = (int)(x_ratio * j) ;
	            y = (int)(y_ratio * i) ;
	            x_diff = (x_ratio * j) - x ;
	            y_diff = (y_ratio * i) - y ;
	            
	            
	            if (x + 1 < w && y + 1 < h) {
  	            // range is 0 to 255 thus bitwise AND with 0xff
  	            A = pixels[y][x] & 0xff;
  	            B = pixels[y][x+1] & 0xff;
  	            C = pixels[y+1][x] & 0xff;
  	            D = pixels[y+1][x+1] & 0xff;
	            
  	            // Y = A(1-w)(1-h) + B(w)(1-h) + C(h)(1-w) + Dwh
  	            gray = (int)(
  	                    A*(1-x_diff)*(1-y_diff) +  B*(x_diff)*(1-y_diff) +
  	                    C*(y_diff)*(1-x_diff)   +  D*(x_diff*y_diff)
  	                    ) ;
	
  	            temp[i][j] = gray;
	            }
	            else {
  	            temp[i][j] = pixels[y][x] & 0xff;
	            }
	        }
	    }
	    return temp ;
	}
}