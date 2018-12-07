package com.cuixbo.learn.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginDemo implements Plugin<Project> {
    def extensionHencoder = 'hencoder'

    @Override
    void apply(Project project) {
        def extension = project.extensions.create(extensionHencoder, ExtensionDemo)
        project.afterEvaluate {
            println "PluginDemo.apply ${extension.name}"
        }
    }
}
