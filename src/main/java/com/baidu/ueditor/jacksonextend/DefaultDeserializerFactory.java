//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.ueditor.jacksonextend;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.deser.DeserializerFactory;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultDeserializerFactory extends BeanDeserializerFactory {
    private static final long serialVersionUID = 1L;
    public static final DefaultDeserializerFactory instance = new DefaultDeserializerFactory(new DeserializerFactoryConfig());

    public DefaultDeserializerFactory(DeserializerFactoryConfig config) {
        super(config);
    }

    @Override
    protected CollectionType _mapAbstractCollectionType(JavaType type, DeserializationConfig config) {
        Class<?> collectionClass = type.getRawClass();
        if (collectionClass.equals(Set.class)) {
            collectionClass = LinkedHashSet.class;
            return (CollectionType)config.constructSpecializedType(type, collectionClass);
        } else {
            return super._mapAbstractCollectionType(type, config);
        }
    }

	@Override
    public DeserializerFactory withConfig(DeserializerFactoryConfig config) {
        return new DefaultDeserializerFactory(config);
    }
}
