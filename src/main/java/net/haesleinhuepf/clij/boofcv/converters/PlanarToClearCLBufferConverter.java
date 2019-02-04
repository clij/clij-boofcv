package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.*;
import ij.ImagePlus;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
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
public class PlanarToClearCLBufferConverter extends AbstractCLIJConverter<Planar, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(Planar source) {
        if (source.bands.length == 1) {
            return clij.convert(source.getBand(0), ClearCLBuffer.class);
        } else {

            long[] dimensions = {source.getWidth(), source.getHeight(), source.bands.length};

            NativeTypeEnum type = null;
            if (source.getBand(0) instanceof GrayU8) {
                type = NativeTypeEnum.UnsignedByte;
            } else if (source.getBand(0) instanceof GrayU16) {
                type = NativeTypeEnum.UnsignedShort;
            } else if (source.getBand(0) instanceof GrayS16) {
                type = NativeTypeEnum.Short;
            } else if (source.getBand(0) instanceof GrayF32) {
                type = NativeTypeEnum.Float;
            } else {
                throw new IllegalArgumentException("Unsupported type: " + source.getBand(0));
            }

            ClearCLBuffer stack = clij.createCLBuffer(dimensions, type);

            for (int z = 0; z < stack.getDepth(); z++) {
                ClearCLBuffer slice = clij.convert(source.getBand(z), ClearCLBuffer.class);
                clij.op().copySlice(slice, stack, z);
                slice.close();
            }

            return stack;
        }
    }

    @Override
    public Class<Planar> getSourceType() {
        return Planar.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
