package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlanarToImagePlusConverterTest {
    @Test
    public void test3DGrayConversion() {
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = TestUtilities.getRandomImage(100, 100, 10, 8, 0, 255);

        ClearCLBuffer bufferIn = clij.push(imp);

        // here starts the actual test

        Planar<GrayU8> planar = clij.convert(bufferIn, Planar.class);

        ImagePlus result = clij.convert(planar, ImagePlus.class);

        TestUtilities.compareImages(imp, result, 0);

        bufferIn.close();
    }
}