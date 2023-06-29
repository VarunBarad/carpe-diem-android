package com.varunbarad.carpediem.android

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class CarpeDiemApplication : Application() {
	override fun onCreate() {
		super.onCreate()

		AndroidThreeTen.init(this)
	}
}
