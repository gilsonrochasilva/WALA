/*******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ibm.wala.ipa.callgraph.impl;

import java.util.Iterator;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cha.ClassHierarchy;
import com.ibm.wala.util.debug.Trace;

/**
 * 
 * Includes all application methods in an analysis scope as entrypoints.
 * 
 * @author sfink
 * @autor eyahav
 */
public class AllApplicationEntrypoints extends BasicEntrypoints {

  private final static boolean DEBUG = false;
  /**
   * @param scope
   *          governing analyais scope
   * @param cha
   *          governing class hierarchy
   */
  public AllApplicationEntrypoints(AnalysisScope scope, final ClassHierarchy cha) {

    for (IClass klass : cha) {
      if (!klass.isInterface()) {
        if (isApplicationClass(scope, klass)) {
          for (Iterator methodIt = klass.getDeclaredMethods().iterator(); methodIt.hasNext();) {
            IMethod method = (IMethod) methodIt.next();
            if (!method.isAbstract()) {
              add(new ArgumentTypeEntrypoint(method, cha));
            }
          }
        }
      }
    }
    if (DEBUG) {
      Trace.println(getClass() + "Number of EntryPoints:" + size());
    }

  }

  /**
   * @param scope
   * @param klass
   * @return true iff klass is loaded by the application loader.
   */
  private boolean isApplicationClass(AnalysisScope scope, IClass klass) {
    return scope.getApplicationLoader().equals(klass.getClassLoader().getReference());
  }
}