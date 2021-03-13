package com.mayandro.bitcointrend

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.espresso.contrib.RecyclerViewActions.*
import com.mayandro.bitcointrend.ui.home.HomeActivity
import com.mayandro.bitcointrend.ui.home.dashboard.adapter.StatsAdapter
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class DashboardFragmentTest {
    @get: Rule
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun checkIfScreenIsPresent() {
        Espresso.onView(withId(R.id.textScreenTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun clickProfileCard() {
        Espresso.onView(withId(R.id.cardProfileImageContainer)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.buttonPositive)).perform(ViewActions.click())
    }

    @Test
    fun clickSearchCard() {
        Espresso.onView(withId(R.id.imageSearch)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.buttonPositive)).perform(ViewActions.click())
    }

    @Test
    fun clickInfoButton() {
        Espresso.onView(withId(R.id.imageSearch)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.buttonPositive)).perform(ViewActions.click())
    }

    @Test
    fun swipeStatsCard() {
        Espresso.onView(withId(R.id.cardStat)).perform(swipeUp())

        Espresso.onView(withId(R.id.recyclerViewStats)).perform(actionOnItemAtPosition<StatsAdapter.ViewHolder>(1, ViewActions.click()))

        Espresso.onView(withId(R.id.recyclerViewStats)).perform(swipeUp())

        Espresso.onView(withId(R.id.fabDownload)).perform(ViewActions.click())
    }

    @Test
    fun statSelectorFilter() {
        Espresso.onView(withId(R.id.textStatsLabel)).perform(ViewActions.click())

        Espresso.onView(withId(R.id.textViewDialogTitle)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.bottomSheetList)).perform(actionOnItemAtPosition<StatsAdapter.ViewHolder>(1, ViewActions.click()))

        Espresso.onView(withId(R.id.textStatsCardValue)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}