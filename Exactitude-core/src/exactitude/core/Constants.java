/**
 * Software License Agreement (BSD License)
 *
 * Copyright 2010 Brendan Murray. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY BRENDAN MURRAY ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BRENDAN MURRAY OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
 *
 */
package exactitude.core;

/**
 * This class is a convenience class to store common constants
 * 
 * @author Brendan Murray
 *
 */
public class Constants {
   /**
    * This is the default name of the configuration file used to
    * define the various elements.
    */
   public static final String DEFAULT_DESCRIPTOR = "converter-descriptors.xml";

   /**
    * This is the default name of the file where events are logged.
    */
   public static final String DEFAULT_LOGFILE    = "exactitude.log";

   /**
    * This is the name of the class on which the logger is based.
    */
   public static final String LOGGER_CLASS       = "exactitude.core.exactitude";

   /**
    * This is the ID of the logger.
    */
   public static final String LOG_ID             = "exactitude.core.logger";
   
   /**
    * The name of the default standard metadata format in ImageIO
    */
   public static final String STANDARD_FORMAT    = "javax_imageio_1.0";
   
   /**
    * The name of the metadata format for PNG files in ImageIO
    */
   public static final String PNG_FORMAT         = "javax_imageio_png_1.0";
   
   /**
    * The name of the image metadata format for PNG files in ImageIO
    */
   public static final String JPEG_FORMAT        = "javax_imageio_jpeg_image_1.0";
   
   /**
    * The name of the stream metadata format for PNG files in ImageIO
    */
   public static final String JPEG_SFORMAT       = "javax_imageio_jpeg_stream_1.0";
   
   /**
    * The name of the default standard metadata format in ImageIO
    */
   public static final String GIF_FORMAT         = "javax_imageio_gif_image_1.0";
   
   /**
    * The name of the  metadata format for GIF files in ImageIO
    */
      public static final String GIF_SFORMAT        = "javax_imageio_gif_stream_1.0";
      
   /**
    * The name of the metadata format for TIFF files in JAI
    */
   public static final String TIFF_FORMAT        = "com_sun_media_imageio_plugins_tiff_image_1.0";
   /**
    * The name of the metadata format for TIFF files in JAI
    */
   public static final String TIFF_SFORMAT       = "com_sun_media_imageio_plugins_tiff_stream_1.0";
   /**
    * The name of the metadata format for BMP files in JAI
    */
   public static final String BMP_FORMAT         = "com_sun_media_imageio_plugins_bmp_image_1.0";
   /**
    * The name of the metadata format for JPEG2000 files in JAI
    */
   public static final String JPEG2000_FORMAT    = "com_sun_media_imageio_plugins_jpeg2000_image_1.0";
   /**
    * The name of the image metadata format for WBMP files in JAI
    */
   public static final String WBMP_FORMAT        = "com_sun_media_imageio_plugins_wbmp_image_1.0";
   /**
    * The name of the stream metadata format for WBMP files in JAI
    */
   public static final String WBMP_SFORMAT       = "com_sun_media_imageio_plugins_wbmp_stream_1.0";
   
   /**
    * The file type for TIFF files
    */
   public static final String TIFF_TYPE          = "tif";
   
   /**
    * The file type for JPEG files
    */
   public static final String JPEG_TYPE          = "jpg";
   
   /**
    * The file type for PNG files
    */
   public static final String PNG_TYPE           = "png";
   
   
   /**
    * The length of the EXIF header ("Exif\0\0")
    */
   public static final int    METAHeaderLenEXIF  = 6;
   
   /**
    * The length of the IPTC header
    */
   public static final int    METAHeaderLenIPTC  = 14;
}
