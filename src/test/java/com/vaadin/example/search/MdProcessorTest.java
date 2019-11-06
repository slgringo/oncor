package com.vaadin.example.search;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class MdProcessorTest {

    @Test(enabled = false)
    public void testGetResourceFilename() {
        assertEquals("file.pdf", MdProcessor.INSTANCE.getResourceFilename(" some text resources:\r\n - file.pdf"));
    }
}
