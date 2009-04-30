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
package com.ibm.wala.ipa.summaries;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.SyntheticMethod;
import com.ibm.wala.ipa.callgraph.Context;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAOptions;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.debug.Assertions;

/**
 * @author sfink
 * 
 */
public class SummarizedMethod extends SyntheticMethod {
  static final boolean DEBUG = false;

  final private MethodSummary summary;

  public SummarizedMethod(MethodReference ref, MethodSummary summary, IClass declaringClass) throws NullPointerException {
    super(ref, declaringClass, summary.isStatic(), summary.isFactory());
    this.summary = summary;
    if (Assertions.verifyAssertions) {
      assert declaringClass != null;
    }
    if (DEBUG) {
      System.err.println(("SummarizedMethod ctor: " + ref + " " + summary));
    }
  }

  /**
   * @see com.ibm.wala.classLoader.IMethod#isNative()
   */
  @Override
  public boolean isNative() {
    return summary.isNative();
  }

  /**
   * @see com.ibm.wala.classLoader.IMethod#isAbstract()
   */
  @Override
  public boolean isAbstract() {
    return false;
  }

  /*
   * @see com.ibm.wala.classLoader.SyntheticMethod#getPoison()
   */
  @Override
  public String getPoison() {
    return summary.getPoison();
  }

  /*
   * @see com.ibm.wala.classLoader.SyntheticMethod#getPoisonLevel()
   */
  @Override
  public byte getPoisonLevel() {
    return summary.getPoisonLevel();
  }

  /*
   * @see com.ibm.wala.classLoader.SyntheticMethod#hasPoison()
   */
  @Override
  public boolean hasPoison() {
    return summary.hasPoison();
  }

  @Override
  public SSAInstruction[] getStatements(SSAOptions options) {
    if (DEBUG) {
      System.err.println(("getStatements: " + this));
    }
    return summary.getStatements();
  }

  // /*
  // *
  // * @see com.ibm.wala.classLoader.IMethod#getIR(com.ibm.wala.util.WarningSet)
  // */
  // public IR getIR(SSAOptions options, WarningSet warnings) {
  // if (DEBUG) {
  // Trace.println("Get IR: " + this);
  // }
  // return findOrCreateIR(options, warnings);
  // }

  // /**
  // * @return
  // */
  // private IR findOrCreateIR(SSAOptions options, WarningSet warnings) {
  // IR result = (IR) CacheReference.get(ir);
  // if (result == null) {
  // if (DEBUG) {
  // Trace.println("Create IR for " + this);
  // }
  // SSAInstruction instrs[] = getStatements(options, warnings);
  // result = new SyntheticIR(this, Everywhere.EVERYWHERE,
  // makeControlFlowGraph(), instrs, options, summary.getConstants(),
  // warnings);
  // ir = CacheReference.make(result);
  // }
  // return result;
  // }
  @Override
  public IR makeIR(Context context, SSAOptions options) {
    SSAInstruction instrs[] = getStatements(options);
    return new SyntheticIR(this, Everywhere.EVERYWHERE, makeControlFlowGraph(instrs), instrs, options, summary.getConstants());
  }

  /*
   * @see com.ibm.wala.classLoader.IMethod#getNumberOfParameters()
   */
  @Override
  public int getNumberOfParameters() {
    return summary.getNumberOfParameters();
  }

  /*
   * @see com.ibm.wala.classLoader.IMethod#isStatic()
   */
  @Override
  public boolean isStatic() {
    return summary.isStatic();
  }

  /*
   * @see com.ibm.wala.classLoader.IMethod#getParameterType(int)
   */
  @Override
  public TypeReference getParameterType(int i) {
    return summary.getParameterType(i);
  }

}
