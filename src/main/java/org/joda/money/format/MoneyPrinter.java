/*
 *  Copyright 2009 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.money.format;

import java.io.IOException;

import org.joda.money.Money;

/**
 * Prints instances of <code>Money</code>.
 * <p>
 * MoneyPrinter is an interface which must be implemented in a thread-safe manner.
 */
public interface MoneyPrinter {

    /**
     * Prints an instance of <code>Money</code> to the <code>Appendable</code>.
     * <p>
     * The implementation determines what to append, which may be some or all
     * of the data held in the <code>Money</code>.
     * 
     * @param context  the context being used, not null
     * @param money  the money to print, not null
     * @return the string printed using the settings of this formatter
     * @throws MoneyFormatException if there is a problem while printing
     * @throws IOException if an IO exception occurs
     */
    void print(MoneyPrintContext context, Appendable appendable, Money money) throws IOException;

}