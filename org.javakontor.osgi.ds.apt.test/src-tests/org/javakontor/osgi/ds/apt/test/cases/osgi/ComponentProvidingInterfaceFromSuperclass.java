package org.javakontor.osgi.ds.apt.test.cases.osgi;

import java.util.Hashtable;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 * 
 */
@Component(service = Map.class)
public class ComponentProvidingInterfaceFromSuperclass extends Hashtable<Object, Object> {

  public class Toto {

  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Activate
  protected void start(Map<String, Object> properties) {

  }
}
