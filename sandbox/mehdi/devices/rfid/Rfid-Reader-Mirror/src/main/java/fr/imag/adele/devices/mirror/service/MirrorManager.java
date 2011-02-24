package fr.imag.adele.devices.mirror.service;

import java.util.Map;

public interface MirrorManager {

	public Map<String, MirrorDescriptor> getListMirror();

	public MirrorDescriptor getMirrorbyID(String i);
}
