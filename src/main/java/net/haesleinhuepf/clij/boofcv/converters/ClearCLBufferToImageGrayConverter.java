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
 * ClearCLBufferToImageGrayConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ClearCLBufferToImageGrayConverter extends AbstractCLIJConverter<ClearCLBuffer, ImageGray> {

    @Override
    public ImageGray convert(ClearCLBuffer source) {
        if (source.getDimension() > 2) {
            throw new IllegalArgumentException("Only 2D images supported at the moment!");
        }

        int width = (int) source.getWidth();
        int height = (int) source.getHeight();

        int numberOfPixels = width * height;

        ImageGray result = null;

        if (source.getNativeType() == NativeTypeEnum.UnsignedByte) {
            GrayU8 image = new GrayU8(width, height);

            byte[] array = new byte[numberOfPixels];
            ByteBuffer buffer = ByteBuffer.wrap(array);
            source.writeTo(buffer, true);

            byte[] sliceArray = (byte[]) image.data;
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            return image;
        } else if (source.getNativeType() == NativeTypeEnum.UnsignedShort) {
            GrayU16 image = new GrayU16(width, height);

            short[] array = new short[numberOfPixels];
            ShortBuffer buffer = ShortBuffer.wrap(array);
            source.writeTo(buffer, true);

            short[] sliceArray = (short[]) image.data;
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            return image;
        } else if (source.getNativeType() == NativeTypeEnum.Short) {
            GrayS16 image = new GrayS16(width, height);

            short[] array = new short[numberOfPixels];
            ShortBuffer buffer = ShortBuffer.wrap(array);
            source.writeTo(buffer, true);

            short[] sliceArray = (short[]) image.data;
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            return image;
        } else if (source.getNativeType() == NativeTypeEnum.Float) {
            GrayF32 image = new GrayF32(width, height);

            float[] array = new float[numberOfPixels];
            FloatBuffer buffer = FloatBuffer.wrap(array);
            source.writeTo(buffer, true);

            float[] sliceArray = (float[]) image.data;
            System.arraycopy(array, 0, sliceArray, 0, sliceArray.length);
            return image;
        } else {
            throw new IllegalArgumentException("Unknown type for ->boofcv conversion: " + source.getNativeType());
        }
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<ImageGray> getTargetType() {
        return ImageGray.class;
    }
}
