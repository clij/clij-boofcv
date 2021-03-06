package net.haesleinhuepf.clij.boofcv;

import boofcv.abst.filter.blur.BlurFilter;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clij.utilities.CLIJUtilities;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.numeric.real.FloatType;
import org.jruby.RubyProcess;

/**
 * CLIJBoofCVGaussianBlurBenchmarking
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 02 2019
 */
public class CLIJBoofCVGaussianBlurBenchmarking {
    public static void main(String... args) {

        // get test image
        ImagePlus original = TestUtilities.getRandomImage(1024, 1024, 1, 32, 0, 100);
        ImagePlus imp = new Duplicator().run(original);
        imp.show();
        RandomAccessibleInterval<FloatType> img = ImageJFunctions.convertFloat(imp);

        boolean showResults = true;

        // init GPU
        CLIJ clij = CLIJ.getInstance();
        new ImageJ();

        net.imagej.ImageJ ij = new net.imagej.ImageJ();

        // convert from ImageJ to GPU
        ClearCLBuffer bufferIn = clij.convert(imp, ClearCLBuffer.class);
        ClearCLBuffer bufferOut = clij.create(bufferIn);

        // convert from GPU to boofCV
        Planar<GrayF32> input = clij.convert(bufferIn, Planar.class);
        Planar<GrayF32> blurred = input.createSameShape();

        // size of the blur kernel. square region with a width of radius*2 + 1
        float sigma = 5;
        int radius = CLIJUtilities.sigmaToKernelSize(sigma) / 2;

        System.out.println("Image size: " + imp.getWidth() + "/" + imp.getHeight());
        System.out.println("sigma = " + sigma);
        for (int i = 0; i < 5; i++) {
            imp = new Duplicator().run(original);
            long time = System.currentTimeMillis();
            IJ.run(imp,"Gaussian Blur...", "sigma=" + sigma);
            System.out.println("ImageJ1 Gaussian blur took " + (System.currentTimeMillis() - time) + " msec");
        }
        if (showResults) {
            // show result
            clij.show(imp, "ImageJ1");
        }

        RandomAccessibleInterval gaussianBlurred = null;
        for (int i = 0; i < 5; i++) {
            long time = System.currentTimeMillis();
            gaussianBlurred = ij.op().filter().gauss(img, sigma, sigma);
            System.out.println("ImageJ-Ops Gaussian blur took " + (System.currentTimeMillis() - time) + " msec");
        }
        if (showResults) {
            // show result
            clij.show(gaussianBlurred, "ImageJ ops");
        }

        for (int i = 0; i < 5; i++) {
            long time = System.currentTimeMillis();
            GBlurImageOps.gaussian(input, blurred, sigma, radius, null);
            System.out.println("BoofCV Gaussian blur took " + (System.currentTimeMillis() - time) + " msec");
        }
        if (showResults) {
            // Show result
            clij.show(blurred, "boofcv");
        }

        for (int i = 0; i < 5; i++) {
            long time = System.currentTimeMillis();
            clij.op().blur(bufferIn, bufferOut, sigma, sigma, 0f);
            System.out.println("clij Gaussian blur took " + (System.currentTimeMillis() - time) + " msec");
        }
        if (showResults) {
            // show result
            clij.show(bufferOut, "clij");
        }

        // cleanup
        bufferIn.close();
        bufferOut.close();
    }
}
