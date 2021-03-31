package ru.job4j.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.Iterator;

@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {

    @GuardedBy("this")
    private final SimpleList<T> list = new SimpleList<>();

    public synchronized void add(T value) {
        list.add(value);
    }

    public synchronized T get(int index) {
        return list.get(index);
    }

    private synchronized SimpleList<T> copy(SimpleList<T> list) {
        SimpleList<T> rsl = new SimpleList<>();
        list.forEach(rsl::add);
        return rsl;
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return copy(list).iterator();
    }
}
