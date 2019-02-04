package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.*;
import ij.ImagePlus;
import ij.gui.NewImage;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.converters.implementations.ClearCLBufferToRandomAccessibleIntervalConverter;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

/**
 * ImageGrayToImagePlusConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
@Plugin(type = CLIJConverterPlugin.class)
public class PlanarToImagePlusConverter extends AbstractCLIJConverter<Planar, ImagePlus> {

    @Override
    public ImagePlus convert(Planar source) {
        int width = (int) source.getWidth();
        int height = (int) source.getHeight();
        int depth = source.getBands().length;

        ImagePlus result = null;

        if (source.getBand(0) instanceof GrayU8) {
            result = NewImage.createByteImage("slice", width, height, depth, NewImage.FILL_BLACK);
            for (int z = 0; z < depth; z++) {
                result.setZ(z + 1);
                byte[] array = ((GrayU8) source.getBand(z)).data;
                byte[] sliceArray = (byte[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else if (source.getBand(0) instanceof GrayU16) {
            result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);
            for (int z = 0; z < depth; z++) {
                result.setZ(z + 1);
                short[] array = ((GrayU16) source.getBand(z)).data;
                short[] sliceArray = (short[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else if (source.getBand(0) instanceof GrayS16) {
            result = NewImage.createShortImage("slice", width, height, depth, NewImage.FILL_BLACK);
            for (int z = 0; z < depth; z++) {
                result.setZ(z + 1);
                short[] array = ((GrayS16) source.getBand(z)).data;
                short[] sliceArray = (short[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else if (source.getBand(0) instanceof GrayF32) {
            result = NewImage.createFloatImage("slice", width, height, depth, NewImage.FILL_BLACK);
            for (int z = 0; z < depth; z++) {
                result.setZ(z + 1);
                float[] array = ((GrayF32) source.getBand(z)).data;
                float[] sliceArray = (float[]) result.getProcessor().getPixels();
                System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            }
        } else {
            throw new IllegalArgumentException("Unknown image type: " + source);
        }

        return result;
    }

    @Override
    public Class<Planar> getSourceType() {
        return Planar.class;
    }

    @Override
    public Class<ImagePlus> getTargetType() {
        return ImagePlus.class;
    }
}
