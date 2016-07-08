package org.cbsoft.framework;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompositePostProcessor implements PostProcessor {
	
	private List<PostProcessor> processors;
	
	public CompositePostProcessor(PostProcessor... list) {
		// TODO Auto-generated constructor stub
		processors = new ArrayList<PostProcessor>();
		for(PostProcessor p : list) {
			processors.add(p);
		}
	}

	@Override
	public byte[] postProcess(byte[] bytes) throws IOException {
		// TODO Auto-generated method stub
		byte [] current = bytes; 
		for (PostProcessor p : processors) {
			bytes = p.postProcess(bytes);
		}
		return bytes;
	}

}
