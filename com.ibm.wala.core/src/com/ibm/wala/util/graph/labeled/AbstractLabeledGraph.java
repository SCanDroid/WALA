/*******************************************************************************
 * Copyright (c) 2007 Manu Sridharan and Juergen Graf
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Manu Sridharan
 *     Juergen Graf
 *******************************************************************************/
/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * This file is a derivative of code released by the University of
 * California under the terms listed below.  
 *
 * Refinement Analysis Tools is Copyright ©2007 The Regents of the
 * University of California (Regents). Provided that this notice and
 * the following two paragraphs are included in any distribution of
 * Refinement Analysis Tools or its derivative work, Regents agrees
 * not to assert any of Regents' copyright rights in Refinement
 * Analysis Tools against recipient for recipientís reproduction,
 * preparation of derivative works, public display, public
 * performance, distribution or sublicensing of Refinement Analysis
 * Tools and derivative works, in source code and object code form.
 * This agreement not to assert does not confer, by implication,
 * estoppel, or otherwise any license or rights in any intellectual
 * property of Regents, including, but not limited to, any patents
 * of Regents or Regentsí employees.
 * 
 * IN NO EVENT SHALL REGENTS BE LIABLE TO ANY PARTY FOR DIRECT,
 * INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
 * INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE
 * AND ITS DOCUMENTATION, EVEN IF REGENTS HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *   
 * REGENTS SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE AND FURTHER DISCLAIMS ANY STATUTORY
 * WARRANTY OF NON-INFRINGEMENT. THE SOFTWARE AND ACCOMPANYING
 * DOCUMENTATION, IF ANY, PROVIDED HEREUNDER IS PROVIDED "AS
 * IS". REGENTS HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT,
 * UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */
package com.ibm.wala.util.graph.labeled;

import java.util.Iterator;
import java.util.Set;

import com.ibm.wala.util.graph.AbstractGraph;

public abstract class AbstractLabeledGraph<T, U> extends AbstractGraph<T> implements LabeledGraph<T, U> {

  /**
   * @return the object which manages edges in the graph
   */
  @Override
  protected abstract LabeledEdgeManager<T, U> getEdgeManager();

  public void addEdge(T src, T dst, U label) {
    getEdgeManager().addEdge(src, dst, label);
  }

  public Iterator<? extends U> getPredLabels(T N) {
    return getEdgeManager().getPredLabels(N);
  }

  public int getPredNodeCount(T N, U label) {
    return getEdgeManager().getPredNodeCount(N, label);
  }

  public Iterator<? extends T> getPredNodes(T N, U label) {
    return getEdgeManager().getPredNodes(N, label);
  }

  public Iterator<? extends U> getSuccLabels(T N) {
    return getEdgeManager().getSuccLabels(N);
  }

  public int getSuccNodeCount(T N, U label) {
    return getEdgeManager().getSuccNodeCount(N, label);
  }

  public Iterator<? extends T> getSuccNodes(T N, U label) {
    return getEdgeManager().getSuccNodes(N, label);
  }

  public boolean hasEdge(T src, T dst, U label) {
    return getEdgeManager().hasEdge(src, dst, label);
  }

  public void removeEdge(T src, T dst, U label) {
    getEdgeManager().removeEdge(src, dst, label);
  }

  public Set<? extends U> getEdgeLabels(T src, T dst) {
    return getEdgeManager().getEdgeLabels(src, dst);
  }

}
