import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Dict<K, V> {

	private final Map<K, V> __map;

	// Constructor to initialize the dictionary
	public Dict() {
		this.__map = new HashMap<>();
	}

	// Mimic Python's dict[key] = value (put operation)
	public void set(K key, V value) {
		__map.put(key, value);
	}

	// Mimic Python's dict[key] (get operation)
	public V get(K key) {
		return __map.get(key);
	}

	// Mimic Python's 'in' keyword (check if key exists)
	public boolean contains(K key) {
		return __map.containsKey(key);
	}

	// Mimic Python's del keyword (remove a key-value pair)
	public V remove(K key) {
		return __map.remove(key);
	}

	// Mimic Python's dict.get(key, default) (get with default value)
	public V getOrDefault(K key, V defaultValue) {
		return __map.getOrDefault(key, defaultValue);
	}

	// Mimic Python's len(d) (get the size of the dict)
	public int length() {
		return __map.size();
	}

	// Mimic Python's clear() (clear all key-value pairs)
	public void clear() {
		__map.clear();
	}

	// Mimic Python's items() (return key-value pairs as a set)
	public Set<Map.Entry<K, V>> items() {
		return __map.entrySet();
	}

	// Mimic Python's keys() (return all keys as a PList)
	public PList<K> keys() {
		PList<K> keyList = new PList<>();
		keyList.addAll(__map.keySet());
		return keyList;
	}

	// Mimic Python's values() (return all values as a PList)
	public PList<V> values() {
		PList<V> valueList = new PList<>();
		valueList.addAll(__map.values());
		return valueList;
	}

	// Mimic Python's __str__ (toString)
	@Override
	public String toString() {
		return __map.toString();
	}
}
