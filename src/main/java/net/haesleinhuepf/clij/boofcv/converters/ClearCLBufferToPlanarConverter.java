package net.haesleinhuepf.clij.boofcv.converters;

import boofcv.struct.image.*;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
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
public class ClearCLBufferToPlanarConverter extends AbstractCLIJConverter<ClearCLBuffer, Planar> {

    @Override
    public Planar convert(ClearCLBuffer source) {
        if (source.getDepth() == 1 || source.getDimension() == 2) {
            ImageGray grayIn = clij.convert(source, ImageGray.class);
            Planar output = new Planar(grayIn.getClass(), grayIn.getWidth(), grayIn.getHeight(), 1);
            output.setBand(0, grayIn);
            return output;
        } else {
            long[] dimensions = {source.getWidth(), source.getHeight()};
            ClearCLBuffer plane = clij.createCLBuffer(dimensions, source.getNativeType());


            Planar output = null;
            for (int z = 0; z < source.getDepth(); z++) {
                clij.op().copySlice(source, plane, z);
                ImageGray grayIn = clij.convert(plane, ImageGray.class);
                if (output == null) {
                    output = new Planar(grayIn.getClass(), grayIn.getWidth(), grayIn.getHeight(), 1);
                }
                output.setBand(z, grayIn);
            }
            plane.close();
            return output;




        }
    }

    @Override
    public Class<ClearCLBuffer> getSourceType() {
        return ClearCLBuffer.class;
    }

    @Override
    public Class<Planar> getTargetType() {
        return Planar.class;
    }
}
