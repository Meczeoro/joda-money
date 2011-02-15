/*
 *  Copyright 2009-2011 Stephen Colebourne
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
import java.io.Serializable;

import org.joda.money.BigMoney;

/**
 * Prints and parses a literal.
 * <p>
 * LiteralPrinterParser is immutable and thread-safe.
 */
final class LiteralPrinterParser implements MoneyPrinter, MoneyParser, Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** Literal. */
    private final String iLiteral;

    /**
     * Constructor.
     * @param literal  the literal text, not null
     */
    LiteralPrinterParser(String literal) {
        iLiteral = literal;
    }

    /** {@inheritDoc} */
    public void print(MoneyPrintContext context, Appendable appendable, BigMoney money) throws IOException {
        appendable.append(iLiteral);
    }

    /** {@inheritDoc} */
    public void parse(MoneyParseContext context) {
        int endPos = context.getIndex() + iLiteral.length();
        if (endPos <= context.getTextLength() &&
                context.getTextSubstring(context.getIndex(), endPos).equals(iLiteral)) {
            context.setIndex(endPos);
        } else {
            context.setError();
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "'" + iLiteral + "'";
    }

}
