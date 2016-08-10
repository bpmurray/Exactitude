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
package exactitude;

/**
 * This class is a convenience class to load and execute the <bold>Exactitude</bold>
 * library. On completion, it prints the averaged comparison value on <code>stdout</code>.
 * 
 * The permitted parameters are:
 * <ul>
 * <li>-i inputFileName</li>
 * The name of the source file.
 * <li>-f fromType</li>
 * The source file type, e.g. jpg, tif, etc.
 * <li>-o outputFileName</li>
 * The name of the output file. If it already exists, it will be overwritten.
 * <li>-t toType</li>
 * The target file type, the same format as <code>fromType</code> above.
 * <li>-l logFileName</li>
 * The name of the file in which to log events.
 * <li>-c</li>
 * If this is specified, the system simply compares the files and does not
 * try to convert. This means that it can be used for comparisons of existing
 * files.
 * <li>-r</li>
 * This indicates that the comparisons are to return the raw values rather than
 * the averaged value. This can assist in determining the best possible method
 * to use in the comparison.
 * <li>-s</li>
 * Run the comparisons in "smart mode"
 * </ul>
 * 
 * @author Brendan Murray
 *
 */

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import exactitude.core.Exactitude;

public class ExactitudeRun {
	private static final String USAGE = "Usage: Convert -i inputFileName [-f fromType] -o outputFileName [-t toType] [-l logFileName] [-c] [-r] [-s]";

	private static String  infileName = null;
	private static String  outfileName = null;
	private static String  fromFormat = null;
	private static String  toFormat = null;
	private static boolean compareOnly = false;
	private static boolean doRaw = false;
	private static boolean doSmart = false;


	private static void usage(Document doc) {
		System.out.println(USAGE);
		if (doc != null) {
			System.out.println("   where the types can be:");
			Element root = doc.getDocumentElement();
			NodeList converters = root.getElementsByTagName("converter");
			int count = converters.getLength();
			for (int iX = 0; iX < count; iX++) {
				NodeList nodes = converters.item(iX).getChildNodes();
				int length = nodes.getLength();
				String name = null;
				String shortname = null;
				for (int iY = 0; iY < length; iY++) {
					Node curNode = nodes.item(iY);
					if (curNode.getNodeType() == Node.ELEMENT_NODE) {
						if ("name".equals(curNode.getNodeName())) {
							name = curNode.getFirstChild().getNodeValue();
						} else if ("shortname".equals(curNode.getNodeName())) {
							shortname = curNode.getFirstChild().getNodeValue();
						}
					}
				}
				System.out.println("      " + shortname + " - " + name);
			}
		}
	}

	/*
	 * Name: validate - validate the program arguments
	 * 
	 * @param args
	 *            - the program arguments
	 * @return true if the arguments are successfully validated, otherwise false
	 */
	private static boolean validate(String[] args) {
		for (int iX = 0; iX < args.length; iX++) {
			// Need separate flags & arg
			// TODO: make this more flexible
			if (args[iX].length() > 2) {
				return false;
			}
			if (args[iX].length() > 1 && args[iX].charAt(0) == '-') {
				char flag = args[iX].charAt(1);
				switch (flag) {
				case 'i':
					infileName = args[++iX];
					break;
				case 'o':
					outfileName = args[++iX];
					break;
				case 'f':
					fromFormat = args[++iX];
					break;
				case 't':
					toFormat = args[++iX];
					break;
				case 'c':
					compareOnly = true;
					break;
				case 'r':
					doRaw = true;
					break;
				case 's':
					doSmart = true;
					break;
				default:
					System.out.println("Unknown flag " + args[iX]);
					return false;
				}
			} else {
				return false;
			}
		}

		// Default input type = input file's extension
		if (null == fromFormat && null != infileName) {
			fromFormat = (infileName.lastIndexOf(".") == -1) ? "" :
				         infileName.substring(infileName.lastIndexOf(".") + 1,
						 infileName.length());
		}

		// Default output type = output file's extension
		if (null == toFormat && null != outfileName) {
			toFormat = (outfileName.lastIndexOf(".") == -1) ? "" :
				       outfileName.substring(outfileName.lastIndexOf(".") + 1,
					   outfileName.length());
		}

		
		return (infileName != null && outfileName != null &&
				fromFormat != null && toFormat != null);
	}



	/*
	 * Main method - see above for permitted args.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (validate(args) == false) {
			usage(null);
			return;
		}
		if (doRaw) {
			double err[] = (new Exactitude()).processRaw(infileName, fromFormat, outfileName, toFormat, compareOnly, doSmart);
			for (double val: err) {
				System.out.print("\t" + val);
			}
			System.out.println();
		} else {
		double err = (new Exactitude()).process(infileName, fromFormat, outfileName, toFormat, compareOnly, doSmart);
		System.out.println(err);
		}
	}
}
