package org.javakontor.osgi.ds.apt.test.cases.osgi;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

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
