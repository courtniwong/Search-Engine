import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * All methods that writes to files.
 *
 * @author courtniwong
 *
 */
public class OutputFileWriter {

	/**
	 * Helper method to indent several times by 2 spaces each time. For example,
	 * indent(0) will return an empty string, indent(1) will return 2 spaces,
	 * and indent(2) will return 4 spaces.
	 *
	 * <p>
	 * <em>Using this method is optional!</em>
	 * </p>
	 *
	 * @param times
	 * @return
	 * @throws IOException
	 */
	public static String indent(int times) {
		return times > 0 ? String.format("%" + times * 2 + "s", " ") : "";
	}

	/**
	 * Helper method to quote text for output. This requires escaping the
	 * quotation mark " as \" for use in Strings. For example:
	 *
	 * <pre>
	 * String text = "hello world";
	 * System.out.println(text); // output: hello world
	 * System.out.println(quote(text)); // output: "hello world"
	 * </pre>
	 *
	 * @param text
	 *            input to surround with quotation marks
	 * @return quoted text
	 */
	public static String quote(String text) {
		return "\"" + text + "\"";
	}

	/**
	 * Writes index to output file in proper format.
	 *
	 * @param output:
	 *            inverted index output file
	 * @param elements:
	 *            words stored index
	 * @throws IOException:
	 *             prints error statement
	 *
	 * @see #writeIndexWords(Entry, BufferedWriter, Indentation Level)
	 */
	public static void writeInvertedIndex(Path output, TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements) {
		try (BufferedWriter writer = Files.newBufferedWriter(output, Charset.forName("UTF-8"))) {
			writer.write("{");
			if (!elements.isEmpty()) {
				Entry<String, TreeMap<String, TreeSet<Integer>>> first = elements.firstEntry();
				writeIndexWords(first, writer, 1);
				for (Entry<String, TreeMap<String, TreeSet<Integer>>> entry : elements.tailMap(first.getKey(), false)
						.entrySet()) {
					writer.write(",");
					writeIndexWords(entry, writer, 1);
				}
			}
			writer.newLine();
			writer.write("}");
			writer.newLine();
		} catch (IOException e) {
			System.err.println("Error when writing to output file, " + output);
		}
	}

	/**
	 * Helper method to write the words in the index.
	 *
	 * @param entry:
	 *            word in index and its nested attributes
	 * @param writer:
	 *            writer use to write to file
	 * @param level:
	 *            indentation level
	 * @throws IOException
	 *
	 * @see #writeIndexPaths(Entry, BufferedWriter, Indentation Level)
	 */
	public static void writeIndexWords(Entry<String, TreeMap<String, TreeSet<Integer>>> entry, BufferedWriter writer,
			int level) throws IOException {
		TreeMap<String, TreeSet<Integer>> files = entry.getValue();
		writer.newLine();
		writer.write(indent(level));
		writer.write(quote(entry.getKey()));
		writer.write(": {");
		if (!files.isEmpty()) {
			Entry<String, TreeSet<Integer>> first = files.firstEntry();
			writeIndexPaths(first, writer, level + 1);
			for (Entry<String, TreeSet<Integer>> file : files.tailMap(first.getKey(), false).entrySet()) {
				writer.write(",");
				writeIndexPaths(file, writer, level + 1);
			}
		}
		writer.newLine();
		writer.write(indent(level) + "}");
	}

	/**
	 * Helper method to write file name of the word.
	 *
	 * @param textfiles:
	 *            paths of word in index
	 * @param writer:
	 *            writer used to write to file
	 * @param level:
	 *            indentation level
	 * @throws IOException
	 *
	 * @see #writeIndexPositions(TreeSet, BufferedWriter, Indentation Level)
	 */
	public static void writeIndexPaths(Entry<String, TreeSet<Integer>> textfiles, BufferedWriter writer, int level)
			throws IOException {
		if (!textfiles.getValue().isEmpty()) {
			TreeSet<Integer> locations = textfiles.getValue();
			writer.newLine();
			writer.write(indent(level));
			writer.write(quote(textfiles.getKey()));
			writer.write(": [");
			writeIndexPositions(locations, writer, level + 1);
		}
		writer.newLine();
		writer.write(indent(level - 1) + "]");
	}

	/**
	 * Helper method to write positions of the word.
	 *
	 * @param positions
	 *            set of indexes where word was found in file
	 * @param writer
	 *            writer used to write to file
	 * @param level
	 *            indentation level
	 * @throws IOException
	 */
	public static void writeIndexPositions(TreeSet<Integer> positions, BufferedWriter writer, int level)
			throws IOException {
		if (!positions.isEmpty()) {
			writer.newLine();
			writer.write(indent(level) + positions.first());
			for (Integer item : positions.tailSet(positions.first(), false)) {
				writer.write(",");
				writer.newLine();
				writer.write(indent(level) + item);
			}
		}
	}

	/**
	 * Write queries in proper format.
	 *
	 * @param queryOutput:
	 *            query output file
	 * @param queryResults:
	 *            LinkedHashMap of query and it's results
	 *
	 * @see #writeQueryWord(LinkedHashMap, String, BufferedWriter, Indentation
	 *      Level)
	 */
	public static void writeQuery(Path queryOutput, LinkedHashMap<String, List<SearchResult>> queryResults) {
		try (BufferedWriter writer = Files.newBufferedWriter(queryOutput, Charset.forName("UTF-8"))) {
			writer.write("{");
			writer.newLine();
			if (!queryResults.keySet().isEmpty()) {
				Iterator<String> key = queryResults.keySet().iterator();
				String firstKey = key.next();
				writeQueryWord(queryResults, firstKey, writer, 1);

				while (key.hasNext()) {
					String nextKey = key.next();
					writer.write(",");
					writer.newLine();
					writeQueryWord(queryResults, nextKey, writer, 1);
				}
			}
			writer.newLine();
			writer.write("}");
		} catch (IOException e) {
			System.err.println("Error writing to output file, " + queryOutput);
		}
	}

	/**
	 * Helper method to write query key in proper format.
	 *
	 * @param queryResults:
	 *            LinkedHashMap of query and it's reults
	 * @param key:
	 *            query
	 * @param writer:
	 *            writer used to write to file
	 * @param level:
	 *            indentation level
	 * @throws IOException
	 *
	 * @see #writeQueryResults(List, BufferedWriter, Indentation Level)
	 */
	public static void writeQueryWord(LinkedHashMap<String, List<SearchResult>> queryResults, String key,
			BufferedWriter writer, int level) throws IOException {
		writer.write(indent(1) + quote(key) + ": [");
		List<SearchResult> nextSearchResults = queryResults.get(key);
		writeQueryResults(nextSearchResults, writer, 1);
		writer.newLine();
		writer.write(indent(1) + "]");
	}

	/**
	 * Write query results in proper format.
	 *
	 * @param searchResults:
	 *            list of query results
	 * @param writer:
	 *            writer used to write to file
	 * @param level:
	 *            indentation level
	 * @throws IOException
	 *
	 * @see #writeQueryResultsHelper(SearchResult, BufferedWriter, Indentation
	 *      Level)
	 */
	public static void writeQueryResults(List<SearchResult> searchResults, BufferedWriter writer, int level)
			throws IOException {
		if (!searchResults.isEmpty()) {
			Iterator<SearchResult> result = searchResults.iterator();
			SearchResult firstResult = result.next();
			writeQueryResultsWriter(firstResult, writer, level);

			while (result.hasNext()) {
				SearchResult nextResult = result.next();
				writer.write(",");
				writeQueryResultsWriter(nextResult, writer, level);
			}
		}
	}

	/**
	 * Helper method to write query results in proper format.
	 *
	 * @param result:
	 *            query result
	 * @param writer:
	 *            writer used to write to file
	 * @param level:
	 *            indentation level
	 * @throws IOException
	 */
	public static void writeQueryResultsWriter(SearchResult result, BufferedWriter writer, int level)
			throws IOException {
		writer.newLine();
		writer.write(indent(level + 1) + "{\n");
		writer.write(indent(level + 2) + quote("where") + ": " + quote(result.getPath()) + ",\n");
		writer.write(indent(level + 2) + quote("count") + ": " + result.getFrequency() + ",\n");
		writer.write(indent(level + 2) + quote("index") + ": " + result.getPosition() + "\n");
		writer.write(indent(level + 1) + "}");
	}
}
