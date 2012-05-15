package org.javakontor.osgi.ds.apt.test.cases.bnd;

import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

/**
 * @author Simon Chemouil (simon.chemouil@global-vision-systems.com)
 * 
 */
@Component(enabled = false)
public class ComponentNotEnabled {

  @Activate
  protected void activate(Map<String, Object> params) {

  }

}
