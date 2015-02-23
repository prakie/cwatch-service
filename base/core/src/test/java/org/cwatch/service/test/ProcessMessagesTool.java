package org.cwatch.service.test;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map.Entry;

import org.cwatch.vdm.ais.AisMessage;
import org.cwatch.vdm.ais.AisMessageContainer;
import org.cwatch.vdm.ais.gson.AisGsonConverter;
import org.cwatch.vdm.ais.gson.CbInfoCbGsonAdapter;

import ssn.spm.domain.vdm.commentblock.CbInfoCb;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProcessMessagesTool {

	public static void main(String[] args) throws Exception {
		CbInfoCbGsonAdapter<CbInfoCb> adapter = new CbInfoCbGsonAdapter<CbInfoCb>(CbInfoCb.class);
		AisGsonConverter<AisMessageContainer> gsonconv = new AisGsonConverter<AisMessageContainer>(adapter);

		ArrayListMultimap<String, AisMessage> map = ArrayListMultimap.create();
		
		GsonBuilder gsonBilder = new GsonBuilder();
		gsonBilder.registerTypeAdapter((Type)adapter.getAdapterClass(), adapter);
		Gson gson = gsonBilder.setPrettyPrinting().create();
		
		int fileCount = 0;
		int msgCount = 0;
		
		
		for (File file : Files.fileTreeTraverser().breadthFirstTraversal(new File(".")).filter(Files.isFile())) {
			AisMessageContainer msgs = gsonconv.fromJson(Files.toByteArray(file), AisMessageContainer.class);

			fileCount++;
			
			for (AisMessage msg : msgs.getAisMessages()) {
				
				msgCount++;
				
				map.put(
						msg.getPositionReport().getAisMessageType(),
						msg
				);
				
			}
			
		}
		
		for (Entry<String, Collection<AisMessage>> entry : map.asMap().entrySet()) {
			System.out.println(entry.getKey() + " - " + entry.getValue().size());
			
			AisMessageContainer c = new AisMessageContainer();
			c.getAisMessages().add(entry.getValue().iterator().next());
			System.out.println(new String(gson.toJson(c)));
		}
		
		System.out.println("files: " + fileCount + " messages: " + msgCount + " msg/file: " + (double)msgCount/fileCount);
		
	}
	
}
