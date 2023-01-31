package com.coderscampus.assignment;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessedCode {
	public static  void main(String[] args) {
		
		Assignment8 assignment8 = new Assignment8(); 
		
		//To achieve thread safety of List, we can use Collections.synchronizedList
		//(but when using Iterator/for each ,we should add synchronized(list) block)
		List<Integer> datas = Collections.synchronizedList(new ArrayList<>(1000)); 
		
		ExecutorService executor = Executors.newCachedThreadPool(); 
		List<CompletableFuture<Void>> tasks = new ArrayList<>();
		
		for (int i = 0; i < 1000; i++) {
			CompletableFuture<Void> task = CompletableFuture.supplyAsync(() -> assignment8.getNumbers(), executor)
					.thenAccept(data -> datas.addAll(data));
			
			tasks.add(task);
		}
		
		while(tasks.stream().filter(CompletableFuture :: isDone).count() < 1000) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		System.out.println("Done, I have fetched "+ datas.size() + " number");
	
		/*
		 *  step 2
		 *  Once you are able to asynchronously fetch the data, you'll need to
		 *  determine the number of times each unique number appears.
		 */		
		Map<Integer, Integer> result = new HashMap<>();
		
		datas.stream().forEach(n -> {
			if(!result.containsKey(n)) {
				result.put(n, 1);
			}
			else {
				result.put(n, result.get(n) + 1);
			}
		});
		
		System.out.println(result);
	
	}

}
