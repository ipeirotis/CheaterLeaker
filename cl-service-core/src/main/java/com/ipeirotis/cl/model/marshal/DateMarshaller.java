package com.ipeirotis.cl.model.marshal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMarshaller;

public class DateMarshaller implements DynamoDBMarshaller<Date> {
	private static final DateFormat DF = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Override
	public String marshall(Date getterReturnResult) {
		return DF.format(getterReturnResult);
	}

	@Override
	public Date unmarshall(Class<Date> clazz, String obj) {
		try {
			return DF.parse(obj);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}