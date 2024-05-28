package com.artistle.navkitexample.root

import CreateMapView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.yandex.mapkit.mapview.MapView

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.MapChild -> CreateMapView(
                mainMapComponent = child.component
            )
        }
    }
}