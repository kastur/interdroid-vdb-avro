package interdroid.vdb.avro.view.factory;

import interdroid.vdb.avro.control.handler.EditTextHandler;
import interdroid.vdb.avro.control.handler.ValueHandler;
import interdroid.vdb.avro.model.AvroRecordModel;
import interdroid.vdb.avro.model.NotBoundException;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;

import android.app.Activity;
import android.net.Uri;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;

/**
 * A builder for Type.STRING.
 *
 * @author nick &lt;palmer@cs.vu.nl&gt;
 *
 */
class AvroStringBuilder extends AvroViewBuilder {

	/**
	 * Construct a buidler for Type.STRING.
	 */
	protected AvroStringBuilder() {
		super(Type.STRING);
	}

	@Override
	public final View buildEditView(final Activity activity,
			final AvroRecordModel dataModel,
			final ViewGroup viewGroup, final Schema schema,
			final String field, final Uri uri,
			final ValueHandler valueHandler) throws NotBoundException {
		return buildEditText(activity, viewGroup, schema,
				InputType.TYPE_CLASS_TEXT,
				new EditTextHandler(dataModel, schema.getType(), valueHandler));
	}

}