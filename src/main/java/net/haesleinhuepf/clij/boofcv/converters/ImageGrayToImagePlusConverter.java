package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.*;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.converters.implementations.ClearCLBufferToRandomAccessibleIntervalConverter;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * ImageGrayToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ImageGrayToImagePlusConverter extends AbstractCLIJConverter<ImageGray, ImagePlus> {

    @Override
    public ImagePlus convert(ImageGray source) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        int depth = 1;

        ImagePlus result = null;

        if (source instanceof GrayU8) {
            result = NewImage.createByteImage("slice", width, height, depth, NewImage.FILL_BLACK);
            byte[] array = ((GrayU8) source).data;
            byte[] sliceArray = (byte[]) result.getProcessor().getPixels();
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
        } else if (source instanceof GrayU16) {
            result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);
            short[] array = ((GrayU16) source).data;
            short[] sliceArray = (short[]) result.getProcessor().getPixels();
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
        } else if (source instanceof GrayS16) {
            result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);
            short[] array = ((GrayS16) source).data;
            short[] sliceArray = (short[]) result.getProcessor().getPixels();
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
        } else if (source instanceof GrayF32) {
            result = NewImage.createFloatImage("slice", width, height, depth, NewImage.FILL_BLACK);
            float[] array = ((GrayF32) source).data;
            float[] sliceArray = (float[]) result.getProcessor().getPixels();
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
        } else {
            throw new IllegalArgumentException("Unknown image type: " + source);
        }

        return result;
    }

    public ImagePlus convertLegacy(ClearCLBuffer source) {
        ClearCLBufferToRandomAccessibleIntervalConverter cclbtraic = new ClearCLBufferToRandomAccessibleIntervalConverter();
        cclbtraic.setCLIJ(clij);
        RandomAccessibleInterval rai = cclbtraic.convert(source);
        return new Duplicator().run(ImageJFunctions.wrap(rai, "" + rai));
    }

    @Override
    public Class<ImageGray> getSourceType() {
        return ImageGray.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}
