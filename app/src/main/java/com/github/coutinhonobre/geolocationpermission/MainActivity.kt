package com.github.coutinhonobre.geolocationpermission

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val locationPermissionRequestCode = 100
    private var locationPermissionDeniedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener {
            // Check the location permission
            checkPermission()
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                locationPermissionDeniedCount++
                if (locationPermissionDeniedCount >= 3) {
                    // Show a dialog explaining the need for the location permission
                    showLocationPermissionRationaleDialog()
                } else {
                    // Request the location permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        locationPermissionRequestCode
                    )
                }
            } else {
                // Request the location permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionRequestCode
                )
            }
        } else {
            // Location permission has been granted, start the location service
        }
    }

    private fun showLocationPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permissão de localização é necessária")
            .setMessage("Esta permissão é necessária para fornecer a localização do usuário. Você pode habilitá-la nas configurações do aplicativo.")
            .setPositiveButton("Configurações") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
                startActivity(intent)
            }
            .setNegativeButton("Cancelar", { dialog, _ -> dialog.dismiss() })
            .create()
            .show()
    }
}
