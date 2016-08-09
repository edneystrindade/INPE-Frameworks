package org.cbsoft.framework;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FileSerializer implements Serializer {
	
	private PostProcessor pp;
	private DataFormatter df;



	public FileSerializer(PostProcessor pp, DataFormatter df) {
		this.pp = pp;
		this.df = df;
	}

	@Override
	public PostProcessor getPostProcessor() {
		return pp;
	}

	@Override
	public void setPostProcessor(PostProcessor pp) {
		this.pp = pp;
	}





	/* (non-Javadoc)
	 * @see org.cbsoft.framework.Serializer#generateFile(java.lang.String, java.lang.Object)
	 */
	@Override
	public void generateFile(String filename, Object obj) {
		byte[] bytes = df.FormatData(getPropertiesList(obj));
		
		try {
			bytes = pp.postProcess(bytes);	
			FileOutputStream fileout = new FileOutputStream(filename);
			fileout.write(bytes);
			fileout.close();
		} catch (Exception e) {
			throw new RuntimeException("Problems writing the file",e);
		}
		
	}

	
	private Map<String, Object> getPropertiesList(Object obj) {
		Map<String,Object> props = new HashMap<String, Object>();
		Class<?> clazz = obj.getClass();
		
		for (Method m: clazz.getMethods()){
			if (isAllowedGetter(m)) {
				try {
					Object value = m.invoke(obj);
					String getterName = m.getName();
					String propName = getterName.substring(3).toLowerCase();
					value = formatValue(m, value);
					props.put(propName, value);
				} catch (Exception e) {
					throw new RuntimeException("Cannot retrieve properties.", e);
				} 
				
			}
		}
		
		return props;
	}

	private Object formatValue(Method m, Object value) throws InstantiationException, IllegalAccessException {
		for (Annotation an : m.getAnnotations()) {
			Class <?> anType = an.annotationType();
			if (anType.isAnnotationPresent(FormatterImplementation.class)) {
				FormatterImplementation fi = anType.getAnnotation(FormatterImplementation.class);
				Class <? extends ValueFormatter> c = fi.value();
				ValueFormatter vf = c.newInstance();
				vf.readAnnotation(an);
				value = vf.formatValue(value);
			}
		}
		return value;
	}

	private boolean isAllowedGetter(Method m) {
		return m.getName().startsWith("get") && 
				m.getParameterTypes().length == 0 &&
				m.getReturnType() != void.class &&
				!m.getName().equals("getClass") &&
				!m.isAnnotationPresent(DontIncludeOnFile.class);
	}
	
}