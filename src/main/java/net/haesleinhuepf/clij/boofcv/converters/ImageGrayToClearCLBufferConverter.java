package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.*;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.converters.implementations.RandomAccessibleIntervalToClearCLBufferConverter;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.display.imagej.ImageJFunctions;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * ImageGrayToClearCLBufferConverter
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
@Plugin(type = CLIJConverterPlugin.class)
public class ImageGrayToClearCLBufferConverter extends AbstractCLIJConverter<ImageGray, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(ImageGray source) {
        long[] dimensions = new long[]{source.getWidth(), source.getHeight()};

        if (source instanceof GrayU8) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedByte);
            byte[] outputArray = ((GrayU8) source).data;
            ByteBuffer byteBuffer = ByteBuffer.wrap(outputArray);
            target.readFrom(byteBuffer, true);
            return target;
        } else if (source instanceof GrayU16) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.UnsignedShort);
            short[] outputArray = ((GrayU16) source).data;
            ShortBuffer shortBuffer = ShortBuffer.wrap(outputArray);
            target.readFrom(shortBuffer, true);
            return target;
        } else if (source instanceof GrayS16) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.Short);
            short[] outputArray = ((GrayS16) source).data;
            ShortBuffer shortBuffer = ShortBuffer.wrap(outputArray);
            target.readFrom(shortBuffer, true);
            return target;
        } else if (source instanceof GrayF32) {
            ClearCLBuffer target = clij.createCLBuffer(dimensions, NativeTypeEnum.Float);
            float[] outputArray = ((GrayF32) source).data;
            FloatBuffer floatBuffer = FloatBuffer.wrap(outputArray);
            target.readFrom(floatBuffer, true);
            return target;

        } else {
            throw new IllegalArgumentException("Unknown type for boofcy-> conversion: " + source);
        }
    }

    @Override
    public Class<ImageGray> getSourceType() {
        return ImageGray.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
