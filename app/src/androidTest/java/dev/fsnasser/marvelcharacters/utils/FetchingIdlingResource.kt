package dev.fsnasser.marvelcharacters.utils

import androidx.test.espresso.IdlingResource
import dev.fsnasser.marvelcharacters.utils.helpers.FetcherListener

class FetchingIdlingResource : IdlingResource, FetcherListener {

    private var idle = true
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = FetchingIdlingResource::class.java.simpleName

    override fun isIdleNow(): Boolean = idle

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

    override fun doneFetching() {
        idle = true
        resourceCallback?.onTransitionToIdle()
    }

    override fun beginFetching() {
        idle = false
    }

}