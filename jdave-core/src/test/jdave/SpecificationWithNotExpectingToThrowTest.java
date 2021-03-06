/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jdave;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Pekka Enberg
 */
public class SpecificationWithNotExpectingToThrowTest{
    private Specification<Void> specification = new Specification<Void>() { };

    @Test
    public void testShouldPassWhenNoExceptionRaised() throws Throwable {
        specification.specify(new Block() {
            public void run() throws Throwable {
                // Intentionally left blank.
            }
        }, specification.not().raise(IllegalArgumentException.class));
    }

    @Test
    public void testShouldFailIfThrowsExpectedException() throws Throwable {
        try {
            specification.specify(new Block() {
                public void run() throws Throwable {
                    throw new IllegalArgumentException();
                }
            }, specification.not().raise(IllegalArgumentException.class));
            fail();
        } catch (ExpectationFailedException e) {
            assertEquals("The specified block threw java.lang.IllegalArgumentException", e.getMessage());
        }
    }
    
    @Test
    public void testFailsIfAnyExceptionIsThrownFromBlockWhichDoesNotExpectAnyException() throws Throwable {
        try {
            specification.specify(new Block() {
                public void run() throws Throwable {
                    throw new IllegalArgumentException();
                }
            }, specification.not().raiseAnyException());
            fail();
        } catch (ExpectationFailedException e) {
            assertEquals("The specified block threw java.lang.IllegalArgumentException", e.getMessage());
        }        
    }

    @Test
    public void testShouldFailIfThrowsSubclassOfExpectedException() throws Throwable {
        try {
            specification.specify(new Block() {
                public void run() throws Throwable {
                    throw new IllegalArgumentException();
                }
            }, specification.not().raise(Throwable.class));
            fail();
        } catch (ExpectationFailedException e) {
            assertEquals("The specified block threw java.lang.IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testShouldFailIfThrowsExactlyExpectedException() throws Throwable {
        try {
            specification.specify(new Block() {
                public void run() throws Throwable {
                    throw new IllegalArgumentException();
                }
            }, specification.not().raiseExactly(IllegalArgumentException.class));
            fail();
        } catch (ExpectationFailedException e) {
            assertEquals("The specified block threw java.lang.IllegalArgumentException", e.getMessage());
        }
    }

    @Test
    public void testShouldRethrowIfThrowsSubclassOfExactlyExpectedException() throws Throwable {
        try {
            specification.specify(new Block() {
                public void run() throws Throwable {
                    throw new IllegalArgumentException("rethrown");
                }
            }, specification.not().raiseExactly(Throwable.class));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("rethrown", e.getMessage());
        }
    }
}
