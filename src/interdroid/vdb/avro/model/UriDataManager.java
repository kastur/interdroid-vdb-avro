/*
 * Copyright (c) 2008-2012 Vrije Universiteit, The Netherlands All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the Vrije Universiteit nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package interdroid.vdb.avro.model;

import interdroid.util.DbUtil;
import interdroid.vdb.content.EntityUriMatcher;

import interdroid.vdb.content.EntityUriMatcher.UriMatch;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * A handler for persisting models to Uris.
 * @author nick &lt;palmer@cs.vu.nl&gt;
 *
 */
public final class UriDataManager {
	/** Access to logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(UriDataManager.class);

	/**
	 * No construction.
	 */
	private UriDataManager() {
		// No construction
	}

	/**
	 * Utility for safely closing a cursor.
	 * @param cursor the cursor to close
	 */
	static void safeClose(final Cursor cursor) {
		if (null != cursor) {
			try {
				cursor.close();
			} catch (Exception e) {
				LOG.warn("Ignoring exception closing cursor.", e);
			}
		}
	}

	/**
	 * Loads data from a content provider.
	 * @param resolver the resolver to load with
	 * @param rootUri the uri to load
	 * @param cursor the cursor to load from
	 * @param fieldName the name of the field being loaded
	 * @param fieldSchema the schema for the field
	 * @return the loaded data
	 * @throws NotBoundException if the data is not bound properly
	 */
	@SuppressWarnings("rawtypes")
	static Object loadDataFromUri(final ContentResolver resolver,
			final Uri rootUri, final Cursor cursor,
			final String fieldName, final Schema fieldSchema)
					throws NotBoundException {
		LOG.debug("Loading field: " + fieldName + " : " + fieldSchema);
		Object value = null;
		switch (fieldSchema.getType()) {
		case ARRAY:
			value = new UriArray(Uri.withAppendedPath(rootUri, fieldName),
					fieldSchema).load(resolver, fieldName);
			break;
		case BOOLEAN:
			value = (cursor.getInt(
					DbUtil.getFieldIndex(cursor, fieldName)) == 1);
			break;
		case BYTES:
			// TODO: Should these be handled using streams?
			value = cursor.getBlob(
					DbUtil.getFieldIndex(cursor, fieldName));
			break;
		case DOUBLE:
			value = cursor.getDouble(
					DbUtil.getFieldIndex(cursor, fieldName));
			break;
		case ENUM:
			value = cursor.getInt(
					DbUtil.getFieldIndex(cursor, fieldName));
			break;
		case FIXED:
			// TODO: Should these be handled using streams?
			value = cursor.getBlob(
					DbUtil.getFieldIndex(cursor, fieldName));
			break;
		case FLOAT:
			value = cursor.getFloat(
					DbUtil.getFieldIndex(cursor, fieldName));
			break;
		case INT:
			value = cursor.getInt(
					DbUtil.getFieldIndex(cursor, fieldName));
			break;
		case LONG:
			value = cursor.getLong(
					DbUtil.getFieldIndex(cursor, fieldName));
			break;
		case MAP:
			value = new UriMap(Uri.withAppendedPath(rootUri, fieldName),
					fieldSchema).load(resolver, fieldName);
			break;
		case NULL:
			value = null;
			break;
		case RECORD:
			int recordId = cursor.getInt(
					DbUtil.getFieldIndex(cursor, fieldName));
			if (recordId > 0) {
				Uri recordUri = getRecordUri(rootUri, fieldSchema);
				value = new UriRecord(Uri.withAppendedPath(recordUri,
						String.valueOf(recordId)), fieldSchema).load(resolver);
			} else {
				value = null;
			}
			break;
		case STRING:
			LOG.debug("Loading {} : columns: {}", fieldName,
					cursor.getColumnNames());
			value = cursor.getString(DbUtil.getFieldIndex(cursor, fieldName));
			LOG.debug("Loaded value: " + value);
			break;
		case UNION:
			value = new UriUnion(fieldSchema).load(resolver, rootUri, cursor,
					fieldName);
			break;
		default:
			throw new IllegalArgumentException(
					"Unsupported type: " + fieldSchema);
		}
		return value;
	}

	/**
	 * @param rootUri the root uri we are working with
	 * @param fieldSchema the schema for the record
	 * @return the uri for a record
	 */
	static Uri getRecordUri(final Uri rootUri, final Schema fieldSchema) {
		UriMatch match = EntityUriMatcher.getMatch(rootUri);
		return Uri.withAppendedPath(match.getCheckoutUri(),
				fieldSchema.getFullName());
	}

	/**
	 * Stores data to the given Uri.
	 * @param resolver the resolver to store with
	 * @param rootUri the root uri for what we are storing
	 * @param values the values to store to
	 * @param fieldName the name of the field
	 * @param fieldSchema the schema for the field
	 * @param data the data for the field
	 * @return the uri for what was stored
	 * @throws NotBoundException if the data is not bound properly
	 */
	@SuppressWarnings("rawtypes")
	static Uri storeDataToUri(final ContentResolver resolver,
			final Uri rootUri, final ContentValues values,
			final String fieldName, final Schema fieldSchema, final Object data)
					throws NotBoundException {
		LOG.debug("Storing to: " + rootUri + " fieldName: {} schema: {}",
				fieldName, fieldSchema);
		Uri dataUri = null;
		switch (fieldSchema.getType()) {
		case ARRAY:
			if (data != null) {
				UriArray array = (UriArray) data;
				array.save(resolver, fieldName);
				dataUri = array.getInstanceUri();
			} else {
				// Make sure any old values don't exist
				LOG.warn("Clearing old values.");
				new UriArray(Uri.withAppendedPath(rootUri, fieldName),
						fieldSchema).delete(resolver);
			}
			break;
		case BOOLEAN:
			values.put(fieldName, (Boolean) data);
			break;
		case BYTES:
			values.put(fieldName, (byte[]) data);
			break;
		case DOUBLE:
			values.put(fieldName, (Double) data);
			break;
		case ENUM:
			values.put(fieldName, (Integer) data);
			break;
		case FIXED:
			values.put(fieldName, (byte[]) data);
			break;
		case FLOAT:
			values.put(fieldName, (Float) data);
			break;
		case INT:
			values.put(fieldName, (Integer) data);
			break;
		case LONG:
			values.put(fieldName, (Long) data);
			break;
		case MAP:
			if (data != null) {
				UriMap map = (UriMap) data;
				map.save(resolver, fieldName);
				dataUri = map.getInstanceUri();
			} else {
				new UriMap(Uri.withAppendedPath(rootUri, fieldName),
						fieldSchema).delete(resolver);
			}
			break;
		case NULL:
			values.putNull(fieldName);
			break;
		case RECORD:
			if (data != null) {
				UriRecord record = (UriRecord) data;
				record.save(resolver);
				dataUri = record.getInstanceUri();
			}
			break;
		case STRING:
			values.put(fieldName, (String) data);
			break;
		case UNION:
			if (data != null) {
				UriUnion union = (UriUnion) data;
				union.save(resolver, rootUri, values, fieldName);
			}
			break;
		default:
			throw new IllegalArgumentException(
					"Unsupported type: " + fieldSchema);
		}

		return dataUri;
	}

	/**
	 * Inserts the values in the given uri.
	 * @param resolver the resolver to use
	 * @param baseUri the uri to insert to
	 * @param contentValues the values to be inserted
	 * @return the uri for the inserted data
	 */
	public static Uri insertUri(final ContentResolver resolver,
			final Uri baseUri, final ContentValues contentValues) {
		LOG.debug("Inserting into {}", baseUri);
		return resolver.insert(baseUri, contentValues);
	}

	/**
	 * Updates the data at a uri or throws an exception.
	 * @param resolver the resolver to use
	 * @param rootUri the uri to update
	 * @param values the values to store
	 */
	public static void updateUriOrThrow(final ContentResolver resolver,
			final Uri rootUri, final ContentValues values) {
		LOG.debug("Updating: " + rootUri);
		if (values.size() > 0) {
			// Turns out update returns 0 if nothing changed in the row.
			//          int count =
			resolver.update(rootUri, values, null, null);
			//          if (count != 1) {
			//              throw new RuntimeException(
			//                      "Error updating record. Count was: "
			//                      + count);
			//          }
		}
	}
}
