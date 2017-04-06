/*
 * Copyright 2017 Crown Copyright
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

package uk.gov.gchq.koryphe.predicate;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.ToStringBuilder;
import uk.gov.gchq.koryphe.composite.Composite;
import java.util.List;
import java.util.function.Predicate;

/**
 * A composite {@link java.util.function.Predicate} that returns true if any of it's predicates return true, otherwise false.
 *
 * @param <I> Type of input to be validated.
 */
public final class Or<I> extends Composite<Predicate<I>> implements IKoryphePredicate<I> {
    public Or() {
        super();
    }

    @SafeVarargs
    public Or(final Predicate<I>... predicates) {
        super(Lists.newArrayList(predicates));
    }

    public Or(final List<Predicate<I>> predicates) {
        super(predicates);
    }

    @Override
    public boolean test(final I input) {
         return functions.stream()
                                  .reduce(Predicate::or)
                                  .orElse(p -> false).test(input);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(super.getFunctions())
                .toString();
    }
}