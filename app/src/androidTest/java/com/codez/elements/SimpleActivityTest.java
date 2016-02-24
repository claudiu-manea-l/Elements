package com.codez.elements;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by Shafqat on 2/1/2016.
 */
public class SimpleActivityTest extends ActivityTestRule<SimpleActivity>{

    private static final String TEXT_ITEM_EXPANDABLE_VIEW = "ExpandableView";

    private static final String TEXT_ITEM_REBUILDABLE_SPINNER = "RebuildableSpinner";

    private static final String TEXT_ITEM_RESTFUL_LIST = "RestfulList";

    private static final String TEXT_ITEM_TITILED_LIST = "TitledList";

    public SimpleActivityTest(){
        super(SimpleActivity.class,true);
    }

    @Rule
    public ActivityTestRule<SimpleActivity> activityTestRule
            = new ActivityTestRule<>(SimpleActivity.class);

    @Test
    public void testListViewItemClicked_Test() {

        onData(anything())
                .inAdapterView(withId(R.id.list_view)).atPosition(0)
                .perform(click());

        onView(withId(R.id.text1))
                .check(matches(withText(TEXT_ITEM_EXPANDABLE_VIEW)));
    }

    public void testListView(){

    }

}
