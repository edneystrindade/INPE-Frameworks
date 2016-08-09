package org.cbsoft.framework;

public interface Serializer {

	void generateFile(String filename, Object obj);

	void setPostProcessor(PostProcessor pp);

	PostProcessor getPostProcessor();

}