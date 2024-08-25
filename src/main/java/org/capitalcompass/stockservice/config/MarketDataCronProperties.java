package org.capitalcompass.stockservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("marketDataCronProperties")
@ConfigurationProperties(prefix = "market-data")
public class MarketDataCronProperties {
	
	private String fetchCron;

	private String deleteDuplicatesCron;

	public String getFetchCron() {
		return fetchCron;
	}

	public void setFetchCron(String fetchCron) {
		this.fetchCron = fetchCron;
	}

	public String getDeleteDuplicatesCron() {
		return deleteDuplicatesCron;
	}

	public void setDeleteDuplicatesCron(String deleteDuplicatesCron) {
		this.deleteDuplicatesCron = deleteDuplicatesCron;
	}

}
