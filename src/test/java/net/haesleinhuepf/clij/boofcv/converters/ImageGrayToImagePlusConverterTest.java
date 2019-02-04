package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageGray;
import boofcv.struct.image.Planar;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageGrayToImagePlusConverterTest {
    @Test
    public void test2DGrayConversion() {
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = TestUtilities.getRandomImage(100, 100, 1, 8, 0, 255);

        ClearCLBuffer bufferIn = clij.push(imp);

        // here starts the actual test

        GrayU8 grayImage = (GrayU8) clij.convert(bufferIn, ImageGray.class);

        ImagePlus result = clij.convert(grayImage, ImagePlus.class);

        TestUtilities.compareImages(imp, result, 0);

        bufferIn.close();
    }
}