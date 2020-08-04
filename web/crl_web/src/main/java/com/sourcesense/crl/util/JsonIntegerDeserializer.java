/* 
 * Copyright (C) 2019 Consiglio Regionale della Lombardia
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
package com.sourcesense.crl.util;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * Deserializzatore json per i numeri decimali. Converte le virgole in punti
 * 
 * @author sourcesense
 *
 */
public class JsonIntegerDeserializer extends JsonDeserializer<Integer> {

	@Override
	public Integer deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Integer value = 0;

		if (jp.getText() != null && jp.getTextLength() > 0) {

			if (jp.getText().contains(",")) {

				value = Integer.valueOf(jp.getText().replace(",", ""));
			} else if (jp.getText().contains(".")) {

				value = Integer.valueOf(jp.getText().replace(".", ""));
			} else {

				value = Integer.valueOf(jp.getText());
			}
		}

		return value;

	}

}
