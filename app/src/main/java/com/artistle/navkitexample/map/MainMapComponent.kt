package com.artistle.navkitexample.map

import com.arkivanov.decompose.value.Value
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline

interface MainMapComponent {

    val mapState: Value<MapState>

    fun setCurrentLocation(longitude: Double, latitude: Double)

    fun setupDistance(pointTarget: Point)

    fun setupRoute(polyline: Polyline)

    data class MapState(
        val pointCurrent: Point = Point(43.5877,39.7155),
        val pointTo: Point = Point(43.5840,39.7201),
        val pointFrom: Point = pointCurrent,
        val addressTo: String = "",
        val addressFrom: String = "",
        val requestPoints: ArrayList<RequestPoint> = arrayListOf(),
        val navigationPoints: ArrayList<RequestPoint> = arrayListOf(),
        val road:Polyline? = null,
        val zoom: Float = DEFAULT_ZOOM,
        val azimuth: Float = DEFAULT_AZIMUTH,
        val tilt: Float = DEFAULT_TILT
    ) {

        private companion object {

            private val DEFAULT_POINT = Point(55.7522,37.6156) //MOSCOW
            private const val DEFAULT_ZOOM = 19f
            private const val DEFAULT_AZIMUTH = 150f
            private const val DEFAULT_TILT = 30f
        }
    }
}