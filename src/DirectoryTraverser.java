import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods to traverse a given directory.
 *
 * @author courtniwong
 *
 */
public class DirectoryTraverser {
	/**
	 * Returns a arraylist of textfiles.
	 *
	 * @param directory
	 *            input file
	 * @return List of text files
	 */
	public static ArrayList<String> traverse(Path directory) {
		ArrayList<String> files = new ArrayList<>();
		traverse(directory, files);
		return files;
	}

	/**
	 * Recursively goes through the directory and adds all files ending in "txt"
	 * to the arraylist.
	 *
	 * @param directory
	 *            input directory
	 * @param files
	 *            List of textfiles
	 */
	public static void traverse(Path directory, List<String> files) {
		try (DirectoryStream<Path> directoryList = Files.newDirectoryStream(directory)) {
			for (Path file : directoryList) {
				if (Files.isDirectory(file)) {
					traverse(file, files);
				} else {
					if (file.toString().toLowerCase().endsWith("txt")) {
						files.add(file.toString());
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Directory is not valid.");
		}
	}
}
