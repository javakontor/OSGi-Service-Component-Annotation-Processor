package org.javakontor.osgi.ds.apt.test.cases;

import java.util.Hashtable;
import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
@Component(provide = Map.class)
public class ComponentProvidingInterfaceFromSuperclass extends Hashtable<Object, Object> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Activate
  protected void start(Map<String, Object> properties) {

  }
}
