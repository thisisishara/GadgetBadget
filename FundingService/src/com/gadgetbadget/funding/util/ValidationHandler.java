package com.gadgetbadget.funding.util;

import java.math.BigInteger;
import java.sql.Timestamp;

import com.google.gson.JsonObject;

public class ValidationHandler {
	public boolean verifyPaymentMethod(JsonObject paymentMethodDetails) {
		try {
			if (! (paymentMethodDetails.has("creditcard_no") || paymentMethodDetails.has("creditcard_security_no") || paymentMethodDetails.has("exp_date") || paymentMethodDetails.has("creditcard_type"))) {
				return false;
			}

			BigInteger cc_no = new BigInteger(paymentMethodDetails.get("creditcard_no").getAsString());

			if (paymentMethodDetails.get("creditcard_type").getAsString().equalsIgnoreCase("Master") || paymentMethodDetails.get("creditcard_type").getAsString().equalsIgnoreCase("Visa")) {
				int cc_length = paymentMethodDetails.get("creditcard_no").getAsString().length();
				if(!( cc_length == 13 || (cc_length == 16))) {
					return false;
				}

				int cc_securitycode_length = paymentMethodDetails.get("creditcard_security_no").getAsString().length();				
				if( ! (cc_securitycode_length == 3 || cc_securitycode_length == 4)) {
					return false;
				}
				
			} else {
				if(paymentMethodDetails.get("creditcard_no").getAsString().length() != 16) {
					return false;
				}

				if(paymentMethodDetails.get("creditcard_security_no").getAsString().length() != 4) {
					return false;
				}
			}

			Timestamp expTimestamp = Timestamp.valueOf(paymentMethodDetails.get("exp_date").getAsString());
			Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
			
			if(expTimestamp.before(nowTimestamp)) {
				return false;
			}
			
			return true;

		} catch (Exception ex) {
			return false;
		}
	}
}
