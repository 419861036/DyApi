package kkd.common.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * 对图像操作的简化 API
 * @author Administrator
 *
 */
public class ImgUtil {
	/**
     * 对一个图像进行旋转
     * 
     * @param image
     *            图像
     * @param degree
     *            旋转角度, 90 为顺时针九十度， -90 为逆时针九十度
     * @return 旋转后得图像对象
     */
    public static BufferedImage rotate(BufferedImage image, int degree) {
        int iw = image.getWidth();// 原始图象的宽度
        int ih = image.getHeight();// 原始图象的高度
        int w = 0;
        int h = 0;
        int x = 0;
        int y = 0;
        degree = degree % 360;
        if (degree < 0)
            degree = 360 + degree;// 将角度转换到0-360度之间
        double ang = degree * 0.0174532925;// 将角度转为弧度

        /**
         * 确定旋转后的图象的高度和宽度
         */

        if (degree == 180 || degree == 0 || degree == 360) {
            w = iw;
            h = ih;
        } else if (degree == 90 || degree == 270) {
            w = ih;
            h = iw;
        } else {
            int d = iw + ih;
            w = (int) (d * Math.abs(Math.cos(ang)));
            h = (int) (d * Math.abs(Math.sin(ang)));
        }

        x = (w / 2) - (iw / 2);// 确定原点坐标
        y = (h / 2) - (ih / 2);
        BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());
        Graphics gs = rotatedImage.getGraphics();
        gs.fillRect(0, 0, w, h);// 以给定颜色绘制旋转后图片的背景
        AffineTransform at = new AffineTransform();
        at.rotate(ang, w / 2, h / 2);// 旋转图象
        at.translate(x, y);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        op.filter(image, rotatedImage);
        image = rotatedImage;
        return image;
    }
    /**
     * 自动等比缩放一个图片，多余的部分，用给定背景颜色补上<br />
     * 如果参数中的宽度或高度为<b>-1</b>的话，着按照指定的高度或宽度对原图等比例缩放图片，不添加背景颜色
     * 
     * @param im
     *            图像对象
     * @param w
     *            宽度
     * @param h
     *            高度
     * @param bgColor
     *            背景颜色
     * 
     * @return 被转换后的图像
     */
    public static BufferedImage zoomScale(BufferedImage im, int w, int h, Color bgColor) {

        // 检查背景颜色
        bgColor = null == bgColor ? Color.black : bgColor;
        // 获得尺寸
        int oW = im.getWidth();
        int oH = im.getHeight();
        float oR = (float) oW / (float) oH;
        float nR = (float) w / (float) h;

        int nW, nH, x, y;
        /*
         * 缩放
         */
        // 原图太宽，计算当原图与画布同高时，原图的等比宽度
        if (oR > nR) {
            nW = w;
            nH = (int) (((float) w) / oR);
            x = 0;
            y = (h - nH) / 2;
        }
        // 原图太长
        else if (oR < nR) {
            nH = h;
            nW = (int) (((float) h) * oR);
            x = (w - nW) / 2;
            y = 0;
        }
        // 比例相同
        else {
            nW = w;
            nH = h;
            x = 0;
            y = 0;
        }

        // 创建图像
        BufferedImage re = new BufferedImage(w, h, ColorSpace.TYPE_RGB);
        // 得到一个绘制接口
        Graphics gc = re.getGraphics();
        gc.setColor(bgColor);
        gc.fillRect(0, 0, w, h);
        gc.drawImage(im, x, y, nW, nH, bgColor, null);
        // 返回
        return re;
    }
    /**
     * 自动等比缩放一个图片
     * 
     * @param im
     *            图像对象
     * @param w
     *            宽度
     * @param h
     *            高度
     * 
     * @return 被转换后的图像
     */
    public static BufferedImage zoomScale(BufferedImage im, int w, int h) {
        // 获得尺寸
        int oW = im.getWidth();
        int oH = im.getHeight();

        int nW = w, nH = h;

        /*
         * 缩放
         */
        // 未指定图像高度，根据原图尺寸计算出高度
        if (h == -1) {
            nH = (int) ((float) w / oW * oH);
        }
        // 未指定图像宽度，根据原图尺寸计算出宽度
        else if (w == -1) {
            nW = (int) ((float) h / oH * oW);
        }

        // 创建图像
        BufferedImage re = new BufferedImage(nW, nH, ColorSpace.TYPE_RGB);
        re.getGraphics().drawImage(im, 0, 0, nW, nH, null);
        // 返回
        return re;
    }
    /**
     * 自动缩放剪切一个图片，令其符合给定的尺寸
     * <p>
     * 如果图片太大，则将其缩小，如果图片太小，则将其放大，多余的部分被裁减
     * 
     * @param im
     *            图像对象
     * @param w
     *            宽度
     * @param h
     *            高度
     * @return 被转换后的图像
     */
    public static BufferedImage clipScale(BufferedImage im, int w, int h) {
        // 获得尺寸
        int oW = im.getWidth();
        int oH = im.getHeight();
        float oR = (float) oW / (float) oH;
        float nR = (float) w / (float) h;

        int nW, nH, x, y;
        /*
         * 裁减
         */
        // 原图太宽，计算当原图与画布同高时，原图的等比宽度
        if (oR > nR) {
            nW = (h * oW) / oH;
            nH = h;
            x = (w - nW) / 2;
            y = 0;
        }
        // 原图太长
        else if (oR < nR) {
            nW = w;
            nH = (w * oH) / oW;
            x = 0;
            y = (h - nH) / 2;
        }
        // 比例相同
        else {
            nW = w;
            nH = h;
            x = 0;
            y = 0;
        }
        // 创建图像
        BufferedImage re = new BufferedImage(w, h, ColorSpace.TYPE_RGB);
        re.getGraphics().drawImage(im, x, y, nW, nH, Color.black, null);
        // 返回
        return re;
    }
    
    /**
     * Java's ImageIO can't process 4-component images and Java2D can't apply
     * AffineTransformOp either, so convert raster data to RGB. Technique due to
     * MArk Stephens. Free for any use.
     */
    @SuppressWarnings("unused")
	private static BufferedImage createJPEG4(Raster raster) {
        int w = raster.getWidth();
        int h = raster.getHeight();
        byte[] rgb = new byte[w * h * 3];

        float[] Y = raster.getSamples(0, 0, w, h, 0, (float[]) null);
        float[] Cb = raster.getSamples(0, 0, w, h, 1, (float[]) null);
        float[] Cr = raster.getSamples(0, 0, w, h, 2, (float[]) null);
        float[] K = raster.getSamples(0, 0, w, h, 3, (float[]) null);

        for (int i = 0, imax = Y.length, base = 0; i < imax; i++, base += 3) {
            float k = 220 - K[i], y = 255 - Y[i], cb = 255 - Cb[i], cr = 255 - Cr[i];

            double val = y + 1.402 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y - 0.34414 * (cb - 128) - 0.71414 * (cr - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 1] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);

            val = y + 1.772 * (cb - 128) - k;
            val = (val - 128) * .65f + 128;
            rgb[base + 2] = val < 0.0 ? (byte) 0 : val > 255.0 ? (byte) 0xff : (byte) (val + 0.5);
        }

        raster = Raster.createInterleavedRaster(new DataBufferByte(rgb, rgb.length),
                                                w,
                                                h,
                                                w * 3,
                                                3,
                                                new int[]{0, 1, 2},
                                                null);

        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs,
                                                false,
                                                true,
                                                Transparency.OPAQUE,
                                                DataBuffer.TYPE_BYTE);
        return new BufferedImage(cm, (WritableRaster) raster, true, null);
    }
    
}
