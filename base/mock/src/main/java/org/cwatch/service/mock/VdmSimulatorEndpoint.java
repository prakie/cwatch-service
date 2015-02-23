package org.cwatch.service.mock;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;

public class VdmSimulatorEndpoint extends DefaultEndpoint {

	@Override
	public Producer createProducer() throws Exception {
		throw new RuntimeCamelException();
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		return new VdmSimulatorConsumer(this, processor);
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	protected String createEndpointUri() {
		return "vdmsim://testproxy";
	}
	
}
