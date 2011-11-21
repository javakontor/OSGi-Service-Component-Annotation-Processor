package org.javakontor.ds.apt;

import aQute.bnd.annotation.component.Component;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.javakontor.ds.apt.scanner.BndAnnotationScanner;
import org.javakontor.ds.apt.scanner.OsgiAnnotationScanner;

@SupportedAnnotationTypes(value = { "aQute.bnd.annotation.component.*",
		"org.osgi.service.component.annotations.*" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class DsAnnotationProcessor extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		if (!roundEnv.processingOver()) {
			Set<? extends Element> components = roundEnv.getRootElements();
			for (Element element : components) {
				if (element.getAnnotation(Component.class) != null) {
					BndAnnotationScanner bndScanner = new BndAnnotationScanner(
							processingEnv);
					bndScanner.scan(element);
					bndScanner.exportXml();
				} else if (element
						.getAnnotation(org.osgi.service.component.annotations.Component.class) != null) {
					OsgiAnnotationScanner osgiScanner = new OsgiAnnotationScanner(
							processingEnv);
					osgiScanner.scan(element);
					osgiScanner.exportXml();
				}
			}
		}
		return false;
	}
}
