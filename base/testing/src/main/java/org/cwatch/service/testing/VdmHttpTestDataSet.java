package org.cwatch.service.testing;

import org.apache.camel.component.dataset.DataSetSupport;
import org.springframework.stereotype.Component;

@Component("vdmHttpTestDataSet")
public class VdmHttpTestDataSet extends DataSetSupport {

	public VdmHttpTestDataSet() {
		super(5);
	}
	
	@Override
	protected Object createMessageBody(long messageIndex) {
		return VdmHttpTestDataSet.class.getResource(String.format("/ais%02d.json.gz", messageIndex+1));
	}
	
	
}
