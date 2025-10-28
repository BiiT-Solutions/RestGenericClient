package com.biit.rest.exceptions;

/*-
 * #%L
 * Rest Generic Client
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

public class EmptyResultException extends RuntimeException {

    private static final long serialVersionUID = 3681934777038371416L;

    public EmptyResultException(String message) {
        super(message);
    }

    public EmptyResultException(String message, Throwable e) {
        super(message, e);
    }
}
