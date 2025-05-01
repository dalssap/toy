package com.dalssap.toy.common.config

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import java.util.*


class YamlPropertySourceFactory: PropertySourceFactory {
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertiesPropertySource {
        val factory = YamlPropertiesFactoryBean().apply {
            setResources(resource.resource)
        }

        val properties = factory.`object` ?: Properties()

        return PropertiesPropertySource(
            name ?: resource.resource.filename ?: "yaml-property",
            properties
        )
    }
}