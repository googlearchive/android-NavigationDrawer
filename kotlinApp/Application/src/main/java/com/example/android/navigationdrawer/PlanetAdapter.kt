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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Adapter for the planet data used in our drawer menu.
 */
class PlanetAdapter(
        private val dataset: Array<String>,
        private val listener: OnItemClickListener
) : RecyclerView.Adapter<PlanetAdapter.ViewHolder>() {

    /**
     * Interface for receiving click events from cells.
     */
    interface OnItemClickListener {
        fun onClick(view: View, position: Int)
    }

    /**
     * Custom [ViewHolder] for our planet views.
     */
    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.drawer_list_item, parent, false)
                .findViewById(android.R.id.text1))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            textView.text = dataset[position]
            textView.setOnClickListener { view -> listener.onClick(view, position) }
        }
    }

    override fun getItemCount() = dataset.size
}
