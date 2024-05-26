package etf.rma.spirale

import android.view.View
import android.widget.EditText
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import etf.rma.spirale.mainActivity.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.anything
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestNovaBiljkaActivity {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )

    private fun hasNoErrorText(): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("has no error text")
            }

            override fun matchesSafely(view: View): Boolean {
                return if (view is EditText) {
                    view.error == null
                } else false
            }
        }
    }

    @Test
    fun testValidacijaPoljaPrekratko() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        onView(withId(R.id.nazivET)).perform(typeText("N"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("P"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("U"), closeSoftKeyboard())

        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Sadrzaj prekratak")))
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Sadrzaj prekratak")))
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Sadrzaj prekratak")))

    }

    @Test
    fun testValidacijaPoljaPredugo() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        onView(withId(R.id.nazivET)).perform(
            typeText("Predug naziv za biljku"), closeSoftKeyboard()
        )
        onView(withId(R.id.porodicaET)).perform(
            typeText("Preduga porodica biljke"), closeSoftKeyboard()
        )
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(
            typeText("Predugo upozorenje za biljku"), closeSoftKeyboard()
        )

        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Sadrzaj predug")))
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Sadrzaj predug")))
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Sadrzaj predug")))
    }

    @Test
    fun testJeloETValidacija() {

        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        onView(withId(R.id.jeloET)).perform(typeText("N"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Sadrzaj prekratak")))
        onView(withId(R.id.jeloET)).perform(clearText())


        onView(withId(R.id.jeloET)).perform(
            typeText("Predug naziv jela za biljku"), closeSoftKeyboard()
        )
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Sadrzaj predug")))
        onView(withId(R.id.jeloET)).perform(clearText())

        onView(withId(R.id.jeloET)).perform(typeText("Posteno jelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))

    }

    @Test
    fun testDuplikatiJela() {

        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        onView(withId(R.id.jeloET)).perform(typeText("Grah"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))
        onView(withId(R.id.jeloET)).perform(clearText())

        onView(withId(R.id.jeloET)).perform(typeText("Pita"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))
        onView(withId(R.id.jeloET)).perform(clearText())

        onView(withId(R.id.jeloET)).perform(typeText("PITA"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Jelo vec postoji!")))
        onView(withId(R.id.jeloET)).perform(clearText())
    }

    @Test
    fun testIzmijenaJela() {

        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        onView(withId(R.id.jeloET)).perform(typeText("Grah"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))
        onView(withId(R.id.jeloET)).perform(clearText())

        onView(withId(R.id.jeloET)).perform(typeText("Pita"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))
        onView(withId(R.id.jeloET)).perform(clearText())

        onView(withId(R.id.jeloET)).perform(typeText("Burek"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))
        onView(withId(R.id.jeloET)).perform(clearText())

        onData(anything()).inAdapterView(withId(R.id.jelaLV)).atPosition(1).perform(click())

        onView(withId(R.id.dodajJeloBtn)).check(matches(withText("Izmijeni jelo")))
        onView(withId(R.id.jeloET)).check(matches(withText("Pita")))
        onView(withId(R.id.jeloET)).perform(clearText())
        onView(withId(R.id.jeloET)).perform(typeText("Burek"))
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Jelo vec postoji!")))
        onView(withId(R.id.jeloET)).perform(clearText())
        onView(withId(R.id.jeloET)).perform(typeText("Zeljanica"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.dodajJeloBtn)).check(matches(withText("Dodaj jelo")))
        onData(anything()).inAdapterView(withId(R.id.jelaLV)).atPosition(1)
            .check(matches(withText("Zeljanica")))


    }

    @Test
    fun testIspravnaBiljka() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        onView(withId(R.id.nazivET)).perform(
            typeText("Luda Lavanda"), closeSoftKeyboard()
        )
        onView(withId(R.id.porodicaET)).perform(
            typeText("Ludae"), closeSoftKeyboard()
        )
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(
            typeText("Luda je!"), closeSoftKeyboard()
        )

        onView(withId(R.id.jeloET)).perform(typeText("Mmmmm"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))
        onView(withId(R.id.jeloET)).perform(clearText())

        onView(withId(R.id.jeloET)).perform(typeText("Bljak!"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasNoErrorText()))
        onView(withId(R.id.jeloET)).perform(clearText())

        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(1).perform(click())
        onData(anything()).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(1).perform(click())

        onData(anything()).inAdapterView(withId(R.id.profilOkusaLV)).atPosition(0).perform(click())

        onView(withId(R.id.dodajBiljkuBtn)).perform(click())

    }

//    @Test
//    fun testOdabirIzEnuma() {
//        onView(withId(R.id.novaBiljkaBtn)).perform(click())
//
//        onView(withId(R.id.nazivET)).perform(typeText("Naziv"), closeSoftKeyboard())
//        onView(withId(R.id.porodicaET)).perform(typeText("Porodica"), closeSoftKeyboard())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(
//            typeText("Upozorenje!"), closeSoftKeyboard()
//        )
//
//        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
//
//        onView(withText("Nije odabrano dovoljno opcija"))
//            .inRoot(withDecorView(not(`is`(activityTestRule.activity.window.decorView))))
//            .check(matches(isDisplayed()))
//
//    }

}