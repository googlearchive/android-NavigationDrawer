/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawer

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import java.util.Locale

/**
 * Fragment that appears in the "content_frame" and shows a planet.
 */
class PlanetFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val planetNumber = arguments.getInt(ARG_PLANET_NUMBER)
        val planet = resources.getStringArray(R.array.planets_array)[planetNumber]
        val imageId = resources.getIdentifier(planet.toLowerCase(Locale.getDefault()),
                "drawable", activity.packageName)
        activity.title = planet

        return inflater.inflate(R.layout.fragment_planet, container, false).apply {
            findViewById<ImageView>(R.id.image).setImageResource(imageId)
        }
    }

    companion object {
        private val ARG_PLANET_NUMBER = "planet_number"

        fun newInstance(position: Int) = PlanetFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_PLANET_NUMBER, position)
            }
        }
    }
}