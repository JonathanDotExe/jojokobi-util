package at.jojokobi.mcutil;

import java.util.List;

public interface ItemProvider<T extends Identifiable> {

	public List<T> provide ();
	
}
