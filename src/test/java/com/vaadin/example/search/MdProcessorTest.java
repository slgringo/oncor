package com.vaadin.example.search;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class MdProcessorTest {

    @Test
    public void testGetResourceFilename() {
        assertEquals("file.pdf", MdProcessor.getResourceFilename(" some text resources:\r\n - file.pdf"));
    }
}
