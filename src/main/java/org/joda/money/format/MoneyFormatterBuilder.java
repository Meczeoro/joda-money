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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.money.Money;

/**
 * Provides the ability to build a formatter for <code>Money</code> instances.
 * <p>
 * MoneyFormatter is a mutable builder - a new instance should be created for each use.
 * The formatters produced by the builder are immutable and thread-safe.
 */
public final class MoneyFormatterBuilder {

    /**
     * The printers.
     */
    private final List<MoneyPrinter> iPrinters = new ArrayList<MoneyPrinter>();

    //-----------------------------------------------------------------------
    /**
     * Constructor, creating a new empty builder.
     */
    public MoneyFormatterBuilder() {
    }

    //-----------------------------------------------------------------------
    /**
     * Appends the amount to the builder using a localized format.
     * <p>
     * The amount is the value itself, such as '12.34'.
     * 
     * @return this, for chaining, never null
     */
    public MoneyFormatterBuilder appendAmount() {
        return appendInternal(new Amount(MoneyAmountStyle.LOCALIZED_GROUPING));
    }

    /**
     * Appends the amount to the builder using the specified amount style.
     * <p>
     * The amount is the value itself, such as '12.34'.
     * <p>
     * The amount style allows the formatting of the number to be controlled in detail.
     * See {@link MoneyAmountStyle} for more details.
     * 
     * @param style  the style to use, not null
     * @return this, for chaining, never null
     */
    public MoneyFormatterBuilder appendAmount(MoneyAmountStyle style) {
        MoneyFormatter.checkNotNull(style, "MoneyAmountStyle must not be null");
        return appendInternal(new Amount(style));
    }

    //-----------------------------------------------------------------------
    /**
     * Appends the currency code to the builder.
     * <p>
     * The currency code is the three letter ISO code, such as 'GBP'.
     * 
     * @return this, for chaining, never null
     */
    public MoneyFormatterBuilder appendCurrencyCode() {
        return appendInternal(Code.INSTANCE);
    }

    /**
     * Appends the currency code to the builder.
     * <p>
     * The numeric code is the ISO numeric code, such as '826' and is
     * zero padded to three digits.
     * 
     * @return this, for chaining, never null
     */
    public MoneyFormatterBuilder appendCurrencyNumeric3Code() {
        return appendInternal(Numeric3Code.INSTANCE);
    }

    /**
     * Appends the currency code to the builder.
     * <p>
     * The numeric code is the ISO numeric code, such as '826'.
     * 
     * @return this, for chaining, never null
     */
    public MoneyFormatterBuilder appendCurrencyNumericCode() {
        return appendInternal(NumericCode.INSTANCE);
    }

    /**
     * Appends the localized currency symbol to the builder.
     * <p>
     * The localized currency symbol is the symbol as chosen by the locale
     * of the formatter.
     * 
     * @return this, for chaining, never null
     */
    public MoneyFormatterBuilder appendCurrencySymbolLocalized() {
        return appendInternal(LocalizedSymbol.INSTANCE);
    }

    /**
     * Appends a literal to the builder.
     * <p>
     * The localized currency symbol is the symbol as chosen by the locale
     * of the formatter.
     * 
     * @param literal  the literal to append, null or empty ignored
     * @return this, for chaining, never null
     */
    public MoneyFormatterBuilder appendLiteral(CharSequence literal) {
        if (literal == null || literal.length() == 0) {
            return this;
        }
        return appendInternal(new Literal(literal.toString()));
    }

    //-----------------------------------------------------------------------
    /**
     * Appends the specified printer to this builder.
     * 
     * @param printer  the printer to append, not null
     * @return this for chaining, never null
     */
    public MoneyFormatterBuilder append(MoneyPrinter printer) {
        MoneyFormatter.checkNotNull(printer, "MoneyPrinter must not be null");
        return appendInternal(printer);
    }

    //-----------------------------------------------------------------------
    /**
     * Appends the specified printer to this builder.
     * 
     * @param printer  the printer to append, null ignored
     * @return this for chaining, never null
     */
    private MoneyFormatterBuilder appendInternal(MoneyPrinter printer) {
        if (printer != null) {
            iPrinters.add(printer);
        }
        return this;
    }

    //-----------------------------------------------------------------------
    /**
     * Builds the formatter from the builder using the default locale.
     * <p>
     * Once the builder is in the correct state it must be converted to a
     * <code>MoneyFormatter</code> to be used. Calling this method does not
     * change the state of this instance, so it can still be used.
     * <p>
     * The locale is simply the initial locale used.
     * It can be changed by calling {@link MoneyFormatter#withLocale(Locale)}.
     * 
     * @return the formatter built from this builder, never null
     */
    public MoneyFormatter toFormatter() {
        return toFormatter(Locale.getDefault());
    }

    /**
     * Builds the formatter from the builder setting the locale.
     * <p>
     * Once the builder is in the correct state it must be converted to a
     * <code>MoneyFormatter</code> to be used. Calling this method does not
     * change the state of this instance, so it can still be used.
     * <p>
     * The locale is simply the initial locale used.
     * It can be changed by calling {@link MoneyFormatter#withLocale(Locale)}.
     * 
     * @param locale  the initial locale for the formatter, not null
     * @return the formatter built from this builder, never null
     */
    public MoneyFormatter toFormatter(Locale locale) {
        MoneyFormatter.checkNotNull(locale, "Locale must not be null");
        MoneyPrinter[] printers = (MoneyPrinter[]) iPrinters.toArray(new MoneyPrinter[iPrinters.size()]);
        return new MoneyFormatter(locale, printers);
    }

//    /**
//     * Returns a copy of this instance with the specified pattern.
//     * <p>
//     * The specified pattern is used for positive and zero amounts, while for
//     * negative amounts it is prefixed by the negative sign.
//     * <p>
//     * A pattern is a simple way to define the characters which surround the numeric value.
//     * For example, <code>${amount} ${code}</code> will print the ISO code after the value,
//     * producing an output like <code>12.34 GBP</code>.
//     * Similarly, <code>-${symbol}${amount}</code> will produce the output <code>-£12.34</code>.
//     * <p>
//     * The pattern contains the following elements:<br />
//     * <ul>
//     * <li><code>${amount}</code> : the monetary amount itself
//     * <li><code>${code}</code> : the letter-based currency code, such as 'USD'
//     * <li><code>${numericCode}</code> : the numeric currency code, such as '840'
//     * <li><code>${symbol}</code> : the currency symbol, such as '$'
//     * </ul>
//     * 
//     * @param pattern  the pattern
//     * @return the new instance, never null
//     */
//    public MoneyFormatterBuilder withPattern(String pattern) {
//        return withPattern(pattern, "-" + pattern, pattern);
//    }
//
//    /**
//     * Returns a copy of this instance with the specified positive and negative pattern.
//     * <p>
//     * The specified positive pattern is also used for zero amounts.
//     * <p>
//     * A pattern is a simple way to define the characters which surround the numeric value.
//     * For example, <code>${amount} ${code}</code> will print the ISO code after the value,
//     * producing an output like <code>12.34 GBP</code>.
//     * Similarly, <code>-${symbol}${amount}</code> will produce the output <code>-£12.34</code>.
//     * <p>
//     * The pattern contains the following elements:<br />
//     * <ul>
//     * <li><code>${amount}</code> : the monetary amount itself
//     * <li><code>${code}</code> : the letter-based currency code, such as 'USD'
//     * <li><code>${numericCode}</code> : the numeric currency code, such as '840'
//     * <li><code>${symbol}</code> : the currency symbol, such as '$'
//     * </ul>
//     * 
//     * @param positivePattern  the positive pattern
//     * @param negativePattern  the negative pattern
//     * @return the new instance, never null
//     */
//    public MoneyFormatterBuilder withPattern(String positivePattern, String negativePattern) {
//        return withPattern(positivePattern, negativePattern, positivePattern);
//    }
//
//    /**
//     * Returns a copy of this instance with the specified positive, negative and zero pattern.
//     * <p>
//     * The positive pattern is used for positive amounts, the negative pattern for negative amounts
//     * and the zero pattern for zero amounts.
//     * <p>
//     * A pattern is a simple way to define the characters which surround the numeric value.
//     * For example, <code>${amount} ${code}</code> will print the ISO code after the value,
//     * producing an output like <code>12.34 GBP</code>.
//     * Similarly, <code>-${symbol}${amount}</code> will produce the output <code>-£12.34</code>.
//     * <p>
//     * The pattern contains the following elements:<br />
//     * <ul>
//     * <li><code>${amount}</code> : the monetary amount itself
//     * <li><code>${code}</code> : the letter-based currency code, such as 'USD'
//     * <li><code>${numericCode}</code> : the numeric currency code, such as '840'
//     * <li><code>${symbol}</code> : the currency symbol, such as '$'
//     * </ul>
//     * 
//     * @param positivePattern  the positive pattern
//     * @param negativePattern  the negative pattern
//     * @param zeroPattern  the zero pattern
//     * @return the new instance, never null
//     */
//    public MoneyFormatterBuilder withPattern(String positivePattern, String negativePattern, String zeroPattern) {
//        MoneyUtils.checkNotNull(positivePattern, "Positive pattern must not be null");
//        MoneyUtils.checkNotNull(negativePattern, "Negative pattern must not be null");
//        MoneyUtils.checkNotNull(zeroPattern, "Zero pattern must not be null");
//        return new MoneyFormatterBuilder(iLocale, iZeroCharacter, iDecimalPointCharacter, iGroupingCharacter,
//                iGrouping, iGroupingSize, iForceDecimalPoint, positivePattern, negativePattern, zeroPattern);
//    }

    //-----------------------------------------------------------------------
    /**
     * Handles the amount.
     */
    private static class Amount implements MoneyPrinter {
        /** The style to use. */
        private volatile MoneyAmountStyle iStyle;
        /**
         * Constructor.
         * @param style  the style, not null
         */
        Amount(MoneyAmountStyle style) {
            iStyle = style;
        }
        /** {@inheritDoc} */
        public void print(MoneyPrintContext context, Appendable appendable, Money money) throws IOException {
            iStyle = iStyle.localize(context.getLocale());
            String str = money.getAmount().toPlainString();
            int decPoint = str.indexOf('.');
            if (iStyle.isGrouping()) {
                int groupingSize = iStyle.getGroupingSize();
                char groupingChar = iStyle.getGroupingCharacter();
                int pre = (decPoint < 0 ? str.length() : decPoint);
//                int post = (decPoint < 0 ? 0 : str.length() - decPoint - 1);
                for (int i = 0; pre > 0; i++, pre--) {
                    appendable.append(str.charAt(i));
                    if (pre > 3 && pre % groupingSize == 1) {
                        appendable.append(groupingChar);
                    }
                }
                if (decPoint >= 0) {
                    appendable.append(iStyle.getDecimalPointCharacter()).append(str.substring(decPoint + 1));
                } else if (iStyle.isForcedDecimalPoint()) {
                    appendable.append(iStyle.getDecimalPointCharacter());
                }
//                decPoint++;
//                for (int i = 0; i < post; i++) {
//                    appendable.append(str.charAt(i + decPoint));
//                    if (i % groupingSize == 2) {
//                        appendable.append(groupingChar);
//                    }
//                }
            } else {
                if (decPoint < 0) {
                    if (iStyle.isForcedDecimalPoint()) {
                        appendable.append(str).append(iStyle.getDecimalPointCharacter());
                    } else {
                        appendable.append(str);
                    }
                } else {
                    appendable.append(str.subSequence(0, decPoint))
                        .append(iStyle.getDecimalPointCharacter()).append(str.substring(decPoint + 1));
                }
            }
        }
        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "${amount}";
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Handles the textual currency code.
     */
    private static class Code implements MoneyPrinter {
        static final MoneyPrinter INSTANCE = new Code();
        /** {@inheritDoc} */
        public void print(MoneyPrintContext context, Appendable appendable, Money money) throws IOException {
            appendable.append(money.getCurrencyUnit().getCurrencyCode());
        }
        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "${code}";
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Handles the numeric currency code as three digits.
     */
    private static class Numeric3Code implements MoneyPrinter {
        static final MoneyPrinter INSTANCE = new Numeric3Code();
        /** {@inheritDoc} */
        public void print(MoneyPrintContext context, Appendable appendable, Money money) throws IOException {
            appendable.append(money.getCurrencyUnit().getNumeric3Code());
        }
        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "${numeric3Code}";
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Handles the numeric currency code.
     */
    private static class NumericCode implements MoneyPrinter {
        static final MoneyPrinter INSTANCE = new NumericCode();
        /** {@inheritDoc} */
        public void print(MoneyPrintContext context, Appendable appendable, Money money) throws IOException {
            appendable.append(Integer.toString(money.getCurrencyUnit().getNumericCode()));
        }
        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "${numericCode}";
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Handles the localized symbol.
     */
    private static class LocalizedSymbol implements MoneyPrinter {
        static final MoneyPrinter INSTANCE = new LocalizedSymbol();
        /** {@inheritDoc} */
        public void print(MoneyPrintContext context, Appendable appendable, Money money) throws IOException {
            appendable.append(money.getCurrencyUnit().getSymbol(context.getLocale()));
        }
        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "${symbolLocalized}";
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Handles a literal.
     */
    private static class Literal implements MoneyPrinter {
        private final String iLiteral;
        /**
         * Constructor.
         * @param literal  the literal text, not null
         */
        Literal(String literal) {
            iLiteral = literal;
        }
        /** {@inheritDoc} */
        public void print(MoneyPrintContext context, Appendable appendable, Money money) throws IOException {
            appendable.append(iLiteral);
        }
        /** {@inheritDoc} */
        @Override
        public String toString() {
            return "'" + iLiteral + "'";
        }
    }

}