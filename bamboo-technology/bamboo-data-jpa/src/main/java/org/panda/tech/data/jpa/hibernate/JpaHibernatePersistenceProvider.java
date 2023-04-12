package org.panda.tech.data.jpa.hibernate;

import org.hibernate.boot.Metadata;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.springframework.orm.jpa.persistenceunit.SmartPersistenceUnitInfo;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于Hibernate的JPA持久化提供者
 */
public class JpaHibernatePersistenceProvider extends HibernatePersistenceProvider implements MetadataProvider {

    private Metadata metadata;

    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info,
            Map properties) {
        List<String> mergedClassesAndPackages = new ArrayList<>(info.getManagedClassNames());
        if (info instanceof SmartPersistenceUnitInfo) {
            mergedClassesAndPackages.addAll(((SmartPersistenceUnitInfo) info).getManagedPackages());
        }
        EntityManagerFactoryBuilderImpl builder = new EntityManagerFactoryBuilderImpl(
                new PersistenceUnitInfoDescriptor(info) {
                    @Override
                    public List<String> getManagedClassNames() {
                        return mergedClassesAndPackages;
                    }
                }, properties);
        EntityManagerFactory factory = builder.build();
        // build()之后才有元数据
        this.metadata = builder.getMetadata();
        return factory;
    }

    @Override
    public Metadata getMetadata() {
        return this.metadata;
    }

}
