import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * InvertedIndex stores words and it's attributes into an index from input
 * files, writes index to output files, provides methods to check if certain
 * attributes exit within the index, and creates search results for queries.
 *
 * @author courtniwong
 *
 */
public class InvertedIndex {

	/**
	 * Stores a word to a map of paths to a set of locations.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;

	public InvertedIndex() {
		index = new TreeMap<>();
	}

	/**
	 * Adds words into index.
	 *
	 * @param word
	 *            word in file
	 * @param path
	 *            file word is found in
	 * @param position
	 *            index of word
	 */
	public void add(String word, String path, Integer position) {
		if (!hasWord(word)) {
			index.put(word, new TreeMap<String, TreeSet<Integer>>());
		}
		if (!hasPath(word, path)) {
			index.get(word).put(path, new TreeSet<Integer>());
		}
		index.get(word).get(path).add(position);

	}

	/**
	 * Calls writeNestedObject method in OutputFileWriter class that writes the
	 * inverted index in proper format.
	 *
	 * @param file
	 *            output file
	 */
	public void writeJSON(Path file) {
		OutputFileWriter.writeInvertedIndex(file, index);
	}

	/**
	 * Returns true if the index contains the word. False if it does not.
	 *
	 * @param word
	 *            word found in file
	 * @return
	 */
	public boolean hasWord(String word) {
		return index.containsKey(word);

	}

	/**
	 * Returns true if the index contains the path for the given word. False if
	 * it does not.
	 *
	 * @param word
	 *            word in index
	 * @param path
	 *            path the word might be found in
	 * @return
	 */
	public boolean hasPath(String word, String path) {
		if (hasWord(word)) {
			return index.get(word).containsKey(path);
		}
		return false;
	}

	/**
	 * Return true if the index contains the position for the given word and
	 * path. False if it does not.
	 *
	 * @param word
	 *            word in index
	 * @param path
	 *            path the word is found in
	 * @param position
	 *            possible index of word
	 * @return
	 *
	 * @see #hasWord(String)
	 * @see #hasPath(String, String)
	 */
	public boolean hasPosition(String word, String path, int position) {
		if (hasWord(word) && hasPath(word, path)) {
			return index.get(word).get(path).contains(position);
		}
		return false;
	}

	/**
	 * Create SearchResult objects for each query.
	 *
	 * @param cleanQueries:
	 *            list of normalized queries
	 * @return List of SearchResult objects
	 */
	public List<SearchResult> partialSearch(List<String> cleanQueries) {
		HashMap<String, SearchResult> searchResultMap = new HashMap<>();
		List<SearchResult> searchResults = new ArrayList<>();

		for (String query : cleanQueries) {
			for (String word : index.tailMap(query).keySet()) {
				if (word.startsWith(query)) {
					for (String path : index.get(word).keySet()) {
						int frequency = index.get(word).get(path).size();
						int position = index.get(word).get(path).first();

						if (searchResultMap.containsKey(path)) {
							searchResultMap.get(path).update(frequency, position);
						} else {
							SearchResult newSearchResult = new SearchResult(frequency, position, path);
							searchResultMap.put(path, newSearchResult);
							searchResults.add(newSearchResult);
						}
					}
				} else {
					break;
				}
			}
		}
		Collections.sort(searchResults);
		return searchResults;
	}

	@Override
	public String toString() {
		return "InvertedIndex [index=" + index + "]";
	}
}
