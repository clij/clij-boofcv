package net.haesleinhuepf.clij.boofcv;

import boofcv.alg.filter.binary.GThresholdImageOps;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageGray;
import boofcv.struct.image.Planar;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import net.imglib2.view.Views;

/**
 * CLIJBoofCVGaussianBlurBenchmarking
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 02 2019
 */
public class CLIJBoofCVOtsuThresholdingBenchmarking {
    public static void main(String... args) {

        // get test image
        ImagePlus imp = TestUtilities.getRandomImage(10240, 10240, 1, 32, 0, 100);
        imp.show();
        RandomAccessibleInterval<FloatType> img = ImageJFunctions.convertFloat(imp);

        // init GPU
        CLIJ clij = CLIJ.getInstance();
        new ImageJ();

        net.imagej.ImageJ ij = new net.imagej.ImageJ();

        // convert from ImageJ to GPU
        ClearCLBuffer bufferIn = clij.convert(imp, ClearCLBuffer.class);
        ClearCLBuffer bufferOut = clij.create(bufferIn.getDimensions(), NativeTypeEnum.UnsignedByte);

        // convert from GPU to boofCV
        GrayF32 input = (GrayF32) clij.convert(bufferIn, ImageGray.class);
        GrayU8 thresholded = new GrayU8(input.width,input.height);;

        // size of the blur kernel. square region with a width of radius*2 + 1
        float sigma = 5;
        int radius = CLIJUtilities.sigmaToKernelSize(sigma) / 2;

        System.out.println("Image size: " + imp.getWidth() + "/" + imp.getHeight());
        System.out.println("sigma = " + sigma);
        for (int i = 0; i < 5; i++) {
            long time = System.currentTimeMillis();
            IJ.setAutoThreshold(imp,"Otsu dark");
            IJ.run(imp, "Convert to Mask", "");
            //IJ.run(imp,"Gaussian Blur...", "sigma=" + sigma);
            System.out.println("ImageJ1 Otsu threshold took " + (System.currentTimeMillis() - time) + " msec");
        }

        for (int i = 0; i < 5; i++) {
            long time = System.currentTimeMillis();
            ij.op().threshold().otsu(Views.iterable(img));
            System.out.println("ImageJ-Ops Otsu threshold took " + (System.currentTimeMillis() - time) + " msec");
        }

        for (int i = 0; i < 5; i++) {
            long time = System.currentTimeMillis();
            GThresholdImageOps.threshold(input, thresholded, GThresholdImageOps.computeOtsu(input, 0, 255), true);
            System.out.println("BoofCV Otsu threshold took " + (System.currentTimeMillis() - time) + " msec");
        }

        for (int i = 0; i < 5; i++) {
            long time = System.currentTimeMillis();
            clij.op().automaticThreshold(bufferIn, bufferOut, "Otsu", 0f, 255f, 256);
            System.out.println("clij Otsu threshold took " + (System.currentTimeMillis() - time) + " msec");
        }

        // Show result
        ClearCLBuffer buffer = clij.convert(thresholded, ClearCLBuffer.class);
        clij.show(buffer, "buffer");

        // cleanup
        bufferIn.close();
        bufferOut.close();
        buffer.close();
    }
}
