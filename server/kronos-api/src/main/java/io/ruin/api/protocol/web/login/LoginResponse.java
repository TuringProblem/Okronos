package io.ruin.api.protocol.web.login;

import com.google.common.collect.Maps;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Map;

@ToString
@EqualsAndHashCode
@Getter
public class LoginResponse {

	/**
	 * Valid responses
	 * INVALID_PASSWORD
	 * INVALID_EMAIL
	 * VALID
	 */
	String status;
	int userId = -1;
	Map<String, Object> miscParams;

	public LoginResponse(Map<String, Object> response) {
		if (response == null) {
			status = "ERROR";
		} else {
			status = (String) response.getOrDefault("STATUS", "ERROR");
			userId = ((BigDecimal) response.getOrDefault("MEMBER_ID", BigDecimal.valueOf(-1))).intValue();
			miscParams = Maps.newConcurrentMap();
			miscParams.putAll(response);
			miscParams.remove("STATUS");
			miscParams.remove("MEMBER_ID");
		}
	}
}