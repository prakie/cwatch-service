package org.cwatch.service.test;

import javax.annotation.Resource;

import org.apache.camel.component.gson.GsonDataFormat;
import org.cwatch.service.routes.AisToCdfRouteBuilder;
import org.cwatch.vdm.ais.AisMessageContainer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AisToCdfRouteBuilder.class)
public class VdmHttpFormatTest {

	@Resource
	GsonDataFormat aisMessageContainerGsonDataFormat;
	
	@Test
	public void testMonitoringPart() throws Exception {
		AisMessageContainer object = (AisMessageContainer) aisMessageContainerGsonDataFormat.unmarshal(
				null, 
				VdmHttpFormatTest.class.getResourceAsStream("/vdmHttp-withoutMonitoring.json")
		);
		
		Assert.assertNull(object.getMonitoringMessage());
	}
	
}
