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
        return clij.convert(source.getBand(0), ClearCLBuffer.class);
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
