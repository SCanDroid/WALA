package com.ibm.wala.cast.loader;

import com.ibm.wala.cast.ir.translator.*;
import com.ibm.wala.cast.tree.*;
import com.ibm.wala.cast.tree.impl.*;
import com.ibm.wala.cast.util.*;
import com.ibm.wala.classLoader.*;
import com.ibm.wala.ipa.cha.*;
import com.ibm.wala.types.*;
import com.ibm.wala.util.debug.*;

import java.io.*;
import java.net.*;
import java.util.*;

public abstract class CAstAbstractNativeLoader extends CAstAbstractLoader {
    
  public CAstAbstractNativeLoader(IClassHierarchy cha, IClassLoader parent) {
    super(cha, parent);
  }

  public CAstAbstractNativeLoader(IClassHierarchy cha) {
    this(cha, null);
  }

  protected abstract NativeTranslatorToCAst
    getTranslatorToCAst(CAst ast, URL sourceURL, String localFileName);

  protected abstract TranslatorToIR initTranslator();

  protected void finishTranslation() {

  }

  public void init(final Set modules) {
    final CAst ast = new CAstImpl();

    final TranslatorToIR xlatorToIR = initTranslator();

    class TranslatorNestingHack {

      private void init(ModuleEntry moduleEntry) {
        if (moduleEntry.isModuleFile()) {
          init(moduleEntry.asModule());
        } else if (moduleEntry instanceof SourceFileModule) {
          File f = ((SourceFileModule) moduleEntry).getFile();
          String fn = f.toString();

          try {
            NativeTranslatorToCAst xlatorToCAst = 
	      getTranslatorToCAst(ast, new URL("file://" + f), fn);

            CAstEntity fileEntity = xlatorToCAst.translateToCAst();

            Trace.println(CAstPrinter.print(fileEntity));

            xlatorToIR.translate(fileEntity, fn);
          } catch (MalformedURLException e) {
            Trace.println("unpected problems with " + f);
            Assertions.UNREACHABLE();
          }

        } else if (moduleEntry instanceof SourceURLModule) {
          java.net.URL url = ((SourceURLModule) moduleEntry).getURL();
          String fileName = ((SourceURLModule) moduleEntry).getName();
          String localFileName = fileName.replace('/', '_');

          try {
            File F = TemporaryFile.streamToFile(localFileName,
                ((SourceURLModule) moduleEntry).getInputStream());

            final NativeTranslatorToCAst xlatorToCAst =
		getTranslatorToCAst(ast, url, localFileName);

            CAstEntity fileEntity = xlatorToCAst.translateToCAst();

            Trace.println(CAstPrinter.print(fileEntity));

            xlatorToIR.translate(fileEntity, fileName);

            F.delete();
          } catch (IOException e) {
            Trace.println("unpected problems with " + fileName);
            Assertions.UNREACHABLE();
          }
        }
      }

      private void init(Module module) {
        for (Iterator mes = module.getEntries(); mes.hasNext();) {
          init((ModuleEntry) mes.next());
        }
      }

      private void init() {
        for (Iterator mes = modules.iterator(); mes.hasNext();) {
          init((Module) mes.next());
        }
      }
    }

    (new TranslatorNestingHack()).init();

    for (Iterator ts = types.keySet().iterator(); ts.hasNext();) {
      TypeName tn = (TypeName) ts.next();
      try {
        Trace.println("found type " + tn + " : " + types.get(tn) + " < "
            + ((IClass) types.get(tn)).getSuperclass());
      } catch (Exception e) {
        System.err.println(e);
      }
    }

    finishTranslation();
  }

}