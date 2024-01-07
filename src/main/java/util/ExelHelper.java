package util;

import exception.CommandNotSupportedException;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author Ihor Vasyliev
 * @since 2024/01/07
 */
public class ExelHelper {

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void save(HSSFWorkbook book, String folderPath, String fileName) throws IOException {
		File folder = new File(folderPath);
		folder.mkdirs();
		File file = new File(folder, fileName);
		file.createNewFile();
		try (FileOutputStream out = new FileOutputStream(file)) {
				book.write(out);
		}
	}

	public static void saveThenPrint(HSSFWorkbook book, String folderPath, String fileName) throws IOException, CommandNotSupportedException {
		save(book, folderPath, fileName);

		File folder = new File(folderPath);
		File file = new File(folder, fileName);
		Desktop desktop;
		if (Desktop.isDesktopSupported() && file.exists()) {
			desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.PRINT)) {
				desktop.print(file);
			} else {
				throw new CommandNotSupportedException(CommandNotSupportedException.Command.PRINT);
			}
		}
	}

	public static void saveThenOpen(HSSFWorkbook book, String folderPath, String fileName) throws IOException {
		save(book, folderPath, fileName);
		File folder = new File(folderPath);
		File file = new File(folder, fileName);
		Desktop desktop;
		if (Desktop.isDesktopSupported() && file.exists()) {
			desktop = Desktop.getDesktop();
			desktop.open(file);
		}
	}
}
