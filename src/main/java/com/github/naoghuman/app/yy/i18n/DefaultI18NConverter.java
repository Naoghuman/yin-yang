/*
 * Copyright (C) 2018 Naoghuman's dream
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.naoghuman.app.yy.i18n;

import java.util.Locale;

/**
 *
 * @author Naoghuman
 */
public class DefaultI18NConverter implements I18NConverter {

    @Override
    public String convertTo(final Locale locale) {
        return locale.toLanguageTag();
    }

    @Override
    public Locale convertTo(final String locale) {
        return Locale.forLanguageTag(locale);
    }
    
}
