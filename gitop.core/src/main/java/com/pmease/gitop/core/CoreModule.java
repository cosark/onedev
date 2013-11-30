package com.pmease.gitop.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.hibernate.cfg.NamingStrategy;

import com.pmease.commons.hibernate.AbstractEntity;
import com.pmease.commons.hibernate.ModelProvider;
import com.pmease.commons.hibernate.PrefixedNamingStrategy;
import com.pmease.commons.jetty.ServletConfigurator;
import com.pmease.commons.loader.AbstractPlugin;
import com.pmease.commons.loader.AbstractPluginModule;
import com.pmease.commons.loader.ImplementationProvider;
import com.pmease.commons.util.ClassUtils;
import com.pmease.gitop.core.gatekeeper.MoreGateKeepers;
import com.pmease.gitop.core.manager.impl.DefaultStorageManager;
import com.pmease.gitop.model.ModelLocator;
import com.pmease.gitop.model.gatekeeper.GateKeeper;
import com.pmease.gitop.model.storage.StorageManager;

/**
 * NOTE: Do not forget to rename moduleClass property defined in the pom if you've renamed this class.
 *
 */
public class CoreModule extends AbstractPluginModule {

	@Override
	protected void configure() {
		super.configure();
		
		bind(NamingStrategy.class).toInstance(new PrefixedNamingStrategy("G"));
		
		contribute(ModelProvider.class, new ModelProvider() {

			@Override
			public Collection<Class<? extends AbstractEntity>> getModelClasses() {
				Collection<Class<? extends AbstractEntity>> modelClasses = 
						new HashSet<Class<? extends AbstractEntity>>();
				modelClasses.addAll(ClassUtils.findImplementations(AbstractEntity.class, ModelLocator.class));
				return modelClasses;
			}
			
		});
		
		contribute(ServletConfigurator.class, CoreServletConfigurator.class);
		
		contribute(ImplementationProvider.class, new ImplementationProvider() {
			
			@Override
			public Collection<Class<?>> getImplementations() {
				Collection<Class<?>> implementations = new ArrayList<>();
				for (Class<?> each: ClassUtils.findImplementations(GateKeeper.class, MoreGateKeepers.class)) 
					implementations.add(each);
				return implementations;
			}
			
			@Override
			public Class<?> getAbstractClass() {
				return GateKeeper.class;
			}
		});

		bind(StorageManager.class).to(DefaultStorageManager.class);
	}

	@Override
	protected Class<? extends AbstractPlugin> getPluginClass() {
		return Gitop.class;
	}

}
