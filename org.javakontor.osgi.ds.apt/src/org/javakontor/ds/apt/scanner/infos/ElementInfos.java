package org.javakontor.ds.apt.scanner.infos;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public class ElementInfos {
	private Map<String, AnnotationInfos> annotations = new HashMap<String, AnnotationInfos>();

	public ElementInfos(Element element) {
		for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
			final String currentAnnotationType = mirror.getAnnotationType()
					.toString();
			Class annotationType;
			try {
				annotationType = Class.forName(currentAnnotationType);
				Annotation annotation = element.getAnnotation(annotationType);
				annotations.put(currentAnnotationType, new AnnotationInfos(
						annotation, mirror));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public AnnotationInfos getAnnotationInfos(
			Class<? extends Annotation> annotationType) {
		return annotations.get(annotationType.getName());
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		AnnotationInfos annotationInfos = getAnnotationInfos(annotationType);
		if (annotationInfos != null) {
			return (T) annotationInfos.get();
		}
		return null;
	}
}
