/*
 * Copyright 2017-2020 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FirstItemTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new FirstItem();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return FirstItem.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Iterable.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final FirstItem function = new FirstItem();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.FirstItem\"%n" +
                "}"), json);

        // When 2
        final FirstItem deserialised = JsonSerialiser.deserialise(json, FirstItem.class);

        // Then 2
        assertNotNull(deserialised);
    }

    @Test
    public void shouldReturnCorrectValueWithInteger() {
        // Given
        final FirstItem<Integer> function = new FirstItem<>();

        // When
        final Integer result = function.apply(Arrays.asList(1, 2, 3, 4));

        // Then
        assertNotNull(result);
        assertEquals(1, result);
    }

    @Test
    public void shouldReturnCorrectValueWithString() {
        // Given
        final FirstItem<String> function = new FirstItem<>();

        // When
        final String result = function.apply(Arrays.asList("these", "are", "test", "strings"));

        // Then
        assertNotNull(result);
        assertEquals("these", result);
    }

    @Test
    public void shouldReturnNullForNullElement() {
        // Given
        final FirstItem<String> function = new FirstItem<>();

        // When
        final String result = function.apply(Arrays.asList(null, "two", "three"));

        // Then
        assertNull(result);
    }

    @Test
    public void shouldThrowErrorForNullInput() {
        // Given
        final FirstItem<String> function = new FirstItem<>();

        // When / Then
        final Exception exception = assertThrows(IllegalArgumentException.class, () -> function.apply(null));
        assertTrue(exception.getMessage().contains("Input cannot be null"));
    }
}
