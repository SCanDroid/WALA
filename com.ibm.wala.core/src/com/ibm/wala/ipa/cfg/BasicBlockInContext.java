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
package com.ibm.wala.ipa.cfg;

import java.util.Iterator;

import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPhiInstruction;
import com.ibm.wala.ssa.SSAPiInstruction;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.graph.impl.NodeWithNumber;

/**
 * A helper class to make the ipcfg work correctly with context-sensitive call
 * graphs.
 */
public final class BasicBlockInContext<T extends ISSABasicBlock> extends NodeWithNumber implements ISSABasicBlock {
  private final T delegate;

  private final CGNode node;

  public BasicBlockInContext(CGNode node, T bb) {
    if (bb == null) {
      throw new IllegalArgumentException("null bb");
    }
    this.delegate = bb;
    this.node = node;
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#getFirstInstructionIndex()
   */
  public int getFirstInstructionIndex() {
    return delegate.getFirstInstructionIndex();
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#getLastInstructionIndex()
   */
  public int getLastInstructionIndex() {
    return delegate.getLastInstructionIndex();
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#iterateAllInstructions()
   */
  public Iterator<SSAInstruction> iterator() {
    return delegate.iterator();
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#getMethod()
   */
  public IMethod getMethod() {
    return delegate.getMethod();
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#getNumber()
   */
  public int getNumber() {
    return delegate.getNumber();
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#isCatchBlock()
   */
  public boolean isCatchBlock() {
    return delegate.isCatchBlock();
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#isEntryBlock()
   */
  public boolean isEntryBlock() {
    return delegate.isEntryBlock();
  }

  /*
   * @see com.ibm.wala.cfg.IBasicBlock#isExitBlock()
   */
  public boolean isExitBlock() {
    return delegate.isExitBlock();
  }

  int hashCode = 0;
  @Override
  public int hashCode() {
    if (hashCode == 0) {
      final int prime = 31;
      hashCode = 1;
      hashCode = prime * hashCode + ((delegate == null) ? 0 : delegate.hashCode());
      hashCode = prime * hashCode + ((node == null) ? 0 : node.hashCode());
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final BasicBlockInContext other = (BasicBlockInContext) obj;
    if (delegate == null) {
      if (other.delegate != null)
        return false;
    } else if (!delegate.equals(other.delegate))
      return false;
    if (node == null) {
      if (other.node != null)
        return false;
    } else if (!node.equals(other.node))
      return false;
    return true;
  }

  public T getDelegate() {
    return delegate;
  }

  public CGNode getNode() {
    return node;
  }

  @Override
  public String toString() {
    return delegate.toString();
  }

  public Iterator<TypeReference> getCaughtExceptionTypes() {
    return delegate.getCaughtExceptionTypes();
  }

  public SSAInstruction getLastInstruction() {
    return delegate.getLastInstruction();
  }

  public Iterator<SSAPhiInstruction> iteratePhis() {
    return delegate.iteratePhis();
  }

  public Iterator<SSAPiInstruction> iteratePis() {
    return delegate.iteratePis();
  }

}
