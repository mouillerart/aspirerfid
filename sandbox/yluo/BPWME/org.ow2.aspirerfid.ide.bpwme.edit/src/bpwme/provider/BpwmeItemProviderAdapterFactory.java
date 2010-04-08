/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package bpwme.provider;

import bpwme.util.BpwmeAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BpwmeItemProviderAdapterFactory extends BpwmeAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BpwmeItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
		
	}

	/**
	 * This keeps track of the one adapter used for all {@link bpwme.WorkflowMap} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WorkflowMapItemProvider workflowMapItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.WorkflowMap}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createWorkflowMapAdapter() {
		if (workflowMapItemProvider == null) {
			workflowMapItemProvider = new WorkflowMapItemProvider(this);
		}

		return workflowMapItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link bpwme.Connection} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectionItemProvider connectionItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.Connection}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createConnectionAdapter() {
		if (connectionItemProvider == null) {
			connectionItemProvider = new ConnectionItemProvider(this);
		}

		return connectionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link bpwme.OLCBProc} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OLCBProcItemProvider olcbProcItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.OLCBProc}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createOLCBProcAdapter() {
		if (olcbProcItemProvider == null) {
			olcbProcItemProvider = new OLCBProcItemProvider(this);
		}

		return olcbProcItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link bpwme.CLCBProc} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CLCBProcItemProvider clcbProcItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.CLCBProc}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCLCBProcAdapter() {
		if (clcbProcItemProvider == null) {
			clcbProcItemProvider = new CLCBProcItemProvider(this);
		}

		return clcbProcItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link bpwme.EBProc} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EBProcItemProvider ebProcItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.EBProc}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEBProcAdapter() {
		if (ebProcItemProvider == null) {
			ebProcItemProvider = new EBProcItemProvider(this);
		}

		return ebProcItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link bpwme.Transitions} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransitionsItemProvider transitionsItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.Transitions}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTransitionsAdapter() {
		if (transitionsItemProvider == null) {
			transitionsItemProvider = new TransitionsItemProvider(this);
		}

		return transitionsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link bpwme.Transition} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransitionItemProvider transitionItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.Transition}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTransitionAdapter() {
		if (transitionItemProvider == null) {
			transitionItemProvider = new TransitionItemProvider(this);
		}

		return transitionItemProvider;
	}






	/**
	 * This keeps track of the one adapter used for all {@link bpwme.Condition} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConditionItemProvider conditionItemProvider;

	/**
	 * This creates an adapter for a {@link bpwme.Condition}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createConditionAdapter() {
		if (conditionItemProvider == null) {
			conditionItemProvider = new ConditionItemProvider(this);
		}

		return conditionItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (workflowMapItemProvider != null) workflowMapItemProvider.dispose();
		if (connectionItemProvider != null) connectionItemProvider.dispose();
		if (olcbProcItemProvider != null) olcbProcItemProvider.dispose();
		if (clcbProcItemProvider != null) clcbProcItemProvider.dispose();
		if (ebProcItemProvider != null) ebProcItemProvider.dispose();
		if (transitionsItemProvider != null) transitionsItemProvider.dispose();
		if (transitionItemProvider != null) transitionItemProvider.dispose();
		if (conditionItemProvider != null) conditionItemProvider.dispose();
	}

}
