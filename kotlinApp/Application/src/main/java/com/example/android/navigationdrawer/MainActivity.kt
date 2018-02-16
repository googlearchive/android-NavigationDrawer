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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView

/**
 * A simple launcher activity offering access to the individual samples in this project.
 */
class MainActivity : Activity(), AdapterView.OnItemClickListener {

    private lateinit var samples: Array<Sample>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Prepare list of samples in this dashboard.
        samples = arrayOf(Sample(R.string.navigationdraweractivity_title,
                R.string.navigationdraweractivity_description,
                Intent(this, NavigationDrawerActivity::class.java)))

        // Prepare the GridView.
        findViewById<GridView>(android.R.id.list).run {
            adapter = SampleAdapter()
            onItemClickListener = this@MainActivity
        }
    }

    override fun onItemClick(container: AdapterView<*>, view: View, position: Int, id: Long) {
        startActivity(samples[position].intent)
    }

    private inner class SampleAdapter : BaseAdapter() {

        override fun getCount() = samples.size

        override fun getItem(position: Int) = samples[position]

        override fun getItemId(position: Int) = samples[position].hashCode().toLong()

        override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
            return (convertView ?: layoutInflater.inflate(R.layout.sample_dashboard_item,
                    container, false)).apply {
                        findViewById<TextView>(android.R.id.text1)?.setText(
                                samples[position].titleResId)
                        findViewById<TextView>(android.R.id.text2)?.setText(
                                samples[position].descriptionResId)
                    }
        }

    }

}
