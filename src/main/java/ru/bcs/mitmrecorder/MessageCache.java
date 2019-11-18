package ru.bcs.mitmrecorder;

import lombok.Data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class MessageCache<K, T> {

    private long timeToLive;
    private final LinkedHashMap<K, MessageCacheObject<T>> cacheMap;

    @Data
    protected static class MessageCacheObject<T> {
        private long lastAccessed = System.currentTimeMillis();
        private T value;

        protected MessageCacheObject(T value) {
            this.value = value;
        }
    }

    public MessageCache(long timeToLive, final long timeInterval, int max) {
        this.timeToLive = timeToLive * 2000;

        cacheMap = new LinkedHashMap<>(max);

        if (timeToLive > 0 && timeInterval > 0) {

            Thread t = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(timeInterval * 1000);
                        cleanup();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    // PUT method
    public void put(K key, T value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new MessageCacheObject<>(value));
        }
    }

    // GET method
    @SuppressWarnings("unchecked")
    public T get(K key) {
        synchronized (cacheMap) {
            MessageCacheObject c = cacheMap.get(key);

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return (T) c.value;
            }
        }
    }

    // GET method
    @SuppressWarnings("unchecked")
    public T getLast() {
        synchronized (cacheMap) {
            if (cacheMap.size() < 1) {
                return null;
            }
            MessageCacheObject c = ((Entry<K, MessageCacheObject<T>>) cacheMap.entrySet().toArray()[cacheMap.size() - 1]).getValue();

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return (T) c.value;
            }
        }
    }

    // GET method
    @SuppressWarnings("unchecked")
    public T getLastByKeyPart(K keyPart) {
        synchronized (cacheMap) {
            K foundKey = null;
            for (K key : cacheMap.keySet()) {
                if (key instanceof String) {
                    if (((String) key).contains((String) keyPart)) {
                        foundKey = key;
                    }
                } else {
                    if (key.toString().contains(keyPart.toString())) {
                        foundKey = key;
                    }
                }
            }
            MessageCacheObject c = cacheMap.get(foundKey);

            if (c == null)
                return null;
            else {
                c.lastAccessed = System.currentTimeMillis();
                return (T) c.value;
            }
        }
    }

    // REMOVE method
    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    // Get Cache Objects Size()
    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    public void printKeys() {
        cacheMap.forEach((url, message) -> {
            System.out.println(url);
        });
    }

    // CLEANUP method
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKeyList;

        synchronized (cacheMap) {
            Iterator<Entry<K, MessageCacheObject<T>>> itr = cacheMap.entrySet().iterator();

            deleteKeyList = new ArrayList<>((cacheMap.size() / 2) + 1);
            MessageCacheObject<T> messageCacheObject;
            while (itr.hasNext()) {
                Entry<K, MessageCacheObject<T>> entry = itr.next();
                K key = entry.getKey();
                messageCacheObject = entry.getValue();
                if (messageCacheObject != null
                        && (now > (timeToLive + messageCacheObject.lastAccessed))) {
                    deleteKeyList.add(key);
                }
            }
        }

        for (K key : deleteKeyList) {
            synchronized (cacheMap) {
                cacheMap.remove(key);
            }

            Thread.yield();
        }
    }

}
