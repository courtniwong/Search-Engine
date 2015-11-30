/**
 * Attributes for SearchResult objects, methods that update attributes, compare
 * method that specifies how to compare SearchResult objects.
 *
 * @author courtniwong
 *
 */
public class SearchResult implements Comparable<SearchResult> {
	private int frequency;
	private int position;
	private final String path;

	/**
	 * Constructor for SearchResult object
	 *
	 * @param frequency
	 * @param position
	 * @param path
	 */
	public SearchResult(int frequency, int position, String path) {
		super();
		this.frequency = frequency;
		this.position = position;
		this.path = path;
	}

	/**
	 * Returns the number of times a query word was found at this result.
	 *
	 * @return the number of times a query word was found at this result
	 */
	public int getFrequency() {
		return this.frequency;
	}

	/**
	 * Returns the position of the query word at this result.
	 *
	 * @return the position of the query word at this result
	 */
	public int getPosition() {
		return this.position;
	}

	/**
	 * Returns the file path of the query word at this result.
	 *
	 * @return the file path of the query word at this result
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Updates frequency and position.
	 *
	 * @param frequency
	 *            number of times word is found
	 * @param position
	 *            index of word
	 */
	public void update(int frequency, int position) {
		this.frequency += frequency;
		if (this.position > position) {
			this.position = position;
		}
	}

	/**
	 * Compares frequency, position, and then path.
	 */
	@Override
	public int compareTo(SearchResult a) {
		if (this.frequency != a.getFrequency()) {
			return Integer.compare(a.frequency, this.frequency);
		} else {
			if (this.position != a.getPosition()) {
				return Integer.compare(this.position, a.position);
			} else {
				return this.getPath().compareTo(a.getPath());
			}
		}
	}
}
