package com.artistle.navkitexample.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.artistle.navkitexample.map.MainMapComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    sealed class Child {
        class MapChild(val component: MainMapComponent) : Child()
    }
}