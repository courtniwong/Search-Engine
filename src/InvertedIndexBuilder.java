import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Build index with words found in given files and their attributes.
 *
 * @author courtniwong
 *
 */
public class InvertedIndexBuilder {
	/**
	 * Stores all text files from a directory and adds all words from text files
	 * into the index.
	 *
	 * @param input:
	 *            input directory
	 * @param index:
	 *            Inverted Index
	 */
	public static void addMap(Path input, InvertedIndex index) {
		parseDirectory(input, index);
	}

	/**
	 * Create arraylist of textfiles from directory and parse files.
	 *
	 * @param input
	 *            Input directory to search for text files
	 * @param index
	 *            Inverted index to store parsed words
	 *
	 * @see #parseFile(String, InvertedIndex)
	 */
	public static void parseDirectory(Path input, InvertedIndex index) {
		if (Files.isDirectory(input)) {
			ArrayList<String> textFiles = DirectoryTraverser.traverse(input);
			for (String file : textFiles) {
				parseFile(file, index);
			}
		}
	}

	/**
	 * Parse file into words and add words, file name, and position to index.
	 *
	 * @param input:
	 *            Input directory
	 * @param index:
	 *            Inverted Index
	 */
	public static void parseFile(String input, InvertedIndex index) {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(input.toString()), Charset.forName("UTF-8"))) {
			String line = null;
			Integer position = 0;
			while ((line = reader.readLine()) != null) {
				List<String> newLine = WordParser.split(line);
				for (String word : newLine) {
					position++;
					index.add(word, input.toString(), position);
				}
			}
		} catch (IOException e) {
			System.err.println("There is an error with file, " + input);
		}
	}
}
