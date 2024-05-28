package com.artistle.navkitexample.map

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline

class MainMapComponentImpl(
    componentContext: ComponentContext
) : ComponentContext by componentContext, MainMapComponent {

    private val _mapState: MutableValue<MainMapComponent.MapState> =
        MutableValue(MainMapComponent.MapState())

    private val drivingRouter by lazy {
        DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.ONLINE)
    }

    private val drivingOptions by lazy {
        DrivingOptions().apply {
            routesCount = 3
        }
    }

    private val vehicleOptions = VehicleOptions()

    override val mapState: Value<MainMapComponent.MapState> = _mapState

    override fun setCurrentLocation(longitude: Double, latitude: Double) {
        _mapState.update {
            it.copy(pointCurrent = Point(latitude, longitude))
        }
    }

    override fun setupRoute(polyline: Polyline) {
        _mapState.update {
            it.copy(road = polyline)
        }
    }


    override fun setupDistance(pointTarget: Point) {

        val requestPoints = _mapState.value.requestPoints.apply {
            add(RequestPoint(_mapState.value.pointCurrent, RequestPointType.WAYPOINT, null,null))
            add(RequestPoint(pointTarget, RequestPointType.WAYPOINT, null, null))
        }
        createDrivingRouteSession(requestPoints)
    }

    private fun createDrivingRouteSession(requestPoint: List<RequestPoint>) {
        drivingRouter.requestRoutes(
            requestPoint,
            drivingOptions,
            vehicleOptions,
            createDrivingRouteListener(onSuccessGetRout = { result ->
                setupRoute(result)
            })
        )
    }

    private fun createDrivingRouteListener(
        onSuccessGetRout: (Polyline) -> Unit,
        onFailureGetRoute: () -> Unit = {}
    ): DrivingSession.DrivingRouteListener {
        return object : DrivingSession.DrivingRouteListener {
            override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
                drivingRoutes.forEach { route ->
                    onSuccessGetRout.invoke(route.geometry)
                }
            }

            override fun onDrivingRoutesError(error: com.yandex.runtime.Error) {

            }
        }
    }
}