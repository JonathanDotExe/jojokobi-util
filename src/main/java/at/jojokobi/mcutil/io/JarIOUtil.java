package at.jojokobi.mcutil.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public final class JarIOUtil {
	
	private JarIOUtil() {
		
	}
	
	public static void inputStreamToFile (InputStream from, File to) {
		if (to.isFile()) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(to));
				Scanner scanner = new Scanner(from);) {
				
				while (scanner.hasNext()) {
					try {
						writer.write(scanner.next());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
