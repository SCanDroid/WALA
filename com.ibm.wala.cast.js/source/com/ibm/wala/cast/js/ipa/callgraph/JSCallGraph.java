/******************************************************************************
 * Copyright (c) 2002 - 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *****************************************************************************/
package com.ibm.wala.cast.js.ipa.callgraph;

import com.ibm.wala.cast.ipa.callgraph.AstCallGraph;
import com.ibm.wala.cast.js.cfg.JSInducedCFG;
import com.ibm.wala.cast.js.loader.JSCallSiteReference;
import com.ibm.wala.cast.js.ssa.JavaScriptInvoke;
import com.ibm.wala.cast.js.types.JavaScriptTypes;
import com.ibm.wala.cfg.InducedCFG;
import com.ibm.wala.classLoader.CallSiteReference;
import com.ibm.wala.classLoader.NewSiteReference;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.callgraph.impl.FakeRootMethod;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSANewInstruction;
import com.ibm.wala.types.MethodReference;
import com.ibm.wala.types.TypeReference;

public class JSCallGraph extends AstCallGraph {

  public JSCallGraph(IClassHierarchy cha, AnalysisOptions options, AnalysisCache cache) {
    super(cha, options, cache);
  }

  public final static MethodReference fakeRoot = MethodReference.findOrCreate(JavaScriptTypes.FakeRoot, FakeRootMethod.name,
      FakeRootMethod.descr);

  public static class JSFakeRoot extends ScriptFakeRoot {

    public JSFakeRoot(IClassHierarchy cha, AnalysisOptions options, AnalysisCache cache) {
      super(fakeRoot, cha.lookupClass(JavaScriptTypes.FakeRoot), cha, options, cache);
    }

    public InducedCFG makeControlFlowGraph(SSAInstruction[] instructions) {
      return new JSInducedCFG(instructions, this, Everywhere.EVERYWHERE);
    }

    public SSANewInstruction addAllocation(TypeReference T) {
      if (cha.isSubclassOf(cha.lookupClass(T), cha.lookupClass(JavaScriptTypes.Root))) {
        int instance = nextLocal++;
        NewSiteReference ref = NewSiteReference.make(statements.size(), T);
        SSANewInstruction result = getDeclaringClass().getClassLoader().getInstructionFactory().NewInstruction(instance, ref);
        statements.add(result);
        return result;
      } else {
        return super.addAllocation(T);
      }
    }

    public SSAAbstractInvokeInstruction addDirectCall(int function, int[] params, CallSiteReference site) {
      CallSiteReference newSite = new JSCallSiteReference(statements.size());

      JavaScriptInvoke s = new JavaScriptInvoke(function, nextLocal++, params, nextLocal++, newSite);
      statements.add(s);

      return s;
    }
  }

  @Override
  protected CGNode makeFakeWorldClinitNode() {
    return null;
  }

  protected CGNode makeFakeRootNode() throws com.ibm.wala.util.CancelException  {
    return findOrCreateNode(new JSFakeRoot(cha, options, getAnalysisCache()), Everywhere.EVERYWHERE);
  }
}
