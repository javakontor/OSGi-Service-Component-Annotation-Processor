package org.javakontor.osgi.ds.apt.test.cases.bnd;

import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

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
