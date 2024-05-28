package com.artistle.navkitexample.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.artistle.navkitexample.map.MainMapComponent
import com.artistle.navkitexample.map.MainMapComponentImpl
import kotlinx.serialization.Serializable

class DefaultRootComponent(
    componentContext: ComponentContext
): RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()


    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Map,
        handleBackButton = true,
        childFactory = ::child,
    )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Map -> RootComponent.Child.MapChild(mapComponent(componentContext))
        }

    private fun mapComponent(componentContext: ComponentContext): MainMapComponent {
        return MainMapComponentImpl(componentContext)
    }

    override fun onBackClicked(toIndex: Int) {
        TODO()
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Map : Config
    }
}