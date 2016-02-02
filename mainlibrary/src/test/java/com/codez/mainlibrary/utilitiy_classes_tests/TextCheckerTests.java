package com.codez.mainlibrary.utilitiy_classes_tests;

import android.test.suitebuilder.annotation.SmallTest;
import android.text.InputType;

import com.codez.mainlibrary.utilities.TextChecker;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by Shafqat on 1/29/2016.
 */
@SmallTest
public class TextCheckerTests {

    public String mNumeric;
    public String mAlphaNumeric;
    public String mPersonName;
    public String mSpecialCharacters;

    @Before
    public void initTestStrings(){
        mNumeric = "1234567890";
        mPersonName = "John Doe";
        mSpecialCharacters = "very.“(),:;<>[]”.VERY.“very@\\\\ \"very”.unusual---string";
        mAlphaNumeric = "A1B2C3";
    }

    @Test
    public void correctEmailSimple_ReturnsTrue() {
        assertTrue(TextChecker.checkIfEmailProper("name@email.com"));
    }

    @Test
    public void correctEmailSubDomain_ReturnsTrue() {
        assertTrue(TextChecker.checkIfEmailProper("name@email.co.uk"));
    }

    @Test
    public void invalidEmailNoTld_ReturnsFalse() {
        assertFalse(TextChecker.checkIfEmailProper("name@email"));
    }

    @Test
    public void invalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(TextChecker.checkIfEmailProper("name@email..com"));
    }

    @Test
    public void invalidEmailNoUsername_ReturnsFalse() {
        assertFalse(TextChecker.checkIfEmailProper("@email.com"));
    }

    @Test
    public void emptyString_ReturnsFalse() {
        assertFalse(TextChecker.checkIfEmailProper(""));
    }

    @Test
    public void hasIllegalChars_ReturnsTrue(){
        assertTrue(TextChecker.hasIlegalChars(mSpecialCharacters));
    }

    @Test
    public void hasIllegalChars_ReturnsFalse(){
        assertFalse(TextChecker.hasIlegalChars(mAlphaNumeric));
    }

    @Test
    public void isNumeric_ReturnsTrue(){
        assertTrue(TextChecker.isNumeric(mNumeric));
    }

    @Test
    public void isNumeric_ReturnsFalse(){
        assertFalse(TextChecker.isNumeric(mAlphaNumeric));
    }

    @Test
    public void checkTest_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(TextChecker.checkText("name@email.com", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
    }

    @Test
    public void checkTest_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(TextChecker.checkText("name@email.co.uk", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
    }

    @Test
    public void checkTest_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(TextChecker.checkText("name@email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
    }

    @Test
    public void checkTest_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(TextChecker.checkText("name@email..com", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
    }

    @Test
    public void checkTest_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(TextChecker.checkText("@email.com", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS));
    }

    @Test
    public void checkText_PhoneNumber_ReturnTrue(){
        assertTrue(TextChecker.checkText(mNumeric, InputType.TYPE_CLASS_PHONE));
    }

    @Test
    public void checkText_PhoneNumber_ReturnFalse(){
        assertFalse(TextChecker.checkText(mAlphaNumeric, InputType.TYPE_CLASS_PHONE));
    }

    @Test
    public void checkText_PersonName_ReturnTrue(){
        assertTrue(TextChecker.checkText(mPersonName, InputType.TYPE_TEXT_VARIATION_PERSON_NAME));
    }

    @Test
    public void checkText_PersonName_ReturnFalse(){
        assertFalse(TextChecker.checkText(mSpecialCharacters, InputType.TYPE_TEXT_VARIATION_PERSON_NAME));
    }

    @Test
    public void checkText_PostalAddress_ReturnTrue(){
        assertTrue(TextChecker.checkText(mAlphaNumeric, InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS));
    }
}
