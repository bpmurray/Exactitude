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

package exactitude.core.converter;

import java.io.IOException;

/**
 * @author Brendan Murray
 * 
 * This abstract class describes the interface for the converter types
 */
public abstract class IConverter {
   private String fromType;
   private String toType;
   private IMetadataConverter metaConverter;

   /**
    * Return the source format
    * 
    * @return the fromType
    */
   public String getFromType() {
      return fromType;
   }

   /**
    * Set the source file type
    * 
    * @param fromType - the fromType to set
    */
   public void setFromType(String fromType) {
      this.fromType = fromType;
   }

   /**
    * Return the target format
    * 
    * @return the toType
    */
   public String getToType() {
      return toType;
   }

   /**
    * Set the target format
    * @param toType - the toType to set
    */
   public void setToType(String toType) {
      this.toType = toType;
   }

   /**
    * Retrieve the format-specific metadata converter
    * 
    * @return the metadataConverter
    */
   public IMetadataConverter getMetaConverter() {
      return metaConverter;
   }

   /**
    * Set the metadataConverter object.
    * 
    * @param metaConverter the metaConverter to set
    */
   public void setMetaConverter(IMetadataConverter metaConverter) {
      this.metaConverter = metaConverter;
   }

   /**
    * Convert a file from one type to another
    * 
    * @param inName - The name of the input file
    * @param inType - The input file type
    * @param outName - The output file name
    * @param outType - The output file type
    */
   abstract public void convert(String inName, String inType, String outName, String outType)  throws IOException;

   /**
    * Convert a file, using the extensions as the file type
    * 
    * @param inName - Input file name
    * @param outName - Output file's name
    */
   public void convert(String inName, String outName) throws IOException {
      String inType  = (inName.lastIndexOf(".") == -1) ? "" : inName.substring(
            inName.lastIndexOf(".") + 1, inName.length());
      String outType = (outName.lastIndexOf(".") == -1) ? "" : outName.substring(
            outName.lastIndexOf(".") + 1, outName.length());
      convert(inName, inType, outName, outType);
   }




}
