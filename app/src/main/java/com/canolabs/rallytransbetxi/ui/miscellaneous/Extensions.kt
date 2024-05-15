package com.canolabs.rallytransbetxi.ui.miscellaneous

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun Context.bitmapDescriptorFromVector(vectorResId: Int, scaleFactor: Float = 1f): BitmapDescriptor {
    val vectorDrawable = VectorDrawableCompat.create(resources, vectorResId, theme)
    vectorDrawable?.setBounds(0, 0, (vectorDrawable.intrinsicWidth * scaleFactor).toInt(), (vectorDrawable.intrinsicHeight * scaleFactor).toInt())
    val bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}