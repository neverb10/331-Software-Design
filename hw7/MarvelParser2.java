package hw7;

import java.io.*;
import java.util.*;

/**
 * Parser utility to load a weighted dataset.
 */
public class MarvelParser2 {
	/**
	 * A checked exception class for bad data files
	 */
	@SuppressWarnings("serial")
	public static class MalformedDataException extends Exception {
		public MalformedDataException() {
		}

		public MalformedDataException(String message) {
			super(message);
		}

		public MalformedDataException(Throwable cause) {
			super(cause);
		}

		public MalformedDataException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	/**
	 * Reads a dataset. Each line of the input file contains a
	 * node and a label linking other nodes, that may be characters or anything else
	 * 
	 * @requires filename is a valid file path
	 * @param filename
	 *            the file that will be read
	 * @param sharedBooks
	 *            map from characters to characters in shared books;
	 *            typically empty when the routine is called
	 * @modifies characters, books
	 * @effects fills characters with a list of all unique character names
	 * @effects fills books with a map from each comic book to all characters
	 *          appearing in it
	 * @throws MalformedDataException
	 *             if the file is not well-formed: each line contains exactly two
	 *             tokens separated by a tab, or else starting with a # symbol to
	 *             indicate a comment line.
	 */
	
	//shared books is chars to chars with weights on chars in value
	public static void parseData(String filename, Map<String, HashMap<String, Integer>> sharedBooks)
			throws MalformedDataException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));

			//book to chars
			Map<String, List<String>> bookToChars = new HashMap<String, List<String>>();

			String inputLine;
			while ((inputLine = reader.readLine()) != null) {

				// Ignore comment lines.
				if (inputLine.startsWith("#")) {
					continue;
				}

				// Parse the data, stripping out quotation marks and throwing
				// an exception for malformed lines.
				inputLine = inputLine.replace("\"", "");
				String[] tokens = inputLine.split("\t");
				if (tokens.length != 2) {
					throw new MalformedDataException("Line should contain exactly one tab: " + inputLine);
				}

				//should i parse this shit beforehand?
				String character = tokens[0]; //.replace('_', ' ');
				String book = tokens[1];
				//make sure the character is in the map of characters to other characters
				if (!sharedBooks.containsKey(character)) {
					sharedBooks.put(character, new HashMap<String, Integer>());
				}
				// characters in a book, map. if it does not contain a book, add it in
				List<String> chars;
				if (bookToChars.containsKey(book)) {
					// all the characters in the same book
					chars = bookToChars.get(book);
					// map of characters and counts that correspond to that character
					Map<String, Integer> edgeCount = sharedBooks.get(character);
					//check all the characters in the book, and whether the outgoing edges from
					//the character being checked have all the characters in the book
					for (String s : chars) {
						if (edgeCount.containsKey(s)) {
							//put overrides k/v pairs right?
							edgeCount.put(s, 1 + edgeCount.get(s));
							//characters that share books of the outgoing character being checked
							Map<String, Integer> outerEdgeCount = sharedBooks.get(s);
							//if it has the character in question, add 1 to its int in the value
							if (outerEdgeCount.containsKey(character)) {
								outerEdgeCount.put(character, 1 + outerEdgeCount.get(character));
								//otherwise put the character in with one
							} else {
								outerEdgeCount.put(character, 1);
							}
						//characters/counts do not have that character, add it in since they share a book
						} else {
							edgeCount.put(s, 1);
						}
					}
					// pair the character with the book to show it is in it
					chars.add(character);
					bookToChars.put(book, chars);
				//the book is not in our collection of books to characters, we need to add it
				} else {
					chars = new ArrayList<String>();
					chars.add(character);
					bookToChars.put(book, chars);
				}
			}
		} catch (IOException e) {
			System.err.println(e.toString());
			e.printStackTrace(System.err);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println(e.toString());
					e.printStackTrace(System.err);
				}
			}
		}
	}

}
