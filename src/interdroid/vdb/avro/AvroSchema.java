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
package interdroid.vdb.avro;


import org.apache.avro.Schema;

/**
 * The schema for an avro schema.
 *
 * @author nick &lt;palmer@cs.vu.nl&gt;
 *
 */
public final class AvroSchema {

	/**
	 * Prevent construction.
	 */
	private AvroSchema() {
		// No Construction;
	}

	/**
	 * The namespace for avro schemas.
	 */
	public static final String NAMESPACE =
			"interdroid.vdb.content.avro.schemas";

	/**
	 * The schema for avro schemas.
	 */
	public static final Schema SCHEMA;

	/**
	 * The schema for avro records.
	 */
	public static final Schema RECORD;

	/**
	 * The name of the record definition within the schema for schemas.
	 */
	//public static final String RECORD_DEFINITION = "Record";
	public static final String RECORD_DEFINITION = "SimpleRecord";

	public static final String SIMPLE_RECORD_DEFINITION = "SimpleRecord";

	// CHECKSTYLE:OFF
	// Yes, lines are long but this is by design so we can deal with bugs
	// by line number thrown by the Schema parser.

	// TODO: It would be nice to support cross namespace records and such.
	// TODO: Extract constants
	/*
	+"\n                             ,{\"name\": \"defaultValue\", \"type\":"
	+"\n                              {\"type\": \"record\", \"name\": \"JsonValue\","
	+"\n                               \"fields\": ["
	+"\n                                   {\"name\": \"value\","
	+"\n                                    \"type\": ["
	+"\n                                        \"long\", \"double\", \"string\","
	+"\n                                        \"boolean\", \"null\","
	+"\n                                        {\"type\": \"record\", \"name\": \"JsonArray\","
	+"\n                                         \"fields\":["
	+"\n                                             {\"name\": \"elements\","
	+"\n                                              \"type\": {\"type\": \"array\","
	+"\n                                                       \"items\": \"JsonValue\"}}"
	+"\n                                         ]"
	+"\n                                        },"
	+"\n                                        {\"type\": \"array\", \"items\": "
	+"\n                                         {\"type\": \"record\", \"name\": \"JsonField\","
	+"\n                                          \"fields\": ["
	+"\n                                              {\"name\": \"name\", \"type\": \"string\"},"
	+"\n                                              {\"name\": \"value\", \"type\": \"JsonValue\"}"
	+"\n                                          ]"
	+"\n                                         }"
	+"\n                                        }"
	+"\n                                    ]"
	+"\n                                   }"
	+"\n                               ]"
	+"\n                              }"
	+"\n                             },"
	*/
	static {
		// Taken from proposed schema for schemas:
		final String schema =
			"{\"type\": \"record\", \"name\": \"Type\", \"namespace\": \"interdroid.vdb.content.avro.schemas\","
			+ "\n "
			+ "\n \"fields\": ["
			+ "\n     {\"name\": \"type\", \"type\": ["
			+ "\n         {\"type\": \"record\", \"name\": \"SimpleRecord\","
			+ "\n          \"fields\": ["
			+ "\n              {\"name\": \"name\", \"type\": \"string\", \"ui.label\": \"Type Name\", \"ui.required\" : \"true\", \"ui.list\": \"true\"},"
//			+ "\n              {\"name\": \"doc\", \"type\": \"string\"},"
			+ "\n              {\"name\": \"namespace\", \"type\": \"string\", \"ui.list\": \"true\", \"ui.label\": \"Repository Name\", \"ui.visible\": \"false\"},"
			+ "\n              {\"name\": \"aliases\", \"type\": {\"type\": \"array\", \"items\": {\"type\": \"string\"}}, \"ui.visible\": \"false\"},"
			+ "\n              {\"name\": \"fields\","
			+ "\n               \"type\": {\"type\": \"array\", \"items\":"
			+ "\n                        {\"type\": \"record\", \"name\": \"FieldDef\","
			+ "\n                         \"fields\": ["
			+ "\n                             {\"name\": \"name\", \"type\": \"string\"},"
//			+ "\n                             {\"name\": \"label\", \"type\": \"string\"},"
//			+ "\n                             {\"name\": \"doc\", \"type\": \"string\"},"
			+ "\n                             {\"name\": \"list\", \"ui.label\": \"Show In List\", \"type\": \"boolean\", \"default\": true},"
			+ "\n                             {\"name\": \"aliases\", \"type\": {\"type\": \"array\", \"items\": {\"type\": \"string\"}}, \"ui.visible\":\"false\"},"
			+ "\n                             {\"name\": \"type\", \"default\": \"String\", \"type\":"
			+ "\n                                 {\"type\": \"enum\", \"name\": \"SimpleType\","
			+ "\n                                  \"ui.label\": \"Type\", \"symbols\": ["
			+ "\n                                      \"String\", \"Number\", \"Checkbox\", \"Date\", \"Time\", \"Photo\", \"Location\"]}"
			+ "\n                             }"
//			+ "\n                             {\"name\": \"order\","
//			+ "\n                              \"type\": {\"type\": \"enum\", \"name\": \"SortOrder\","
//			+ "\n                                       \"symbols\": [\"INCREASING\", \"DECREASING\","
//			+ "\n                                                   \"IGNORE\"]}}"
			+ "\n                         ]"
			+ "\n                        }"
			+ "\n                       }"
			+ "\n              }"
			+ "\n          ]"
//			+ "\n         },"
//			+ "\n         {\"type\": \"record\", \"name\": \"Enumeration\","
//			+ "\n          \"fields\": ["
//			+ "\n              {\"name\": \"name\", \"type\": \"string\"},"
//			+ "\n              {\"name\": \"namespace\", \"type\": \"string\"},"
//			+ "\n              {\"name\": \"doc\", \"type\": \"string\"},"
////			+ "\n              {\"name\": \"aliases\", \"type\": {\"type\": \"array\", \"items\": {\"type\": \"string\"}}},"
//			+ "\n              {\"name\": \"symbols\","
//			+ "\n               \"type\": {\"type\": \"array\", \"items\": \"string\"}}"
//			+ "\n          ]"
//			+ "\n         },"
//			+ "\n         {\"type\": \"record\", \"name\": \"Fixed\","
//			+ "\n          \"fields\": ["
//			+ "\n              {\"name\": \"name\", \"type\": \"string\"},"
//			+ "\n              {\"name\": \"namespace\", \"type\": \"string\"},"
//			+ "\n              {\"name\": \"doc\", \"type\": \"string\"},"
//			+ "\n              {\"name\": \"aliases\", \"type\": {\"type\": \"array\", \"items\": {\"type\": \"string\"}}},"
//			+ "\n              {\"name\": \"size\", \"type\": \"int\"}"
//			+ "\n          ]"
//			+ "\n         },"
//			+ "\n         {\"type\": \"record\", \"name\": \"Array\","
//			+ "\n          \"fields\": ["
//			+ "\n              {\"name\": \"elements\", \"type\": \"Type\"}"
//			+ "\n          ]"
//			+ "\n         },"
//			+ "\n         {\"type\": \"record\", \"name\": \"Map\","
//			+ "\n          \"fields\": ["
//			+ "\n              {\"name\": \"values\", \"type\": \"Type\"}"
//			+ "\n          ]"
//			+ "\n         },"
//			+ "\n         {\"type\": \"record\", \"name\": \"Union\","
//			+ "\n          \"fields\": ["
//			+ "\n              {\"name\": \"branches\","
//			+ "\n               \"type\": {\"type\": \"array\", \"items\": \"Type\"}}"
//			+ "\n          ]"
//			+ "\n         },"
//			+ "\n         {\"type\": \"record\", \"name\": \"Primitive\", \"ui.label\": \"Primitive Type\","
//			+ "\n          \"fields\": ["
//			+ "\n             {\"name\": \"PrimitiveType\", \"ui.label\": \"Primitive Type\","
//			+ "\n              \"type\": {\"type\": \"enum\", \"ui.label\": \"Primitive Type\", "
//			+ "\n                         \"name\": \"PrimitiveType\","
//			+ "\n                         \"symbols\": [\"String\", \"Bytes\", \"Int\","
//			+ "\n                         \"Long\", \"Float\", \"Double\", \"Boolean\", \"Null\"]}}"
//			+ "\n          ]"
//			+ "\n         },"
//			+ "\n         {\"type\": \"record\", \"name\": \"Complex\", \"ui.label\": \"Complex Type\","
//			+ "\n          \"fields\": ["
//			+ "\n             {\"name\": \"ComplexType\", \"ui.label\": \"Complex Type\","
//			+ "\n              \"type\": {\"type\": \"enum\", \"ui.label\": \"Complex Type\","
//			+ "\n                         \"name\": \"ComplexType\","
//			+ "\n                         \"symbols\": [\"Date\", \"Time\", \"Photo\","
//			+ "\n                         \"Location\"]}}"
//			+ "\n          ]"
			+ "\n         }"
			+ "\n     ]}"
			+ "\n ]"
			+ "\n}";
		// CHECKSTYLE:ON

		SCHEMA = Schema.parse(schema);
		RECORD = SCHEMA.getField("type").schema().getTypes().get(0);
	}
}
