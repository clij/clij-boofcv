package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.ImageGray;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImageGrayToClearCLBufferConverterTest {
    @Test
    public void test2DGrayConversion() {
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer bufferIn = clij.push(imp);

        // here starts the actual test

        GrayU8 planar = (GrayU8) clij.convert(bufferIn, ImageGray.class);

        ClearCLBuffer bufferOut = clij.convert(planar, ClearCLBuffer.class);

        TestUtilities.clBuffersEqual(clij, bufferIn, bufferOut, 0);

        bufferIn.close();
        bufferOut.close();
    }
}