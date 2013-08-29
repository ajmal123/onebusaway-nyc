package org.onebusaway.nyc.webapp.actions.api.siri;

import org.onebusaway.nyc.queue.QueueListenerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;

public abstract class NycSiriCacheService<K, V> {

	static Logger _log = LoggerFactory.getLogger(QueueListenerTask.class);
	protected Cache<K, V> _cache;  

	public Cache<K, V> getCache(){
		return _cache;
	}

	protected V retrieve(K key){
		return getCache().getIfPresent(key);
	}

	protected void store(K key, V value) {
		getCache().put(key, value);
	}

	protected boolean containsKey(K key){
		return getCache().asMap().containsKey(key);
	}

	protected abstract void refreshCache();

}