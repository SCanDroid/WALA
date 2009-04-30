package com.ibm.wala.cast.ir.ssa;

import java.util.Collection;
import java.util.Collections;

import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstructionFactory;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.FieldReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.debug.Assertions;

public class AstIsDefinedInstruction extends SSAInstruction {
  private final FieldReference fieldRef;

  private final int fieldVal;

  private final int rval;

  private final int lval;

  public AstIsDefinedInstruction(int lval, int rval, int fieldVal, FieldReference fieldRef) {
    this.lval = lval;
    this.rval = rval;
    this.fieldVal = fieldVal;
    this.fieldRef = fieldRef;
  }

  public AstIsDefinedInstruction(int lval, int rval, FieldReference fieldRef) {
    this.lval = lval;
    this.rval = rval;
    this.fieldVal = -1;
    this.fieldRef = fieldRef;
  }

  public AstIsDefinedInstruction(int lval, int rval, int fieldVal) {
    this.lval = lval;
    this.rval = rval;
    this.fieldVal = fieldVal;
    this.fieldRef = null;
  }

  public AstIsDefinedInstruction(int lval, int rval) {
    this.lval = lval;
    this.rval = rval;
    this.fieldVal = -1;
    this.fieldRef = null;
  }

  public SSAInstruction copyForSSA(SSAInstructionFactory insts,  int[] defs, int[] uses) {
    if (Assertions.verifyAssertions) {
      assert fieldVal == -1 || fieldRef == null;
    }

    return ((AstInstructionFactory)insts).IsDefinedInstruction((defs == null) ? lval : defs[0], (uses == null) ? rval : uses[0],
        (uses == null || fieldVal == -1) ? fieldVal : uses[1], fieldRef);
  }

  public String toString(SymbolTable symbolTable) {
    if (fieldVal == -1 && fieldRef == null) {
      return getValueString(symbolTable, lval) + " = isDefined(" + getValueString(symbolTable, rval) + ")";
    } else if (fieldVal == -1) {
      return getValueString(symbolTable, lval) + " = isDefined(" + getValueString(symbolTable, rval) + "," + fieldRef.getName()
          + ")";
    } else if (fieldRef == null) {
      return getValueString(symbolTable, lval) + " = isDefined(" + getValueString(symbolTable, rval) + ","
          + getValueString(symbolTable, fieldVal) + ")";
    } else {
      Assertions.UNREACHABLE();
      return null;
    }
  }

  public void visit(IVisitor v) {
    ((AstInstructionVisitor) v).visitIsDefined(this);
  }

  public Collection<TypeReference> getExceptionTypes() {
    return Collections.emptySet();
  }

  public boolean hasDef() {
    return true;
  }

  public int getDef() {
    return lval;
  }

  public int getDef(int i) {
    if (Assertions.verifyAssertions) {
      assert i == 0;
    }

    return lval;
  }

  public int getNumberOfDefs() {
    return 1;
  }

  public int getNumberOfUses() {
    return (fieldVal == -1) ? 1 : 2;
  }

  public int getUse(int j) {
    if (j == 0) {
      return rval;
    } else if (j == 1 && fieldVal != -1) {
      return fieldVal;
    } else {
      Assertions.UNREACHABLE();
      return -1;
    }
  }

  public boolean isFallThrough() {
    return true;
  }

  public int hashCode() {
    return 3077 * fieldVal * rval;
  }

  public FieldReference getFieldRef() {
    return fieldRef;
  }
}
