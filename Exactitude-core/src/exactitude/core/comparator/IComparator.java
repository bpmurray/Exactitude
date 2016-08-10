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
package exactitude.core.comparator;

import java.io.FileNotFoundException;


/**
 * @author Brendan Murray
 *
 * This abstract class describes the interface of the comparison objects.
 */
public abstract class IComparator {
   private String format;

   /**
    * @return the format
    */
   public String getFormat() {
      return format;
   }

   /**
    * Set the format
    * 
    * @param format - the format to be set
    */
   public void setFormat(String format) {
      this.format = format;
   }

   /**
    * This is the abstract method that is implemented by all concrete objects. This compares the files
    * and returns a value between zero and one to indicate how close the files are to each other. A low
    * value indicates a closer match.
    *  
    * @param fileName1 - The name of the source file
    * @param fileName2 - The name of the generated output file
    * @return A value indicating how close the two files are
    * @throws FileNotFoundException
    */
   abstract public double compareFiles(String fileName1, String fileName2) throws FileNotFoundException;
   

   /**
    * @return The threshold at which the comparison can be said to have failed.
    */
   public double getThreshold() {
      return 1.0;
   }


}
