package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.*;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class PlanarToClearCLBufferConverterTest {



    @Test
    public void test2DPlanarConversion() {
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = IJ.openImage("src/test/resources/blobs.tif");

        ClearCLBuffer bufferIn = clij.push(imp);

        // here starts the actual test

        Planar<GrayU8> planar = clij.convert(bufferIn, Planar.class);

        ClearCLBuffer bufferOut = clij.convert(planar, ClearCLBuffer.class);

        TestUtilities.clBuffersEqual(clij, bufferIn, bufferOut, 0);

        bufferIn.close();
        bufferOut.close();
    }


    @Test
    public void test3DPlanarU8Conversion() {
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = TestUtilities.getRandomImage(100, 100, 5, 8, 0, 255);

        ClearCLBuffer bufferIn = clij.push(imp);

        // here starts the actual test

        Planar<GrayU8> planar = clij.convert(bufferIn, Planar.class);

        ClearCLBuffer bufferOut = clij.convert(planar, ClearCLBuffer.class);

        TestUtilities.clBuffersEqual(clij, bufferIn, bufferOut, 0);

        bufferIn.close();
        bufferOut.close();
    }

    @Test
    public void test3DPlanarS16Conversion() {
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = TestUtilities.getRandomImage(100, 100, 5, 16, 0, 255);

        ClearCLBuffer bufferIn = clij.push(imp);

        // here starts the actual test

        Planar<GrayS16> planar = clij.convert(bufferIn, Planar.class);

        ClearCLBuffer bufferOut = clij.convert(planar, ClearCLBuffer.class);

        TestUtilities.clBuffersEqual(clij, bufferIn, bufferOut, 0);

        bufferIn.close();
        bufferOut.close();
    }

    @Test
    public void test3DPlanarF32Conversion() {
        new ImageJ();

        CLIJ clij = CLIJ.getInstance();

        ImagePlus imp = TestUtilities.getRandomImage(100, 100, 5, 32, 0, 255);

        ClearCLBuffer bufferIn = clij.push(imp);

        // here starts the actual test

        Planar<GrayF32> planar = clij.convert(bufferIn, Planar.class);

        ClearCLBuffer bufferOut = clij.convert(planar, ClearCLBuffer.class);

        TestUtilities.clBuffersEqual(clij, bufferIn, bufferOut, 0);

        bufferIn.close();
        bufferOut.close();
    }

}