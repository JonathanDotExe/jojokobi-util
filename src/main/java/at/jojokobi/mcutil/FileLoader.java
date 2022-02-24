package at.jojokobi.mcutil;

import java.io.InputStream;

public interface FileLoader<T extends Identifiable>{
	
	public T load (InputStream in) throws Exception;

}
