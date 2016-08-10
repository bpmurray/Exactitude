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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exactitude.impl.converter.ImageIOConverter;

/**
 * @author bpmurray
 *
 * This class provides generic functions for manipulation of EXIF data
 */
public class ExifUtil {
   private static final String TAGMARKER_APP1  = "225";
   private static final String TAGMARKER_APP13 = "237";

   private static final String ID_EXIF         = "Exif\0\0";


   /**
    * Provide an indentation level when dumping to text
    *
    * @param level - the current level of indentation
    * @param out - the output stream on which the indentation is being printed
    */
   private static void indent(int level, PrintStream out) {
      for (int i = 0; i < level; i++) {
         out.print(" ");
      }
   }

   /**
    * Convert a node to a string
    *
    * @param node - the Node to be converted
    * @return the node in string form
    */
   public static String StringizeNode(Node node) {
      StringBuffer sb = new StringBuffer("<");
      sb.append(node.getNodeName());
      NamedNodeMap map = node.getAttributes();
      if (map != null) { // print attribute values
         int length = map.getLength();
         for (int i = 0; i < length; i++) {
            Node attr = map.item(i);
            sb.append(" ").append(attr.getNodeName()).append("=\"").append(attr.getNodeValue()).append("\"");
         }
      }
      Node child = node.getFirstChild();
      if (child != null) {
         sb.append(">");
         while (child != null) { // emit child tags recursively
            sb.append(StringizeNode(child));
            child = child.getNextSibling();
         }
         sb.append("</").append(node.getNodeName()).append(">");
      } else {
         sb.append("/>");
      }
      return sb.toString();
   }

   /**
    * Display a set of nodes in human-legible format
    *
    * @param title - the title on the top
    * @param node - the start node
    * @param level - the indentation level
    * @param pw - the stream on which to display the information
    */
   public static void displayNodes(String title, Node node, int level, PrintStream pw) {
      PrintStream out = pw;
      if (null == out) {
         out = System.out;
      }
      if (null != title) {
         out.println("<!-- **** " + title + " **** -->");
      }


      if ("unknown".equals(node.getNodeName())) {
         if (TAGMARKER_APP1.equals(((IIOMetadataNode) node).getAttribute("MarkerTag"))) {
            IIOMetadata exifMeta = getEXIFMetadata((IIOMetadataNode) node);
            if (null != exifMeta) {
               IIOMetadataNode exifNode = (IIOMetadataNode) exifMeta.getAsTree(exactitude.core.Constants.TIFF_FORMAT);
               displayNodes("EXIF", exifNode, level, out);
            }
         } else if (TAGMARKER_APP13.equals(((IIOMetadataNode) node).getAttribute("MarkerTag"))) {
            IIOMetadata iptcMeta = getIPTCMetadata((IIOMetadataNode) node);
            if (null != iptcMeta) {
               IIOMetadataNode iptcNode = (IIOMetadataNode) iptcMeta.getAsTree(exactitude.core.Constants.TIFF_FORMAT);
               displayNodes("IPTC", iptcNode, level, out);
            }
         }
      } else {
         indent(level, out); // emit open tag
         out.print("<" + node.getNodeName());
         NamedNodeMap map = node.getAttributes();
         if (map != null) { // print attribute values
            int length = map.getLength();
            for (int i = 0; i < length; i++) {
               Node attr = map.item(i);
               out.print(" " + attr.getNodeName() +   "=\"" + attr.getNodeValue() + "\"");
            }
         }
      }

      Node child = node.getFirstChild();
      if (child != null) {
         out.println(">"); // close current tag
         while (child != null) { // emit child tags recursively
            displayNodes(null, child, level + 1, out);
            child = child.getNextSibling();
         }
         indent(level, out); // emit close tag
         out.println("</" + node.getNodeName() + ">");
      } else {
         out.println("/>");
      }
   }

   /**
    * Display nodes on stdout - convenience method
    * @param title - the title
    * @param node - the startng node
    */
   public static void displayNodes(String title, Node node) {
      displayNodes(title, node, 0, null);
   }

   /**
    * Display nodes - convenience method
    * @param title - the title
    * @param node - the startng node
    * @param out - the output stream
    */
   public static void displayNodes(String title, Node node, PrintStream out) {
      displayNodes(title, node, 0, out);
   }

   /**
    * Display Metadata
    * @param title - the title
    * @param metadata - the data to be displayed
    * @param out - the output stream
    */
   public static void displayMetadata(String title, IIOMetadata metadata, PrintStream out) {
      String[] formatNames = metadata.getMetadataFormatNames();
      if (null != formatNames) {
         for (String fname : formatNames) {
            displayNodes(title + " (" + fname + ")", metadata.getAsTree(fname), 0, out);
         }
      }

      formatNames = metadata.getExtraMetadataFormatNames();
      if (null != formatNames) {
         for (String fname : formatNames) {
            displayNodes(title + " (" + fname + ")", metadata.getAsTree(fname), 0, out);
         }
      }
   }

   /**
    * Display Metadata on stdout
    * @param title - the title
    * @param metadata - the data to be displayed
    */
   public static void displayMetadata(String title, IIOMetadata metadata) {
      displayMetadata(title, metadata, null);
   }


   /**
    * Create a new node in the specified document
    *
    * @param doc - the document in which to create the node
    * @param node - the node to add to the document
    * @return The combined node
    */
   public static Node makeNode(Document doc, Node node) {
      Element retVal = doc.createElement(node.getNodeName());
      NamedNodeMap map = node.getAttributes();
      if (map != null) {
         int length = map.getLength();
         for (int iX = 0; iX<length; iX++) {
            Node attr = map.item(iX);
            retVal.setAttribute(attr.getNodeName(), attr.getNodeValue());
         }
      }
      NodeList children = node.getChildNodes();
      int length = children.getLength();
      for (int iX=0; iX<length; iX++) {
         retVal.appendChild(makeNode(doc, children.item(iX)));
      }
      return retVal;
   }

   /**
    * This generates an EXIF node that can be appended to a JPEG image
    * 
    * @param iReader
    * @param imageMetadata
    * @param image
    * @return An appropriately-created IPTC node
    */
   public static IIOMetadataNode createExifMetadataNode(ImageReader iReader, IIOMetadata imageMetadata, BufferedImage image) {

      IIOMetadataNode iptcNode = null;
      ImageWriter     iWriter  = ImageIOConverter.findWriter("tif", "com.sun.media");

      if (null != iWriter) {
         try {
            // Force the correct compression ON
            ImageWriteParam iWriteParam = iWriter.getDefaultWriteParam();
            iWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iWriteParam.setCompressionType("EXIF JPEG");

            // Force output to a memory cache stream
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            MemoryCacheImageOutputStream exifStream = new MemoryCacheImageOutputStream(oStream);
            iWriter.setOutput(exifStream);

            // Create the in-memory metadata object
            iWriter.prepareWriteEmpty(iReader.getStreamMetadata(), new ImageTypeSpecifier(image),
                  image.getWidth(), image.getHeight(), imageMetadata,
                  null, iWriteParam);
            iWriter.endWriteEmpty();
            exifStream.flush();

            // Create the APP1 object content, and set the ID
            byte[] iptcParams = new byte[exactitude.core.Constants.METAHeaderLenEXIF + oStream.size()];
            iptcParams[0] = (byte)'E';
            iptcParams[1] = (byte)'x';
            iptcParams[2] = (byte)'i';
            iptcParams[3] = (byte)'f';
            iptcParams[4] = (byte)0;
            iptcParams[5] = (byte)0;

            // Copy the in-memory copy into the APP1 object (after the 6-byte ID) 
            System.arraycopy(oStream.toByteArray(), 0, iptcParams, exactitude.core.Constants.METAHeaderLenEXIF, oStream.size());

            // Create the marker and unknown tags
            iptcNode = new IIOMetadataNode("unknown");
            iptcNode.setAttribute("MarkerTag", TAGMARKER_APP13);

            // Append the object
            iptcNode.setUserObject(iptcParams);      

         } catch (IOException e){
            e.printStackTrace();
         } finally {
            iWriter.dispose();
         }
      }

      return iptcNode;
   }

   /**
    * Uses a <code>TIFFImageReader</code> plugin to parse the given exif data into tiff
    * tags. The returned <code>IIOMetadata</code> is in whatever format the tiff ImageIO
    * plugin uses. If there is no tiff plugin, then this method returns null.
    *
    * @param exifNode - the metadata tree from which to extract the EXIF data
    * @return - The <code>IIOMetadata</code> object containing the encoded EXIF data
    */
   public static IIOMetadata getEXIFMetadata(IIOMetadataNode exifNode) {
      ImageReader iReader = ImageIOConverter.findReader("tif", "com.sun.media");;
      IIOMetadata exifMetadata = null;
      if (null != iReader) {
         byte[] exif = (byte[]) exifNode.getUserObject();
         // Check that the structure is actually an EXIT block
         byte[] ID = ID_EXIF.getBytes();
         int iX;
         for (iX=0; iX<ID.length; iX++) {
            if (exif[iX] != ID[iX])
               break;
         }
         if (iX<ID.length) {
            return null;
         }

         // Read from memory, but skip the 6 byte "Exif\0\0" header
         ImageInputStream wrapper = new MemoryCacheImageInputStream(
               new java.io.ByteArrayInputStream(exif, exactitude.core.Constants.METAHeaderLenEXIF,
                     exif.length - exactitude.core.Constants.METAHeaderLenEXIF));
         iReader.setInput(wrapper, true, false);

         try {
            exifMetadata = iReader.getImageMetadata(0);
         } catch (Exception e) {
            // shouldn't happen
            throw new Error(e);
         }
         iReader.dispose();
      }
      return exifMetadata;
   }

   /**
    * This generates an IPTC node that can be appended to a JPEG image
    * 
    * @param iReader
    * @param imageMetadata
    * @param image
    * @return An appropriately-created IPTC metadata node
    */
   public static IIOMetadataNode createIptcMetadataNode(ImageReader iReader, IIOMetadata imageMetadata, BufferedImage image) {

      IIOMetadataNode exifNode   = null;
      ImageWriter     iWriter = ImageIOConverter.findWriter("tif", "com.sun.media");

      if (null != iWriter) {
         try {
            // Force the correct compression ON
            ImageWriteParam iWriteParam = iWriter.getDefaultWriteParam();
            iWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iWriteParam.setCompressionType("EXIF JPEG");

            // Force output to a memory cache stream
            ByteArrayOutputStream oStream = new ByteArrayOutputStream();
            MemoryCacheImageOutputStream iptcStream = new MemoryCacheImageOutputStream(oStream);
            iWriter.setOutput(iptcStream);

            // Create the in-memory metadata object
            iWriter.prepareWriteEmpty(iReader.getStreamMetadata(), new ImageTypeSpecifier(image),
                  image.getWidth(), image.getHeight(), imageMetadata,
                  null, iWriteParam);
            iWriter.endWriteEmpty();
            iptcStream.flush();

            // Create the APP1 object content, and set the ID
            byte[] exifParams = new byte[6 + oStream.size()];
            exifParams[0] = (byte)'E';
            exifParams[1] = (byte)'x';
            exifParams[2] = (byte)'i';
            exifParams[3] = (byte)'f';
            exifParams[4] = (byte)0;
            exifParams[5] = (byte)0;

            // Copy the in-memory copy into the APP1 object (after the 6-byte ID) 
            System.arraycopy(oStream.toByteArray(), 0, exifParams, exactitude.core.Constants.METAHeaderLenEXIF, oStream.size());

            // Create the marker and unknown tags
            exifNode = new IIOMetadataNode("unknown");
            exifNode.setAttribute("MarkerTag", TAGMARKER_APP1);

            // Append the object
            exifNode.setUserObject(exifParams);      

         } catch (IOException e){
            e.printStackTrace();
         } finally {
            iWriter.dispose();
         }
      }

      return exifNode;
   }

   /**
    * Uses a <code>TIFFImageReader</code> plugin to parse the given iptc data into tiff
    * tags. The returned <code>IIOMetadata</code> is in whatever format the tiff ImageIO
    * plugin uses. If there is no tiff plugin, then this method returns null.
    *
    * @param iptcNode - the metadata tree from which to extract the IPTC data
    * @return - The <code>IIOMetadata</code> object containing the encoded IPTC data
    */
   public static IIOMetadata getIPTCMetadata(IIOMetadataNode iptcNode) {
      ImageReader iReader = ImageIOConverter.findReader("tif", "com.sun.media");;
      IIOMetadata iptcMetadata = null;
      if (null != iReader) {
         byte[] iptc = (byte[]) iptcNode.getUserObject();

         // Read from memory, but skip the 6 byte "Exif\0\0" header
         ImageInputStream wrapper = new MemoryCacheImageInputStream(
               new java.io.ByteArrayInputStream(iptc, exactitude.core.Constants.METAHeaderLenEXIF,
                     iptc.length - exactitude.core.Constants.METAHeaderLenEXIF));
         iReader.setInput(wrapper, true, false);

         try {
            iptcMetadata = iReader.getImageMetadata(0);
         } catch (Exception e) {
            // shouldn't happen
            throw new Error(e);
         }
         iReader.dispose();
      }
      return iptcMetadata;
   }

}
