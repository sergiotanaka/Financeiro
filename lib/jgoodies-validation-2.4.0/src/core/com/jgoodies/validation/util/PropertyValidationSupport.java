/*
 * Copyright (c) 2003-2012 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jgoodies.validation.util;

import static com.jgoodies.common.base.Preconditions.checkArgument;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.message.PropertyValidationMessage;


/**
 * A utility class that minimizes the effort to create instances
 * of {@link PropertyValidationMessage} in validation code.
 * You can use an instance of this class as a member field of your
 * validator implementation and delegate the message creation to it.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.11 $
 *
 * @see com.jgoodies.validation.message.PropertyValidationMessage
 */
public class PropertyValidationSupport {

    /**
     * Refers to the {@link ValidationResult} that is used to add messages to
     * if no individual result is specified.
     *
     * @see #add(String, String)
     * @see #addError(String, String)
     * @see #addWarning(String, String)
     * @see #clearResult()
     */
    private ValidationResult defaultResult;

    /**
     * Holds the severity that is used in the message creation and adder methods
     * that use no individual severity.
     *
     * @see #create(String, String)
     * @see #add(String, String)
     * @see #add(ValidationResult, String, String)
     */
    private final Severity defaultSeverity;

    /**
     * Refers to the object to be validated.
     */
    private final Object target;

    /**
     * Describes the validation target's role in the outer context.
     */
    private final String role;


    // Instance Creation ******************************************************

    /**
     * Constructs a {@code PropertyValidationSupport} instance for the
     * given validation target and its validation role. The default severity
     * is set to {@code Severity.WARNING}.
     *
     * @param target    the object to be validated
     * @param role      the validation target's role in the outer context
     *
     * @throws NullPointerException if the target or role is {@code null}
     */
    public PropertyValidationSupport(Object target, String role) {
        this(Severity.WARNING, target, role);
    }


    /**
     * Constructs a {@code PropertyValidationSupport} instance for the
     * given validation target and its validation role.
     *
     * @param defaultSeverity   the optional {@code Severity} used for
     *     message creation when no severity is specified
     * @param target    the object to be validated
     * @param role      the validation target's role in the outer context
     *
     * @throws NullPointerException if the target or role is {@code null}
     * @throws IllegalArgumentException if defaultSeverity is {@code Severity.OK}
     */
    public PropertyValidationSupport(Severity defaultSeverity, Object target, String role) {
        this(new ValidationResult(), defaultSeverity, target, role);
    }


    /**
     * Constructs a {@code PropertyValidationSupport} instance for the
     * given default result, default severity, validation target and the given
     * validation role.
     *
     * @param defaultResult     the optional {@code ValidationResult}
     *     that is used to add {@code ValidationMessage}s to
     * @param defaultSeverity   the optional {@code Severity} used for
     *     message creation when no severity is specified
     * @param target    the object to be validated
     * @param role      the validation target's role in the outer context
     *
     * @throws NullPointerException if the target or role is {@code null}
     * @throws IllegalArgumentException if {@code defaultSeverity} is {@code OK}
     */
    public PropertyValidationSupport(ValidationResult defaultResult, Severity defaultSeverity, Object target, String role) {
        checkArgument(defaultSeverity != Severity.OK,
                "Validation messages must have a severity other than OK.");
        this.defaultResult = defaultResult;
        this.defaultSeverity = defaultSeverity;
        this.target = target;
        this.role   = role;
    }


    // Accessing the ValidationResult *****************************************

    /**
     * Sets an empty {@link ValidationResult} as default result.
     * Useful at the begin of a validation sequence.
     */
    public final void clearResult() {
        defaultResult = new ValidationResult();
    }


    /**
     * Returns the default {@link ValidationResult}.
     *
     * @return the default validation result
     */
    public final ValidationResult getResult() {
        return defaultResult;
    }


    // Message Creation *******************************************************

    /**
     * Creates and returns an error {@code PropertyValidationMessage}
     * for the given property and message text.
     *
     * @param property    describes the validated property
     * @param text        the message text
     * @return a {@code PropertyValidationMessage} with error severity
     *     for the given property and text
     */
    public final PropertyValidationMessage createError(String property, String text) {
        return create(Severity.ERROR, property, text);
    }


    /**
     * Creates and returns a warning {@code PropertyValidationMessage}
     * for the given property and message text.
     *
     * @param property    describes the validated property
     * @param text        the message text
     * @return a {@code PropertyValidationMessage} with warning severity
     *     for the given property and text
     */
    public final PropertyValidationMessage createWarning(String property, String text) {
        return create(Severity.WARNING, property, text);
    }


    /**
     * Creates and returns a {@code PropertyValidationMessage}
     * for the given property and message text using the default severity.
     *
     * @param property    describes the validated property
     * @param text        the message text
     * @return a {@code PropertyValidationMessage} with default severity
     *     for the given property and text
     */
    public final PropertyValidationMessage create(String property, String text) {
        return create(defaultSeverity, property, text);
    }


    /**
     * Creates and returns an error {@code PropertyValidationMessage}
     * for the given property and message text using the specified severity.
     *
     * Subclasses can override this method to return a custom subclass
     * of PropertyValidationMessage.
     *
     * @param severity    the {@code Severity} to be used
     * @param property    describes the validated property
     * @param text        the message text
     * @return a {@code PropertyValidationMessage} with the specified severity
     *     for the given property and text
     *
     * @throws IllegalArgumentException if {@code severity} is {@code OK}
     */
    public PropertyValidationMessage create(Severity severity, String property, String text) {
        return new PropertyValidationMessage(severity, text, target, role, property);
    }


    // Adding Messages to the Default ValidationResult ************************

    /**
     * Adds an error {@code PropertyValidationMessage} to this object's
     * default {@code ValidationResult}.
     * Uses the given property and message text.
     *
     * @param property    describes the validated property
     * @param text        the message text
     */
    public final void addError(String property, String text) {
        addError(defaultResult, property, text);
    }


    /**
     * Adds a warning {@code PropertyValidationMessage} to this object's
     * default {@code ValidationResult}.
     * Uses the given property and message text.
     *
     * @param property    describes the validated property
     * @param text        the message text
     */
    public final void addWarning(String property, String text) {
        addWarning(defaultResult, property, text);
    }


    /**
     * Adds a {@code PropertyValidationMessage} to this object's
     * default {@code ValidationResult}.
     * Uses the default severity and the given property and message text.
     *
     * @param property    describes the validated property
     * @param text        the message text
     */
    public final void add(String property, String text) {
        add(defaultResult, property, text);
    }


    /**
     * Adds a {@code PropertyValidationMessage} to this object's
     * default {@code ValidationResult}. Uses the specified
     * {@code Severity} and given property and message text.
     *
     * @param severity    the {@code Severity} to be used
     * @param property    describes the validated property
     * @param text        the message text
     */
    public final void add(Severity severity, String property, String text) {
        add(defaultResult, severity, property, text);
    }


    // Adding Messages to a Given ValidationResult ****************************

    /**
     * Adds an error {@code PropertyValidationMessage} to the specified
     * {@code ValidationResult}.
     * Uses the given property and message text.
     *
     * @param result      the result the message will be added to
     * @param property    describes the validated property
     * @param text        the message text
     */
    public final void addError(ValidationResult result, String property, String text) {
        result.add(createError(property, text));
    }


    /**
     * Adds a warning {@code PropertyValidationMessage} to the specified
     * {@code ValidationResult}.
     * Uses the given property and message text.
     *
     * @param result      the result the message will be added to
     * @param property    describes the validated property
     * @param text        the message text
     */
    public final void addWarning(ValidationResult result, String property, String text) {
        result.add(createWarning(property, text));
    }


    /**
     * Adds a {@code PropertyValidationMessage} to the specified
     * {@code ValidationResult}. Uses this object's default severity
     * and the given property and message text.
     *
     * @param result      the result the message will be added to
     * @param property    describes the validated property
     * @param text        the message text
     */
    public final void add(ValidationResult result, String property, String text) {
        result.add(create(property, text));
    }


    /**
     * Adds a {@code PropertyValidationMessage} to the specified
     * {@code ValidationResult}. Uses the specified severity
     * and the given property and message text.
     *
     * @param result      the result the message will be added to
     * @param severity    the severity used for the created message
     * @param property    describes the validated property
     * @param text        the message text
     *
     * @throws IllegalArgumentException if {@code severity} is {@code OK}
     */
    public final void add(ValidationResult result, Severity severity, String property, String text) {
        result.add(create(severity, property, text));
    }


}
