package dev.fsnasser.marvelcharacters.ui

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.ui.adapters.CharactersListAdapter
import dev.fsnasser.marvelcharacters.ui.views.main.MainActivity
import dev.fsnasser.marvelcharacters.utils.AppConstants
import dev.fsnasser.marvelcharacters.utils.FetchingIdlingResource
import dev.fsnasser.marvelcharacters.utils.TestUtil.createCharacterApi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.action.ViewActions.swipeDown
import org.hamcrest.CoreMatchers.*


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val mainActivityRule = ActivityTestRule(MainActivity::class.java, false, false)

    private lateinit var mServer: MockWebServer

    private val fetchingIdlingResource = FetchingIdlingResource()

    private lateinit var mContext: Context

    @Before
    fun setup() {
        mServer = MockWebServer()
        mServer.start()
        AppConstants.MARVEL_API_URL = mServer.url("/").toString()

        mainActivityRule.launchActivity(null)
        IdlingRegistry.getInstance().register(fetchingIdlingResource)
        mainActivityRule.activity.fetcherListener = fetchingIdlingResource

        mContext = ApplicationProvider.getApplicationContext<Context>()
    }

    @Test
    fun charactersFragment_WhenApiReturns200_CheckAllFields() {
        mServer.enqueue(MockResponse().setResponseCode(200).setBody(Gson()
            .toJson(createCharacterApi(3))))

        val charactersTabLayout = allOf(withText(mContext.getString(R.string.characters)),
            isDescendantOfA(withId(R.id.tlCharactersMain)))
        onView(charactersTabLayout).perform(click())

        onView(withId(R.id.srCharacters)).check(matches(isDisplayed()))
        onView(withId(R.id.rvCharacters)).check(matches(isDisplayed()))
        onView(withId(R.id.pbCharacters)).check(matches(not(isDisplayed())))
        onView(withId(R.id.iCharactersWarning)).check(matches(not(isDisplayed())))
    }

    @Test
    fun charactersFragment_WhenClickOnCharacter_ShouldOpenCharacterDetailActivity() {
        val characterApi = createCharacterApi(3)
        mServer.enqueue(MockResponse().setResponseCode(200).setBody(Gson()
            .toJson(characterApi)))

        val charactersTabLayout = allOf(withText(mContext.getString(R.string.characters)),
            isDescendantOfA(withId(R.id.tlCharactersMain)))
        onView(charactersTabLayout).perform(click())

        onView(withId(R.id.rvCharacters)).perform(
            RecyclerViewActions.actionOnItemAtPosition<CharactersListAdapter.ViewHolder>(
                0,
                click()
            ))

        onView(allOf(instanceOf(AppCompatTextView::class.java), withParent(isAssignableFrom(Toolbar::class.java))))
            .check(matches(withText(characterApi.data.results[0].name)))
        onView(withId(R.id.tvCharacterDetailDesc)).check(
            matches(withText(characterApi.data.results[0].description)))
    }

    @Test
    fun charactersFragment_WhenApiReturns500_ShouldShowWarningMessage() {
        mServer.enqueue(MockResponse().setResponseCode(500).setBody(""))

        val charactersTabLayout = allOf(withText(mContext.getString(R.string.characters)),
            isDescendantOfA(withId(R.id.tlCharactersMain)))
        onView(charactersTabLayout).perform(click())

        onView(withId(R.id.srCharacters)).check(matches(isDisplayed()))
        onView(withId(R.id.rvCharacters)).check(matches(not(isDisplayed())))
        onView(withId(R.id.pbCharacters)).check(matches(not(isDisplayed())))
        onView(withId(R.id.iCharactersWarning)).check(matches(isDisplayed()))
        onView(allOf(withId(R.id.tvWarningMessage), isDescendantOfA(withId(R.id.iCharactersWarning))))
            .check(matches(withText(mContext.getString(R.string.character_list_error_msg))))
    }

    @Test
    fun charactersFragment_WhenApiReturns500_WhenSwipeDownApiReturns200_CheckAllFields() {
        mServer.enqueue(MockResponse().setResponseCode(500).setBody(""))

        val charactersTabLayout = allOf(withText(mContext.getString(R.string.characters)),
            isDescendantOfA(withId(R.id.tlCharactersMain)))
        onView(charactersTabLayout).perform(click())

        val characterApi = createCharacterApi(3)
        mServer.enqueue(MockResponse().setResponseCode(200).setBody(Gson()
            .toJson(characterApi)))

        onView(withId(R.id.srCharacters)).perform(swipeDown())

        onView(withId(R.id.srCharacters)).check(matches(isDisplayed()))
        onView(withId(R.id.rvCharacters)).check(matches(isDisplayed()))
        onView(withId(R.id.pbCharacters)).check(matches(not(isDisplayed())))
        onView(withId(R.id.iCharactersWarning)).check(matches(not(isDisplayed())))
    }

    @After
    fun after() {
        IdlingRegistry.getInstance().unregister(fetchingIdlingResource)

        mServer.shutdown()
    }

}