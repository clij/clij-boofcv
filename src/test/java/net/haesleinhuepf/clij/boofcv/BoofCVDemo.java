package net.haesleinhuepf.clij.boofcv;

import boofcv.abst.filter.blur.BlurFilter;
import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.filter.blur.GBlurImageOps;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageGray;
import boofcv.struct.image.ImageType;
import boofcv.struct.image.Planar;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * This demo shows how to convert an image to boofCV, process it using this library and convert the
 * result back to show it
 *
 * Author: @haesleinhuepf
 * 02 2019
 */
public class BoofCVDemo {
    public static void main(String... args) {

        // code adaped from Peter Abeless, https://boofcv.org/index.php?title=Example_Image_Blur accessed 2019-01-21
        CLIJ clij = CLIJ.getInstance();
        new ImageJ();
        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");
        imp.show();

        ClearCLBuffer bufferIn = clij.convert(imp, ClearCLBuffer.class);

        GrayU8 grayIn = (GrayU8) clij.convert(bufferIn, ImageGray.class);

        Planar<GrayU8> input = new Planar<GrayU8>(GrayU8.class, grayIn.getWidth(), grayIn.getHeight(), 1);
        input.setBand(0, grayIn);

        //ConvertBufferedImage.convertFrom(buffered, true, ImageType.pl(1, GrayU8.class));
        Planar<GrayU8> blurred = input.createSameShape();

        // size of the blur kernel. square region with a width of radius*2 + 1
        int radius = 8;

        // Apply gaussian blur using a procedural interface
        GBlurImageOps.gaussian(input,blurred,-1,radius,null);

        // Apply a mean filter using an object oriented interface.  This has the advantage of automatically
        // recycling memory used in intermediate steps
        BlurFilter<Planar<GrayU8>> filterMean = FactoryBlurFilter.mean(input.getImageType(),radius);
        filterMean.process(input, blurred);

        // Apply a median filter using image type specific procedural interface.  Won't work if the type
        // isn't known at compile time
        BlurImageOps.median(input,blurred,radius);


        // Show result
        ClearCLBuffer buffer = clij.convert(blurred.getBand(0), ClearCLBuffer.class);
        clij.show(buffer, "buffer");
    }
}
