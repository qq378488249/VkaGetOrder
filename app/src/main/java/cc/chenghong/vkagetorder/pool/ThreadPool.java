package cc.chenghong.vkagetorder.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 * 
 * @author lenovo
 *
 */
public class ThreadPool {
	private ExecutorService service;

	private ThreadPool() {
		int num = Runtime.getRuntime().availableProcessors() * 20;
		service = Executors.newFixedThreadPool(num);
	}

	private static final ThreadPool manager = new ThreadPool();

	public static ThreadPool getInstance() {
		return manager;
	}

	public void executeTask(Runnable runnable) {
		service.execute(runnable);
	}

}
