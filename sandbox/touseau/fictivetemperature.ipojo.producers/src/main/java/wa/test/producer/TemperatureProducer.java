package wa.test.producer;

import org.osgi.framework.BundleContext;
import org.osgi.service.wireadmin.Producer;
import org.osgi.service.wireadmin.Wire;
import org.osgi.util.measurement.Measurement;
import org.osgi.util.measurement.Unit;

public class TemperatureProducer implements Producer {

	/**
	 * Wires connected to this producer
	 */
	private Wire[] m_wires;
	
	/**
	 * The flavors injected by the iPOJO setFlavors callback method
	 */
	private Class[] m_flavors; 
	
	private BundleContext m_bundleContext;
	
	/**
	 * Configured and injected by each iPOJO instance
	 */
	private String m_PID;

	
	private int m_measurementCount = 0;
	
	/**
	 * configured and injected by each iPOJO instance
	 */
	private boolean m_constant;

	public TemperatureProducer (BundleContext context){
		m_bundleContext = context;
	}
	
	
	public void setFlavors (String[] flavors){
		Class[] temp = new Class[flavors.length];
		for (int i=0; i < flavors.length; i++){
			try {
				temp[i] = m_bundleContext.getBundle().loadClass(flavors[i]);
			} catch (ClassNotFoundException e) {
				System.err.println("Class not found: "+ flavors[i]);
			}
		}
		m_flavors = temp;
	}
	
	public void consumersConnected(Wire[] wires) {
		m_wires = wires;
	}
	

	/* (non-Javadoc)
	 * @see org.osgi.service.wireadmin.Producer#polled(org.osgi.service.wireadmin.Wire)
	 */
	public Object polled(Wire wire) {
		System.out.println("Temperature Producer "+m_PID+" has been polled");
		if (m_constant){
			return new Measurement(20+273.15, Unit.K);
		}
		else { // temperature evolution is not constant
			return getIntoLevelTemperature(10.0);
		}
	}
	
	private Measurement getIntoLevelTemperature(double value) {
		
		double randomValue = Math.random() * value;
		if (Math.random()>0.5)
			randomValue = -1 * randomValue;
		return new Measurement(20+273.15 + randomValue, Unit.K);

	}
	
}
