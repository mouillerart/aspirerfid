package org.ow2.aspirerfid.ide.bpwme.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.notation.View;
import org.ow2.aspirerfid.bpwme.CLCBProc;
import org.ow2.aspirerfid.bpwme.EBProc;
import org.ow2.aspirerfid.bpwme.diagram.edit.parts.OLCBProcEditPart;
import org.ow2.aspirerfid.bpwme.diagram.edit.parts.WorkflowMapEditPart;
import org.ow2.aspirerfid.bpwme.diagram.simpleditor.PathEditorInput;
import org.ow2.aspirerfid.bpwme.diagram.simpleditor.SimpleEditor;
import org.ow2.aspirerfid.bpwme.impl.OLCBProcImpl;
import org.ow2.aspirerfid.bpwme.provider.NewFakeListener;
import org.ow2.aspirerfid.commons.apdl.model.*;
import org.ow2.aspirerfid.commons.apdl.utils.DeserializerUtil;
import org.ow2.aspirerfid.commons.apdl.utils.Serializer;
import org.ow2.aspirerfid.ide.bpwme.ecspec.model.ExtraProperty;
import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.ECSpecBuilder;
import org.ow2.aspirerfid.ide.bpwme.ecspec.utils.LRSpecBuilder;
import org.ow2.aspirerfid.ide.bpwme.ecspec.views.ECSpecEditor;
import org.ow2.aspirerfid.ide.bpwme.test.FakeListener;


public class MainControl {
	public enum FileAction{
		NewAction, OpenAction
	}
	private static MainControl mainControl;
	//public static String apdlFilePath = "lgylym.xml";
	private static String fileSeparator =
		System.getProperty("file.separator");
	private static String userHome = System.getProperty("user.home");
	public static final String P_PE_ApdlFilesPath =
		userHome+fileSeparator+"AspireRFID"+fileSeparator+"IDE"+fileSeparator
		+"APDLs"+fileSeparator;
	
	private URI apdlURI;


	//public static URI apdlURI = URI.createURI("file:/C:/apdl.xml");
	public OLCBProc olcbProc;
	public EBProc selectedEbProc;
	public LRSpecBuilder lrsb;
	public ECSpecBuilder ecsb;
	public SimpleEditor simpleEditor;
	public ObjectFactory objectFactory;
	public HashMap<Integer, Object> objectMap;
	public enum EventType{OBJECT_EVENT,AGGREGATION_EVENT,QUANTITY_EVENT,TRANSACTION_EVENT};
	public HashMap<String, EventType> ebprocMap;
	public ECSpecEditor ecEditor;
	public WorkflowMapEditPart wme;
	public OLCBProcEditPart olcbep;
	public String assistantPath; //assistant file path
	
	public FakeListener fl;
	public FileAction fa;
	public Vector<ExtraProperty> extraLLRPProperty;
	public Vector<ExtraProperty> extraRPProperty;
	public Vector<ExtraProperty> extraHALProperty;



	public MainControl() throws Exception {
		//1. get olcb proc from file.

		//DeserializerUtil.deserializeOLCBProcFile(apdlFilePath);

		//2.connect file with simple editor and open editor view
		File directory = new File(P_PE_ApdlFilesPath);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		
		objectFactory = new ObjectFactory();
		objectMap = new HashMap<Integer, Object>();
		ebprocMap = new HashMap<String, EventType>();
		extraLLRPProperty = new Vector<ExtraProperty>();
		extraRPProperty = new Vector<ExtraProperty>();
		extraHALProperty = new Vector<ExtraProperty>();


		extraLLRPProperty.add(new ExtraProperty("ConnectionPointAddress",ExtraProperty.LLRP_TYPE));
		extraLLRPProperty.add(new ExtraProperty("ConnectionPointPort",ExtraProperty.LLRP_TYPE));
		extraLLRPProperty.add(new ExtraProperty("PhysicalReaderSource",ExtraProperty.LLRP_TYPE));
		extraLLRPProperty.add(new ExtraProperty("RoSpecID",ExtraProperty.LLRP_TYPE));

		extraRPProperty.add(new ExtraProperty("ConnectionPointAddress",ExtraProperty.RP_TYPE));
		extraRPProperty.add(new ExtraProperty("ConnectionPointPort",ExtraProperty.RP_TYPE));
		extraRPProperty.add(new ExtraProperty("PhysicalReaderSource",ExtraProperty.RP_TYPE));
		extraRPProperty.add(new ExtraProperty("RoSpecID",ExtraProperty.RP_TYPE));

		//simpleEditor = SimpleEditor.getEditor();	
	}

	public OLCBProc createOLCBProc() {
		//1. create object

		olcbProc = objectFactory.createOLCBProc();
		//
		//System.out.println(olcbProc);
		//2. save it to the file
		saveObject();
		return olcbProc;
		//3. refresh the editor
		//SimpleEditor.getEditor();
		//SimpleEditor.refresh();
	}

	public void setAPDLFileName(URI diagramFileURI) {
		String dot = "\\.";
		String[] names = diagramFileURI.lastSegment().split(dot);
		if(names.length == 2) {
			apdlURI = URI.createFileURI(P_PE_ApdlFilesPath+names[0]+".xml");
			File f = new File(apdlURI.toFileString());		
			if(!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			IPath location= new Path(f.getAbsolutePath());
			PathEditorInput input= new PathEditorInput(location);
			SimpleEditor.setEditorInput(input);
			
			URI apdlAssistant = URI.createFileURI(P_PE_ApdlFilesPath+names[0]+".assistant.xml");
			assistantPath = apdlAssistant.toFileString();
			System.out.println("assistant:" + assistantPath);
			//return names[0];
		} else {
			System.err.println("Error in MainControl.getFileName");
			//return null;
		}
	}
	
	//rebuild the in memory models from the existing apdl file
	public void rebuild() {
		FileInputStream inputStream;
		try {
			MainControl mc = MainControl.getMainControl();
			inputStream = new FileInputStream(mc.getApdlURI().toFileString());
			//System.out.println(apdlURI);
			//DeserializerUtil.deserializeOLCBProc(inputStream);
			mc.olcbProc = DeserializerUtil.deserializeOLCBProc(inputStream);
			inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//new ReadFromFile().start();
	}
	
	public org.ow2.aspirerfid.commons.apdl.model.CLCBProc createCLCBProc() {
		org.ow2.aspirerfid.commons.apdl.model.CLCBProc clcbProc= objectFactory.createCLCBProc();
		olcbProc.getCLCBProc().add(clcbProc);
		saveObject();
		return clcbProc;
	}

	public org.ow2.aspirerfid.commons.apdl.model.EBProc createEBProc(org.ow2.aspirerfid.commons.apdl.model.CLCBProc clcb) {
		org.ow2.aspirerfid.commons.apdl.model.EBProc ebproc = objectFactory.createEBProc();
		clcb.getEBProc().add(ebproc);
		ebproc.setApdlDataFields(objectFactory.createApdlDataFields());
		saveObject();
		return ebproc;
	}

	public boolean removeCLCBProc(org.ow2.aspirerfid.commons.apdl.model.CLCBProc clcb) {
		boolean result = olcbProc.getCLCBProc().remove(clcb);
		saveObject();
		return result;
	}

	public void saveObject() {
		new WriteToFile(olcbProc).start();
	}

	//map the diagram to the logical model (both in memory)
	public void mapModels() {
		//1. from diagram get the logical model
		OLCBProcImpl opi = (OLCBProcImpl)((View)olcbep.getModel()).getElement();
		//2. from apdl file get the real model
		OLCBProc op = olcbProc;
		OLCBProcAssistant oa = new OLCBProcAssistant(op);
		//3. do the model mapping by id
		objectMap.clear();
		if(!opi.getId().equals(op.getId())) {
			System.err.println("Wrong Mapping in MC.mapModels()");
			return;
		}
		addMap(opi.hashCode(), op);
		for(CLCBProc cpi : opi.getCLCBProc()) {
			org.ow2.aspirerfid.commons.apdl.model.CLCBProc cp;
			if((cp = oa.getCLCB(cpi.getId())) != null) {
				addMap(cpi.hashCode(), cp);
				for(EBProc ebi : cpi.getEBProc()) {
					org.ow2.aspirerfid.commons.apdl.model.EBProc ep;
					if((ep = oa.getEBProc(cp, ebi.getId())) != null) {
						addMap(ebi.hashCode(), ep);
					}else {
						System.err.println("Wrong Mapping in MC.mapModels()");
						return;
					}
				}
			}else {
				System.err.println("Wrong Mapping");
				return;
			}
		}
	}
	
	
	public OLCBProc getOLCBProc() {
		return olcbProc;
	}

	public void addMap(int hashCode, Object object) {
		objectMap.put(hashCode, object);
	}
	public void delMap(int hashCode) {
		objectMap.remove(hashCode);
	}
	public Object getMapObject(int hashCode) {
		return objectMap.get(hashCode);
	}

	public void getSimpleEditor() {
		SimpleEditor.getEditor();
	}
	//save the OLCB Process to the apdl file

	public static MainControl getMainControl() {
		if(mainControl == null) {
			try {
				mainControl = new MainControl();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mainControl;
	}

	public void setSelectedEBProc() {

	}
	
	public URI getApdlURI() {
		return apdlURI;
	}
	
	public void saveAssistantFile() {
		try {
			FileOutputStream fout = new FileOutputStream(assistantPath);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(ebprocMap);
			oos.close();
		}
		catch (Exception e) { e.printStackTrace();
		} 
	}
	
	public void loadAssistantFile() {
		try {
			FileInputStream fin = new FileInputStream(assistantPath);
			ObjectInputStream ois = new ObjectInputStream(fin);
			ebprocMap = (HashMap<String, EventType>) ois.readObject();
			ois.close();
		}
		catch (Exception e) {
			e.printStackTrace(); 		
		}
	}
	
}

//a new thread to write the OLCB Process to file
//needed because it may take some time
class WriteToFile extends Thread {
	OLCBProc olcbProc;
	public WriteToFile(OLCBProc olcbProc) {
		this.olcbProc = olcbProc;
	}
	@Override
	public void run() {
		super.run();
		FileWriter fw;
		try {
			MainControl mc = MainControl.getMainControl();
			fw = new FileWriter(mc.getApdlURI().toFileString());
			Serializer.serializeOLCBProc(olcbProc, fw);
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class ReadFromFile extends Thread {
	@Override
	public void run() {
		super.run();
		FileInputStream inputStream;
		try {
			MainControl mc = MainControl.getMainControl();
			inputStream = new FileInputStream(mc.getApdlURI().toFileString());
			//System.out.println(apdlURI);
			//DeserializerUtil.deserializeOLCBProc(inputStream);
			mc.olcbProc = DeserializerUtil.deserializeOLCBProc(inputStream);
			inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
