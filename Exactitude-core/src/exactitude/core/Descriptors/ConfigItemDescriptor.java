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
package exactitude.core.Descriptors;

/**
 * @author Brendan Murray
 *
 * This class handles the management of the configuration file's data
 */
public class ConfigItemDescriptor {

   String longName;       // The full formal name
   String shortNameFrom;  // Abbreviated name of source
   String shortNameTo;    // Abbreviated name of target
   String description;    // Text description
   String formatType;     // The generic file type   
   String className;      // Class name for instantiation
   String metaClassName;  // Class name for instantiation of metadata converter


   /**
    * Constructor - creates an object from the XML data
    * 
    * @param longName - the format's long name
    * @param shortNameFrom - the source format's abbreviated name
    * @param shortNameTo - the target format's abbreviated name
    * @param description - a description of the format
    * @param formatType - the generic type of the format
    * @param className - the name of the class that manages the format
    * @param metaClassName - the class that manages the format
    */
   public ConfigItemDescriptor(String longName, String shortNameFrom, String shortNameTo, String description,
         String formatType, String className, String metaClassName) {
      this.longName = longName;
      this.shortNameFrom = shortNameFrom;
      this.shortNameTo = shortNameTo;
      this.description = description;
      this.formatType = formatType;
      this.className = className;
      this.metaClassName = metaClassName;         
   }

   /**
    * @return the longName
    */
   public String getLongName() {
      return longName;
   }


   /**
    * @return the shortNameFrom
    */
   public String getShortNameFrom() {
      return shortNameFrom;
   }

   /**
    * @return the shortNameTo
    */
   public String getShortNameTo() {
      return shortNameTo;
   }

   /**
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   /**
    * @return the formatType
    */
   public String getFormatType() {
      return formatType;
   }

   /**
    * @return the className
    */
   public String getClassName() {
      return className;
   }

   /**
    * @return the metaClassName
    */
   public String getMetaClassName() {
      return metaClassName;
   }


}
