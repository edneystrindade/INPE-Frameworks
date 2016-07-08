package org.cbsoft.framework;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileSerializer {
	
	private PostProcessor pp;
	private DataFormatter df;



	public FileSerializer(PostProcessor pp, DataFormatter df) {
		this.pp = pp;
		this.df = df;
	}

	public void generateFile(String filename, PropertiesGetter propGetter) {
		byte[] bytes = df.FormatData(propGetter.getPropertiesList());
		
		try {
			bytes = pp.postProcess(bytes);	
			FileOutputStream fileout = new FileOutputStream(filename);
			fileout.write(bytes);
			fileout.close();
		} catch (Exception e) {
			throw new RuntimeException("Problems writing the file",e);
		}
		
	}
	
}