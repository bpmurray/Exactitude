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

package exactitude.core.util;

import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Brendan Murray
 * 
 * This class handles logging
 */
public class LogUtil {

   // Used as global variables to persist across calls
   private static Logger      logger  = null;
   private static Level       logging = Level.SEVERE;


   /**
    * Check if logging is active at the specified level.
    * 
    * @param level
    * @return True if the current level is higher or equal to what is passed in
    */
   public static boolean isLogging(String level) {
      return logging.intValue() >= Level.parse(level).intValue();
   }
   public static boolean isLogging(Level level) {
      return logging.intValue() >= level.intValue();
   }


   /**
    * This is to retrieve the global logging object
    * 
    * @return the logger object
    */
   public static synchronized Logger getLogger() {
      if (null == logger) {
         logger = Logger.getLogger(exactitude.core.Constants.LOGGER_CLASS);
         try {
            Handler fh = new FileHandler(exactitude.core.Constants.DEFAULT_LOGFILE);
            logger.addHandler(fh);
         } catch (Exception ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            ex.printStackTrace();
         }
      }
      return logger;
   }

   /**
    * @param level the logging level to use
    */
   public static void setLogging(Level level) {
      logging = level;
   }

}
