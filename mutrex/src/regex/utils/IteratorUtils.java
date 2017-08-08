package regex.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorUtils {

	/**
	 * converts an iterator to a list (and brings the iterator to the end)
	 * 
	 * @param iter
	 * @return
	 */
	public static <T> List<T> iteratorToList(Iterator<T> iter) {
		List<T> copy = new ArrayList<T>();
		while (iter.hasNext())
			copy.add(iter.next());
		return copy;
	}
}
