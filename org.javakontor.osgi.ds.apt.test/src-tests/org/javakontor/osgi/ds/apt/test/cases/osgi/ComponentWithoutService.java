package org.javakontor.osgi.ds.apt.test.cases.osgi;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
@Component
public class ComponentWithoutService {

  @Activate
  protected void activate(Map<String, Object> params) {

  }

}
