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

package uk.gov.gchq.koryphe.function;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.signature.Signature;
import uk.gov.gchq.koryphe.util.SummaryUtil;
import uk.gov.gchq.koryphe.util.VersionUtil;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public abstract class FunctionTest {

    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        return mapper;
    }

    protected abstract Function getInstance();

    protected abstract Class<? extends Function> getFunctionClass();

    protected abstract Class[] getExpectedSignatureInputClasses();

    protected abstract Class[] getExpectedSignatureOutputClasses();

    @Test
    public abstract void shouldJsonSerialiseAndDeserialise() throws IOException;

    protected String serialise(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    protected Function deserialise(String json) throws IOException {
        return MAPPER.readValue(json, getFunctionClass());
    }

    @Test
    public void shouldEquals() {
        // Given
        final Function instance = getInstance();

        // When
        final Function other = getInstance();

        // Then
        assertEquals(instance, other);
        assertEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void shouldEqualsWhenSameObject() {
        // Given
        final Function instance = getInstance();

        // Then
        assertEquals(instance, instance);
        assertEquals(instance.hashCode(), instance.hashCode());
    }

    @Test
    public void shouldNotEqualsWhenDifferentClass() {
        // Given
        final Function instance = getInstance();

        // When
        final Object other = new Object();

        // Then
        assertNotEquals(instance, other);
        assertNotEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void shouldNotEqualsNull() {
        // Given
        final Function instance = getInstance();

        // Then
        assertNotNull(instance);
    }

    @Test
    public void shouldHaveSinceAnnotation() {
        // Given
        final Function instance = getInstance();

        // When
        final Since annotation = instance.getClass().getAnnotation(Since.class);

        // Then
        assertNotNull(annotation, "Missing Since annotation on class " + instance.getClass().getName());
        assertNotNull(annotation.value(), "Missing Since annotation on class " + instance.getClass().getName());
        assumeTrue(VersionUtil.validateVersionString(annotation.value()),
                annotation.value() + " is not a valid value string.");
    }

    @Test
    public void shouldHaveSummaryAnnotation() {
        // Given
        final Function instance = getInstance();

        // When
        final Summary annotation = instance.getClass().getAnnotation(Summary.class);

        // Then
        assertNotNull(annotation, "Missing Summary annotation on class " + instance.getClass().getName());
        assertNotNull(annotation.value(), "Missing Summary annotation on class " + instance.getClass().getName());
        assumeTrue(SummaryUtil.validateSummaryString(annotation.value()),
                annotation.value() + " is not a valid value string.");
    }

    @Test
    public void shouldGenerateExpectedInputSignature() {
        // Given
        final Function function = getInstance();

        // When
        final Signature signature = Signature.getInputSignature(function);

        // Then
        assertTrue(signature.assignable(getExpectedSignatureInputClasses()).isValid());
    }

    @Test
    public void shouldGenerateExpectedOutputSignature() {
        // Given
        final Function function = getInstance();

        // When
        final Signature signature = Signature.getOutputSignature(function);

        // Then
        assertTrue(signature.assignable(getExpectedSignatureOutputClasses()).isValid());
    }
}
