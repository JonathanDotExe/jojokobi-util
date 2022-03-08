package at.jojokobi.mcutil.general;

public final class ArrayUtil {

	private ArrayUtil() {
		
	}
	
	public static <T> boolean arrayContains (T[] array, T value) {
		boolean contains = false;
		for (int i = 0; i < array.length && !contains; i++) {
			contains = array[i] == value;
		}
		return contains;
	}

}
