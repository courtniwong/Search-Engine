import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Create a LinkedHashMap of queries and writes them into an output file.
 *
 * @author courtniwong
 *
 */
public class PartialSearchBuilder {

	private final LinkedHashMap<String, List<SearchResult>> queryResults;

	public PartialSearchBuilder() {
		queryResults = new LinkedHashMap<String, List<SearchResult>>();
	}

	/**
	 * Read in queries and create results for queries
	 *
	 * @param queryFile:
	 *            input queries
	 * @param index
	 *
	 * @see #parseLine(String, InvertedIndex)
	 */
	public void buildSearchResults(Path queryFile, InvertedIndex index) {
		try (BufferedReader reader = Files.newBufferedReader(queryFile, Charset.forName("UTF-8"))) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				parseLine(line, index);
			}
		} catch (IOException e) {
			System.err.println("Error with file, " + queryFile);
		}
	}

	/**
	 * Helper method to create search results and store them in the
	 * LinkedHashMap.
	 *
	 * @param line:
	 *            query
	 * @param index:
	 *            Inverted Index
	 */
	public void parseLine(String line, InvertedIndex index) {
		List<String> cleanQueries = WordParser.split(line);
		List<SearchResult> searchResults = index.partialSearch(cleanQueries);
		queryResults.put(line, searchResults);
	}

	/**
	 * Calls writeQueries method in OutputFileWriter to write queries in proper
	 * format.
	 *
	 * @param queryFile:
	 *            input queries
	 */
	public void writeQueryFile(Path queryFile) {
		OutputFileWriter.writeQuery(queryFile, queryResults);
	}
}
