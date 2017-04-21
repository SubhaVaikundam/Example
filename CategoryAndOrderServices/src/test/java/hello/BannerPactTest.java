package hello;


import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.PactRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.model.PactFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

import utils.Configuration;
import static junit.framework.TestCase.assertEquals;


public class BannerPactTest
{

	@Rule
	public PactRule rule = new PactRule(Configuration.MOCK_HOST, Configuration.MOCK_PORT, this);
	//private DslPart testResults;
	JSONObject testResults = new JSONObject();

	@Pact(state = "BannerService", provider = Configuration.PROVIDER_NAME, consumer = Configuration.CONSUMER_NAME)
	public PactFragment createFragment(ConsumerPactBuilder.PactDslWithProvider.PactDslWithState builder)
	{
		JSONObject jsonData = new JSONObject();
		jsonData.put("bannerName", "FreeShipping");
		jsonData.put("bannerUrl", "./img/free_shipping_banner.jpg");
		List <JSONObject> arrayData = new ArrayList<JSONObject>();
		arrayData.add(jsonData);
		testResults.put("bannerBean", arrayData);
			
		return builder
				.uponReceiving("get banner service response")
				.path("/getBanner")
				.method("GET")
				.willRespondWith()
				.status(200)
				.headers(Configuration.HEADER)
				.body(testResults)
				.toFragment();
	}

	@Test
	@PactVerification("BannerService")
	public void testBanner() throws IOException
	{
		DummyConsumer dConsumer = new DummyConsumer(Configuration.SERVICE_URL);
		assertEquals(testResults.toString(), dConsumer.getResponseAsString("/getBanner"));
	}
}
