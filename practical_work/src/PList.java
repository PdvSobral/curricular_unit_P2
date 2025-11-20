import java.util.ArrayList;
import java.util.Collections;

public class PList<T> extends ArrayList<T> {

	// Constructor to initialize the list
	public PList() {
		super(); // Calls the constructor of ArrayList
	}

	// Mimic Python's append method (ArrayList already has this)
	public void append(T item) {
		super.add(item);  // Calls ArrayList's add method
	}

	// Mimic Python's insert method
	public void insert(int index, T item) {
		if (index >= 0 && index <= size()) {
			super.add(index, item);  // Calls ArrayList's insert method (add at index)
		} else {
			throw new IndexOutOfBoundsException("Index out of range");
		}
	}

	// Mimic Python's pop method
	public T pop() {
		if (isEmpty()) {
			throw new IndexOutOfBoundsException("Pop from empty list");
		}
		return super.remove(size() - 1);  // Removes the last element and returns it
	}

	// Mimic Python's extend method
	public void extend(PList<? extends T> otherList) {
		super.addAll(otherList);  // Adds all elements from another list
	}

	// Mimic Python's index method
	public int indexOf(Object item) {
		return super.indexOf(item);  // Uses ArrayList's indexOf method
	}

	// Mimic Python's len function
	public int length() {
		return super.size();  // Uses ArrayList's size method
	}

	// Mimic Python's contains method
	public boolean contains(Object item) {
		return super.contains(item);  // Uses ArrayList's contains method
	}

	// Mimic Python's slicing (with simple range)
	public PList<T> slice(int start, int end) {
		PList<T> slicedList = new PList<>();
		for (int i = start; i < end && i < size(); i++) {
			slicedList.append(get(i));  // Uses ArrayList's get method
		}
		return slicedList;
	}

	// Mimic Python's reverse method
	public void reverse() {
		Collections.reverse(this);  // Reverses the list in place using Collections.reverse
	}

	// Mimic Python's sort method
	public void sort() {
		super.sort(null);  // Sorts the list using natural ordering (Comparable elements)
	}
}
