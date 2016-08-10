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
package exactitude.impl.utils;

import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

/**
 * @author bpmurray
 *
 *  The code that manages TIFF metadata is based on Maxideon's code
 *  at http://forums.sun.com/thread.jspa?threadID=5419900
 */
public class MetadataReaderUtil {
   /**
    * Uses a TIFFImageReader plugin to parse the given exif data into tiff
    * tags. The returned IIOMetadata is in whatever format the tiff ImageIO
    * plugin uses. If there is no tiff plugin, then this method returns null.
    */
   @SuppressWarnings("unused")
   private static IIOMetadata getTiffMetaFromEXIF(byte[] exif) {
      java.util.Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(exactitude.core.Constants.TIFF_TYPE);

      ImageReader iReader;
      if (!readers.hasNext()) {
         return null;
      }

      iReader = readers.next();

      // skip the 6 byte exif header
      ImageInputStream wrapper = new MemoryCacheImageInputStream(
            new java.io.ByteArrayInputStream(exif, exactitude.core.Constants.METAHeaderLenEXIF, exif.length - exactitude.core.Constants.METAHeaderLenEXIF));
      iReader.setInput(wrapper, true, false);

      IIOMetadata exifMeta;
      try {
         exifMeta = iReader.getImageMetadata(0);
      } catch (Exception e) {
         // shouldn't happen
         throw new Error(e);
      }

      iReader.dispose();
      return exifMeta;
   }

   /**
    * Uses a TIFFImageReader plugin to parse the given tiff data into exif
    * tags. The returned IIOMetadata is in whatever format the jpeg ImageIO
    * plugin uses.
    */
   @SuppressWarnings("unused")
   private static IIOMetadata getEXIFMetaFromTiff(byte[] tiff) {
      java.util.Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(exactitude.core.Constants.JPEG_TYPE);

      ImageReader iReader;
      if (!readers.hasNext()) {
         return null;
      }

      iReader = readers.next();

      // skip the 6 byte exif header
      ImageInputStream wrapper = new MemoryCacheImageInputStream(new java.io.ByteArrayInputStream(tiff, 0, tiff.length));
      iReader.setInput(wrapper, true, false);

      IIOMetadata exifMeta;
      try {
         exifMeta = iReader.getImageMetadata(0);
      } catch (Exception e) {
         // shouldn't happen
         throw new Error(e);
      }

      iReader.dispose();
      return exifMeta;
   }



   /**
    * Uses a TIFFImageReader plugin to parse the given iptc data into tiff
    * tags. The returned IIOMetadata is in whatever format the tiff ImageIO
    * plugin uses. If there is no tiff plugin, then this method returns null.
    *
    * @param iptc - The IPTC data
    * @return - An IIOMetadata object
    */
   public static IIOMetadata getTiffMetaFromIPTC(byte[] iptc) {
      java.util.Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(exactitude.core.Constants.TIFF_TYPE);

      ImageReader iReader;
      if (!readers.hasNext()) {
         return null;
      }

      iReader = readers.next();

      // skip the 14 byte iptc header
      ImageInputStream wrapper = new MemoryCacheImageInputStream(
            new java.io.ByteArrayInputStream(iptc, exactitude.core.Constants.METAHeaderLenIPTC, iptc.length - exactitude.core.Constants.METAHeaderLenIPTC));
      iReader.setInput(wrapper, true, false);

      IIOMetadata iptcMeta;
      try {
         iptcMeta = iReader.getImageMetadata(0);
      } catch (Exception e) {
         // shouldn't happen
         throw new Error(e);
      }

      iReader.dispose();
      return iptcMeta;
   }

   /**
    * Extract all of the metadata from a file, in all of the supported formats.
    * 
    * @param in - The files <code>ImageInputStream</code>
    * @return - An array of the <code>IIOMetadata</code> metadata objects 
    * @throws IOException
    */
   public static ArrayList<IIOMetadataNode> getAllMetadata(ImageInputStream in) throws IOException {
      ArrayList<IIOMetadataNode> metadataList = new ArrayList<IIOMetadataNode>();
      java.util.Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
      ImageReader iReader = null;
      while (readers.hasNext()) {
         iReader = readers.next();
         String formatName = iReader.getOriginatingProvider().getNativeImageMetadataFormatName();
         IIOMetadata metadata = null;

         iReader.setInput(in, false, false);

         // Get any stream metadata
         metadata = iReader.getStreamMetadata();
         if (null != metadata) {
            metadataList.add((IIOMetadataNode) metadata.getAsTree(formatName));
         }

         // Get the image metadata
         int nImages = iReader.getNumImages(true);
         for (int iX=0; iX<nImages; iX++) {
            try {
               metadata = iReader.getImageMetadata(iX);
            } catch (Exception ex) {
               metadata = null;
            }

            // We now have the metadata for this image in this reader
            if (null != metadata) {
               if (exactitude.core.Constants.JPEG_FORMAT.equals(formatName)) {

                  IIOMetadataNode jpgMetadata = (IIOMetadataNode) metadata.getAsTree(formatName);

                  if ("unknown".equals(jpgMetadata.getNodeName()) && "225".equals(jpgMetadata.getAttribute("MarkerTag"))) {
                     IIOMetadata exifMeta = ExifUtil.getEXIFMetadata(jpgMetadata);
                     IIOMetadataNode exifNode = (IIOMetadataNode) exifMeta.getAsTree(exactitude.core.Constants.TIFF_FORMAT);
                     metadataList.add(exifNode);   
                  } else {
                     IIOMetadataNode metadataNode = (IIOMetadataNode) metadata.getAsTree(formatName);
                     metadataList.add(metadataNode);
                  }
               } else {
                  IIOMetadataNode metadataNode = (IIOMetadataNode) metadata.getAsTree(formatName);
                  metadataList.add(metadataNode);
               }
            }
         }
         iReader.dispose();
      }
      return metadataList;
   }

}
