import java.util.ArrayList;

/**
 * Parse and normalize text.
 *
 * @author courtniwong
 *
 */
public class WordParser {

	/** Regular expression for removing special characters. */
	public static final String CLEAN_REGEX = "(?U)[^\\p{Alnum}\\p{Space}]+";

	/** Regular expression for splitting text into words by whitespace. */
	public static final String SPLIT_REGEX = "(?U)\\p{Space}+";

	/**
	 * Converts text into lowercase, replaces special characters with an empty
	 * string, and trims whitespace at the start and end of the string. Special
	 * characters include any non-alphanumeric character or whitespace. Allowed
	 * characters include "A", "a", "á", "ä", and "9". Unallowed characters
	 * include "_", "-", "@", ".", and so on.
	 *
	 * @param text
	 *            input to clean
	 * @return cleaned text
	 *
	 * @see #CLEAN_REGEX
	 */
	public static String clean(String text) {
		text = text.replaceAll(CLEAN_REGEX, "");
		text = text.toLowerCase();
		text = text.trim();

		return text;
	}

	/**
	 * First cleans text. If the result is non-empty, splits the cleaned text
	 * into words by whitespace. The result will be an arraylist of words in all
	 * lowercase without any special characters, or an empty array if the
	 * cleaned text was empty.
	 *
	 * @param text
	 *            input to clean and split into words
	 * @return arraylist of words (or an empty array if cleaned text is empty)
	 *
	 * @see #clean(String)
	 * @see #SPLIT_REGEX
	 */
	public static ArrayList<String> split(String text) {
		text = clean(text);
		String[] list = text.split(SPLIT_REGEX);
		ArrayList<String> words = new ArrayList<>();
		for (String word : list) {
			if (!word.isEmpty()) {
				words.add(word);
			}
		}
		return words;
	}
}
