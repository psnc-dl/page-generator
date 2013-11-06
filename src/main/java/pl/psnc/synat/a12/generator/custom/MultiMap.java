package pl.psnc.synat.a12.generator.custom;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiMap<K, V> implements Map<K, List<V>> {

    private final Map<K, List<V>> map = new HashMap<K, List<V>>();


    public void putElement(K key, V value) {
        List<V> list = map.get(key);

        if (list == null) {
            list = new LinkedList<V>();
            map.put(key, list);
        }
        list.add(value);
    }


    public int size() {
        return map.size();
    }


    public int size(K c) {
        if (!map.containsKey(c))
            return 0;

        return map.get(c).size();
    }


    public boolean isEmpty() {
        return map.isEmpty();
    }


    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }


    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }


    public List<V> get(Object key) {
        return map.get(key);
    }


    public V get(K key, int i) {
        List<V> list = map.get(key);

        if (list == null)
            return null;
        return list.get(i);
    }


    public List<V> remove(Object key) {
        return map.remove(key);
    }


    public void clear() {
        map.clear();
    }


    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return map.put(key, value);
    }


    public void putAll(Map<? extends K, ? extends List<V>> m) {
        map.putAll(m);
    }


    public Collection<List<V>> values() {
        return map.values();
    }


    public Set<java.util.Map.Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }
}
