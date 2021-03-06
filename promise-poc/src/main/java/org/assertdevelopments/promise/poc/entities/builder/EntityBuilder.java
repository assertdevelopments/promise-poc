/*
 * Copyright 2015 Assert Developments
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

package org.assertdevelopments.promise.poc.entities.builder;

/**
 * @author Stefan Bangels
 * @since 2015-06-22
 */
public interface EntityBuilder<E> {

    EntityBuilder<E> setValue(String field, Object value);

    EntityBuilder<E> startEntityValue(String field);

    EntityBuilder<E> endValue();

    E getEntity();

}
