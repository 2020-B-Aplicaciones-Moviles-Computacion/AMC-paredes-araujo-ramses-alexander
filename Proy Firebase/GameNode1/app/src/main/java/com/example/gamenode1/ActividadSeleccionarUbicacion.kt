package com.example.gamenode1

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class ActividadSeleccionarUbicacion : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveStartedListener,
    GoogleMap.OnCameraIdleListener,
    GoogleMap.OnPolylineClickListener,
    GoogleMap.OnPolygonClickListener,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener
{
    lateinit var marcador: Marker
    private lateinit var mMap: GoogleMap
    lateinit var textoUbicacion:TextView
    var locacionActual = com.google.android.gms.maps.model.LatLng(-0.2102193187687271, -78.48855629390985)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_seleccionar_ubicacion)
        textoUbicacion = findViewById<TextView>(R.id.tv_ubicacion_selecta)
        solicitarPermisos()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment;

        mapFragment.getMapAsync(this);

        if (AuthUsuario.locacion==null){
            textoUbicacion.setText(locacionActual.toString())
        }else{
            locacionActual= AuthUsuario.locacion!!
            textoUbicacion.setText(locacionActual.toString())
        }



        val btnSeleccionarUbicacion = findViewById<Button>(R.id.bt_sel_ubicacion)
        btnSeleccionarUbicacion.setOnClickListener{
            AuthUsuario.locacion = marcador?.position
            textoUbicacion.setText(marcador?.position.toString())
            Toast.makeText(this, "Ubicacion actualizada con exito!! \nPorfavor Espere.", Toast.LENGTH_SHORT).show()
            irActividad(ActividadMenuPrincipal::class.java)
        }


        // onMarkerDragEnd onMarkerDragStart onMarkerDrag

    }

    override fun onMapReady(gooMap: GoogleMap?) {
        if (gooMap!=null){
            mMap = gooMap
            establecerConfiguracion(gooMap)

            val titulo = "You"
            marcador = mMap.addMarker(
                MarkerOptions().
                position(locacionActual)
                    .title(titulo)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            mMap.setOnMarkerDragListener(this@ActividadSeleccionarUbicacion)



            moverCamaraConZoom(locacionActual)
            // onMarkerDragEnd onMarkerDragStart onMarkerDrag


        }

    }
    fun moverCamaraConZoom(latLng: LatLng,zoom: Float = 12f){
        mMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(latLng,zoom)
        )

    }
    fun establecerConfiguracion(mapa:GoogleMap){
        val contexto = this.applicationContext
        with(mapa){
            val permisosFineLocation = ContextCompat
                .checkSelfPermission(
                    contexto,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            val tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
            if (tienePermisos){
                mapa.isMyLocationEnabled = true
            }
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
    }
    fun solicitarPermisos(){
        val permisosFineLocation = ContextCompat.checkSelfPermission(
            this.applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        var tienePermisos = permisosFineLocation == PackageManager.PERMISSION_GRANTED
        if (tienePermisos){
            Log.i("mapa","Tiene permisos")
            tienePermisos=true
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }
    }


    override fun onCameraMove() {
        Log.i("mapa","onCameraMove")
    }

    override fun onCameraMoveStarted(p0: Int) {
        Log.i("mapa","onCameraMoveStarted")
    }

    override fun onCameraIdle() {
        Log.i("mapa","onCameraIdle")
    }

    override fun onPolylineClick(p0: Polyline?) {
        Log.i("mapa","onPolylineClick")
    }

    override fun onPolygonClick(p0: Polygon?) {
        Log.i("mapa","onPolygonClick")
    }

    override fun onMarkerDragEnd(marcador: Marker?) {
        /*Log.i("ubicacion onMarkerDragEnd","${marcador?.getPosition()}")
        textoUbicacion.setText(marcador?.position.toString())*/
        textoUbicacion.setText(marcador?.position.toString())
    }

    override fun onMarkerDragStart(marcadorr: Marker?) {

    }

    override fun onMarkerDrag(marcadorx: Marker?) {
        textoUbicacion.setText(marcador?.position.toString())
    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        return false
    }
    fun irActividad(
        clase: Class<*>,
        parametros: ArrayList<Pair<String,*>>? = null,
        codigo:Int? = null
    ){
        val intentExplicito = Intent(
            this,
            clase
        )
        //FOR
        Log.i("intent-explicito","Se envia: ${parametros}")
        if(parametros!=null){
            parametros.forEach{
                var nombreVariable = it.first
                var valorVariable = it.second

                var tipoDato = false
                tipoDato = it.second is String
                if (tipoDato){
                    intentExplicito.putExtra(nombreVariable.toString(),valorVariable as String)
                }
                tipoDato = it.second is Int
                if(tipoDato){
                    intentExplicito.putExtra(nombreVariable.toString(),valorVariable as Int)
                }
                tipoDato = it.second is Parcelable
                if(tipoDato){
                    intentExplicito.putExtra(nombreVariable.toString(),valorVariable as Parcelable)
                }
            }
        }
        if (codigo!=null){
            startActivityForResult(intentExplicito,codigo)
        }else{
            startActivity(intentExplicito)
        }


    }
}