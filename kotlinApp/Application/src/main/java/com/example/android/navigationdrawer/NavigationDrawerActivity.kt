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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

/**
 * This example illustrates a common usage of [DrawerLayout] in the Android support library.
 *
 * When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:
 *
 *  * **View switches**. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.
 *
 *  * **Selective Up**. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.
 *
 * Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.
 */
class NavigationDrawerActivity : Activity(), PlanetAdapter.OnItemClickListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawerList: RecyclerView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var newTitle: CharSequence
    private lateinit var planetTitles: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)

        val drawerTitle = title
        newTitle = title
        planetTitles = resources.getStringArray(R.array.planets_array)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout).apply {
            setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START)
        }

        drawerList = findViewById<RecyclerView>(R.id.left_drawer).apply {
            // Improve performance by indicating the list if fixed size.
            setHasFixedSize(true)
            // Set up the drawer's list view with items and click listener.
            adapter = PlanetAdapter(planetTitles, this@NavigationDrawerActivity)
        }

        // Enable ActionBar app icon to behave as action to toggle nav drawer.
        actionBar.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon.
        drawerToggle = object : ActionBarDrawerToggle(
                this, /* Host Activity */
                drawerLayout, /* DrawerLayout object */
                R.string.drawer_open, /* "Open drawer" description for accessibility */
                R.string.drawer_close  /* "Close drawer" description for accessibility */
        ) {

            override fun onDrawerClosed(drawerView: View) {
                actionBar.title = newTitle
                invalidateOptionsMenu() // Creates call to onPrepareOptionsMenu().
            }

            override fun onDrawerOpened(drawerView: View) {
                actionBar.title = drawerTitle
                invalidateOptionsMenu() // Creates call to onPrepareOptionsMenu().
            }
        }

        // Set a custom shadow that overlays the main content when the drawer opens.
        drawerLayout.addDrawerListener(drawerToggle)

        if (savedInstanceState == null) selectItem(0)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    /**
     * Called whenever we call [invalidateOptionsMenu].
     * If the nav drawer is open, hide action items related to the content view.
     */
    override fun onPrepareOptionsMenu(menu: Menu) =
            super.onPrepareOptionsMenu(menu.apply {
                findItem(R.id.action_websearch).isVisible = !drawerLayout.isDrawerOpen(drawerList)
            })

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        // Handle action buttons
        return when (item.itemId) {
            R.id.action_websearch -> {
                // Create intent to perform web search for this planet.
                val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                    putExtra(SearchManager.QUERY, actionBar.title)
                }
                // Catch event that there's no activity to handle intent.
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* The click listener for RecyclerView in the navigation drawer. */
    override fun onClick(view: View, position: Int) {
        selectItem(position)
    }

    override fun setTitle(title: CharSequence) {
        newTitle = title
        actionBar.title = title
    }

    /**
     * If [ActionBarDrawerToggle] is used, it must be called in [onPostCreate] and
     * [onConfigurationChanged].
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after has occurred.
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Pass any configuration change to the drawer toggle.
        drawerToggle.onConfigurationChanged(newConfig)
    }

    @SuppressLint("CommitTransaction") // commit() is called
    private fun selectItem(position: Int) {
        fragmentManager.beginTransaction().run {
            replace(R.id.content_frame, PlanetFragment.newInstance(position))
            commit()
        }
        title = planetTitles[position]
        drawerLayout.closeDrawer(drawerList)
    }

}
