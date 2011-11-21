package org.javakontor.ds.apt.scanner.infos;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;

public class AnnotationInfos {
	private Annotation annotation;
	private Map<String, Object> values = new HashMap<String, Object>();

	public AnnotationInfos(Annotation annotation, AnnotationMirror mirror) {
		this.annotation = annotation;
		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mirror
				.getElementValues().entrySet()) {
			Object value = entry.getValue().getValue();
			String attributeName = entry.getKey().getSimpleName().toString();
			values.put(attributeName, value);
		}
	}

	public String[] getArrayAsStrings(String attributeName) {
		Object value = values.get(attributeName);
		if (value != null) {
			Collection<AnnotationValue> col = (Collection<AnnotationValue>) value;
			String[] stringValues = new String[col.size()];
			int pos = 0;
			for (AnnotationValue interfaceValue : col) {
				String stringValue = interfaceValue.getValue().toString();
				stringValues[pos++] = stringValue;
			}

			return stringValues;
		} else {
			return new String[0];
		}
	}

	public Object getValue(String attributeName) {
		Object value = values.get(attributeName);
		if (value != null) {
			return value;
		} else {
			return null;
		}
	}

	public String getStringValue(String attributeName) {
		Object value = values.get(attributeName);
		if (value != null) {
			return value.toString();
		} else {
			return null;
		}
	}

	public Annotation get() {
		return annotation;
	}
}
