package qLearner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.*;

public class Indexable<K> implements Iterable<K> {

    final ImmutableList<K> list;
    private final ImmutableMap<K, Integer> m;

    public Indexable(Collection<K> coll) {
        list = ImmutableList.copyOf(coll);
        HashMap h = new HashMap<K, Integer>();
        for (int i = 0; i < list.size(); i++) {
            h.put(list.get(i), i);
        }
        m = ImmutableMap.copyOf(h);
    }

    public K get(int i) {
        return list.get(i);
    }

    public int indexOf(K k) {
        //list.indexOf(k)   O(n)
        return m.getOrDefault(k, -1);  // O(1)
    }

    public int size() {
        return list.size();
    }


    @Override
    public Iterator<K> iterator() {
        return list.iterator();
    }
}
