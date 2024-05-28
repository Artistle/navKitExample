import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.artistle.navkitexample.map.MainMapComponent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView

@ExperimentalPermissionsApi
@Composable
fun CreateMapView(
    mainMapComponent: MainMapComponent,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    val model by mainMapComponent.mapState.subscribeAsState()

    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Box {
        AndroidView(
            modifier = modifier,
            factory = { context ->
                MapView(context).apply {
                    mapWindow.map.addInputListener(object : InputListener {
                        override fun onMapTap(p0: Map, p1: Point) {
                            mainMapComponent.setupDistance(p1)
                        }

                        override fun onMapLongTap(p0: Map, p1: Point) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            },
            update = { view ->
                view.mapWindow.map.move(
                    CameraPosition(
                        model.pointCurrent,
                        model.zoom,
                        model.azimuth,
                        model.tilt
                    )
                )
                model.road?.let { route ->
                    view.mapWindow.map.mapObjects.addPolyline().apply {
                        geometry = route
                    }
                }

            },
        )
        IconButton(modifier = Modifier.background(Color.White, shape = CircleShape).align(Alignment.CenterEnd), onClick = {

            if (locationPermissionState.allPermissionsGranted) {

                fusedLocationClient.lastLocation.addOnSuccessListener {
                    mainMapComponent.setCurrentLocation(it.longitude, it.latitude)
                }
            } else {
                locationPermissionState.launchMultiplePermissionRequest()
            }

        }) {
            Icon(
                Icons.Rounded.LocationOn,
                contentDescription = "find my location"
            )
        }
    }
}